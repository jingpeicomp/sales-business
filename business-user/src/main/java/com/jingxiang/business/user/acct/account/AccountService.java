package com.jingxiang.business.user.acct.account;

import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.bill.AccountBill;
import com.jingxiang.business.user.acct.account.bill.AccountBillRepository;
import com.jingxiang.business.user.acct.common.consts.AccountType;
import com.jingxiang.business.user.acct.common.consts.AcctConsts;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import com.jingxiang.business.user.uc.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

/**
 * 账户服务接口
 * Created by liuzhaoming on 2019/8/25.
 */
@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountBillRepository billRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 订单支付成功引起的账户变更，影响买家、卖家、合伙人三个账户
     *
     * @param payment 支付单
     */
    public void orderPaid(PaymentVo payment) {
        log.info("Order paid account operation start, payment is {} ", payment);
        AccountBill buyerBill = buyerOrderPaid(payment);
        log.info("Order paid account operation buyer finish {}", buyerBill);

        AccountBill sellerAccountBill = sellerOrderPaid(payment);
        log.info("Order paid account operation seller finish {}", sellerAccountBill);

        AccountBill partnerAccountBill = partnerOrderPaid(payment, sellerAccountBill);
        log.info("Order paid account operation partner finish {}", partnerAccountBill);
    }

    /**
     * 订单支付成功买家账户操作，涉及多个账户，手工控制事务
     *
     * @param payment 订单支付记录
     * @return 买家账户流水
     */
    private AccountBill buyerOrderPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(10);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account buyerAccount = accountRepository.findByUserIdAndType(payment.getPayer(), AccountType.USER.getValue())
                    .orElseThrow(() -> {
                        log.error("Cannot find buyer account {}", payment.getPayer());
                        throw new ServiceException("找不到对应的账户,ID:" + payment.getPayer());
                    });
            AccountBill bill = buyerAccount.buyerOrderPaid(payment);
            accountRepository.save(buyerAccount);
            billRepository.save(bill);
            transactionManager.commit(status);
            return bill;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 订单支付成功卖家账户操作，涉及多个账户，手工控制事务
     *
     * @param payment 订单支付记录
     * @return 卖家账户流水
     */
    private AccountBill sellerOrderPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(10);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account buyerAccount = accountRepository.findByUserIdAndType(payment.getPayee(), AccountType.SELLER.getValue())
                    .orElseThrow(() -> {
                        log.error("Cannot find seller account {}", payment.getPayee());
                        throw new ServiceException("找不到对应的账户,ID:" + payment.getPayee());
                    });
            AccountBill bill = buyerAccount.sellerOrderPaid(payment);
            accountRepository.save(buyerAccount);
            billRepository.save(bill);
            transactionManager.commit(status);
            return bill;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 订单支付成功合伙人账户操作，涉及多个账户，手工控制事务
     *
     * @param payment           支付单
     * @param sellerAccountBill 卖家账户流水
     * @return 合伙人账户流水
     */
    private AccountBill partnerOrderPaid(PaymentVo payment, AccountBill sellerAccountBill) {
        ShopVo shop = shopService.queryVoById(payment.getShopId())
                .orElseThrow(() -> {
                    log.error("Cannot find shop {}", payment.getShopId());
                    throw new ServiceException("找不到对应的店铺,ID:" + payment.getShopId());
                });
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(10);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account partnerAccount;
            if (StringUtils.isBlank(shop.getPartner())) {
                //店铺没有合伙人，系统作为合伙人
                partnerAccount = querySystemPartnerAccount();
            } else {
                partnerAccount = accountRepository.findByUserIdAndType(shop.getPartner(), AccountType.PARTNER.getValue())
                        .orElseThrow(() -> {
                            log.error("Cannot find partner account {}", shop.getPartner());
                            throw new ServiceException("找不到对应的账户,ID:" + shop.getPartner());
                        });
            }
            AccountBill bill = partnerAccount.partnerOrderPaid(payment, sellerAccountBill.getAmount(), sellerAccountBill.getSfRate());
            accountRepository.save(partnerAccount);
            billRepository.save(bill);
            transactionManager.commit(status);
            return bill;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 获取系统合伙人账户，主要用于计算服务费
     *
     * @return 系统合伙人账户
     */
    public Account querySystemPartnerAccount() {
        return Optional.ofNullable(accountRepository.findOne(AcctConsts.ID_ACCOUNT_SYSTEM_PARTNER))
                .orElseThrow(() -> {
                    log.error("Cannot find system partner account, please check!");
                    throw new ServiceException("找不到对应的账户,ID:", AcctConsts.ID_ACCOUNT_SYSTEM_PARTNER);
                });
    }

    /**
     * 获取系统卖家账户
     *
     * @return 系统卖家账户
     */
    public Account querySystemSellerAccount() {
        return Optional.ofNullable(accountRepository.findOne(AcctConsts.ID_ACCOUNT_SYSTEM_SELLER))
                .orElseThrow(() -> {
                    log.error("Cannot find system seller account, please check!");
                    throw new ServiceException("找不到对应的账户,ID:", AcctConsts.ID_ACCOUNT_SYSTEM_SELLER);
                });
    }
}
