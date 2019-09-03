package com.jingxiang.business.user.acct.account;

import com.jingxiang.business.base.BusinessConsts;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.bill.AccountBill;
import com.jingxiang.business.user.acct.account.bill.AccountBillRepository;
import com.jingxiang.business.user.acct.common.consts.AccountType;
import com.jingxiang.business.user.acct.common.consts.AcctConsts;
import com.jingxiang.business.user.acct.common.consts.WithdrawalType;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalVo;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import com.jingxiang.business.user.uc.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
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
        log.info("Order paid account operation start, payment is {}", payment);
        AccountBill buyerBill = buyerOrderPaid(payment);
        log.info("Order paid account operation payer finish {}", buyerBill);

        List<AccountBill> sellerAccountBills = sellerOrderPaid(payment);
        log.info("Order paid account operation seller finish {}", sellerAccountBills);

        AccountBill partnerAccountBill = partnerOrderPaid(payment, sellerAccountBills.get(0));
        log.info("Order paid account operation partner finish {}", partnerAccountBill);
    }

    /**
     * 充值单支付成功引起的账户变更，影响卖家、系统两个账户
     *
     * @param payment 支付单
     */
    public void depositPaid(PaymentVo payment) {
        log.info("Deposit paid account operation start, payment is {} ", payment);
        AccountBill sellerBill = sellerDepositPaid(payment);
        log.info("Deposit paid account operation seller finish {}", sellerBill);

        AccountBill systemAccountBill = systemDepositPaid(payment);
        log.info("Deposit paid account operation system finish {}", systemAccountBill);
    }

    /**
     * 提现单创建事件。因为是手工转账，提现单创建时就把账户余额给扣除掉
     *
     * @param withdrawal 提现单
     */
    public void withdrawalCreated(WithdrawalVo withdrawal) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            if (withdrawal.getType() == WithdrawalType.BALANCE) {
                Account sellerAccount = findByUserIdAndType(withdrawal.getUserId(), AccountType.SELLER);
                AccountBill sellerAccountBill = sellerAccount.sellerWithdraw(withdrawal);
                accountRepository.save(sellerAccount);
                billRepository.save(sellerAccountBill);
            } else if (withdrawal.getType() == WithdrawalType.SERVICE_FEE) {
                Account partnerAccount = findByUserIdAndType(withdrawal.getUserId(), AccountType.PARTNER);
                AccountBill partnerAccountBill = partnerAccount.partnerWithdraw(withdrawal);
                accountRepository.save(partnerAccount);
                billRepository.save(partnerAccountBill);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 根据用户ID和账户类型查询账户流水
     *
     * @param userId      用户ID
     * @param accountType 账户类型
     * @return 账户流水列表
     */
    @Transactional(readOnly = true)
    public List<AccountBill> queryBill(String userId, AccountType accountType) {
        return billRepository.queryByUserIdAndAccountTypeOrderByCreateTimeDesc(userId, accountType);
    }

    /**
     * 查询指定用户的账户，如果不存在则创建空白账户
     *
     * @param userId      用户ID
     * @param accountType 账户类型
     * @return 账户
     */
    private Account findByUserIdAndType(String userId, AccountType accountType) {
        return accountRepository.findByUserIdAndType(userId, accountType)
                .orElseGet(() -> accountRepository.save(Account.createNew(userId, accountType)));

    }

    /**
     * 订单支付成功买家账户操作，涉及多个账户，手工控制事务
     *
     * @param payment 订单支付记录
     * @return 买家账户流水
     */
    private AccountBill buyerOrderPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account buyerAccount = findByUserIdAndType(payment.getPayer(), AccountType.USER);
            AccountBill bill = buyerAccount.buyerOrderPaid(payment);
            accountRepository.save(buyerAccount);
            billRepository.save(bill);
            transactionManager.commit(status);
            return bill;
        } catch (Exception e) {
//            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * 订单支付成功卖家账户操作，涉及多个账户，手工控制事务
     *
     * @param payment 订单支付记录
     * @return 卖家账户流水
     */
    private List<AccountBill> sellerOrderPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account buyerAccount = findByUserIdAndType(payment.getPayee(), AccountType.SELLER);
            List<AccountBill> bills = buyerAccount.sellerOrderPaid(payment);
            accountRepository.save(buyerAccount);
            billRepository.save(bills);
            transactionManager.commit(status);
            return bills;
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
        ShopVo shop = shopService.queryVo(payment.getShopId())
                .orElseThrow(() -> {
                    log.error("Cannot find shop {}", payment.getShopId());
                    return new ServiceException("找不到对应的店铺,ID:" + payment.getShopId());
                });
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account partnerAccount;
            if (StringUtils.isBlank(shop.getPartner())) {
                //店铺没有合伙人，系统作为合伙人
                partnerAccount = querySystemPartnerAccount();
            } else {
                partnerAccount = findByUserIdAndType(shop.getPartner(), AccountType.PARTNER);
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
     * 充值单支付成功卖家账户操作，手工控制事务
     *
     * @param payment 充值单支付记录
     * @return 卖家账户流水
     */
    private AccountBill sellerDepositPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account buyerAccount = findByUserIdAndType(payment.getPayer(), AccountType.SELLER);
            AccountBill bill = buyerAccount.sellerSfDeposit(payment);
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
     * 充值单支付成功系统账户操作，银行手续费由平台承担
     *
     * @param payment 充值单支付记录
     * @return 系统账户流水
     */
    private AccountBill systemDepositPaid(PaymentVo payment) {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setTimeout(BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS);
        TransactionDefinition def = new DefaultTransactionDefinition(attribute);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Account systemAccount = querySystemSellerAccount();
            AccountBill bill = systemAccount.systemSfDepositAllowance(payment);
            accountRepository.save(systemAccount);
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
                    return new ServiceException("找不到对应的账户,ID:", AcctConsts.ID_ACCOUNT_SYSTEM_PARTNER);
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
                    return new ServiceException("找不到对应的账户,ID:", AcctConsts.ID_ACCOUNT_SYSTEM_SELLER);
                });
    }
}
