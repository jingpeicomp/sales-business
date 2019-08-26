package com.jingxiang.business.user.acct.account;

import com.jingxiang.business.consts.Role;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.bill.AccountBill;
import com.jingxiang.business.user.acct.common.consts.*;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户信息实体
 * Created by liuzhaoming on 2019/8/25.
 */
@Entity
@Table(name = "T_BIZ_UC_ACCOUNT")
@Data
public class Account implements Serializable {
    /**
     * 账户编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '账户编号'")
    private String id;

    /**
     * 用户编号
     */
    @Column(name = "USER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '用户编号'")
    private String userId;

    /**
     * 账户类型
     */
    @Column(name = "TYPE", nullable = false, updatable = false, columnDefinition = "smallint comment '账户类型'")
    @Convert(converter = AccountType.EnumConvert.class)
    private AccountType type;

    /**
     * 账户余额
     */
    @Column(name = "BALANCE", columnDefinition = "decimal(20,2) comment '账户余额'")
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 总收益
     */
    @Column(name = "TOTAL_INCOME", columnDefinition = "decimal(20,2) comment '总收益'")
    private BigDecimal totalIncome = BigDecimal.ZERO;

    /**
     * 总支出
     */
    @Column(name = "TOTAL_EXPEND", columnDefinition = "decimal(20,2) comment '总支出'")
    private BigDecimal totalExpend = BigDecimal.ZERO;

    /**
     * 服务费(service fee)账户余额
     */
    @Column(name = "SF_BALANCE", columnDefinition = "decimal(20,2) comment '服务费账户余额'")
    private BigDecimal sfBalance = BigDecimal.ZERO;

    /**
     * 服务费(service fee)总收益
     */
    @Column(name = "TOTAL_SF_INCOME", columnDefinition = "decimal(20,2) comment '服务费总收益'")
    private BigDecimal totalSfIncome = BigDecimal.ZERO;

    /**
     * 服务费(service fee)总支出
     */
    @Column(name = "TOTAL_SF_EXPEND", columnDefinition = "decimal(20,2) comment '服务费总支出'")
    private BigDecimal totalSfExpend = BigDecimal.ZERO;

    /**
     * 银行手续费(bank fee)总支出
     */
    @Column(name = "TOTAL_BF_EXPEND", columnDefinition = "decimal(20,2) comment '银行手续费总支出'")
    private BigDecimal totalBfExpend = BigDecimal.ZERO;

    /**
     * 银行手续费（bank free）费率
     */
    @Column(name = "BF_RATE", columnDefinition = "decimal(10,2) comment '银行手续费费率'")
    private BigDecimal bfRate = AcctConsts.WXPAY_FEE_RATE;

    /**
     * 服务费（service free）费率
     */
    @Column(name = "SF_RATE", columnDefinition = "decimal(10,2) comment '服务费费率'")
    private BigDecimal sfRate = AcctConsts.SERVICE_FEE_RATE;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", updatable = false, columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '创建时间'")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "UPDATE_TIME", columnDefinition = "datetime comment '修改时间'")
    private LocalDateTime updateTime;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 订单支付成功买家账户操作
     *
     * @param payment 订单支付记录
     * @return 买家账户流水
     */
    public AccountBill buyerOrderPaid(PaymentVo payment) {
        totalExpend = totalExpend.add(payment.getPaidAmount());
        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(payment.getPaidAmount())
                .balance(balance)
                .fundDirection(FundDirection.CREDIT)
                .operation(AccountOperation.PAY)
                .operator(userId)
                .operatorRole(Role.BUYER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.BALANCE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }

    /**
     * 订单支付成功卖家账户操作
     *
     * @param payment 订单支付记录
     * @return 卖家账户流水
     */
    public AccountBill sellerOrderPaid(PaymentVo payment) {
        BigDecimal bfFee = payment.getPaidAmount().multiply(bfRate);
        BigDecimal currentIncome = payment.getPaidAmount().subtract(bfFee);
        totalIncome = totalIncome.add(currentIncome);
        totalBfExpend = totalBfExpend.add(bfFee);
        balance = balance.add(currentIncome);
        BigDecimal sfFee = totalIncome.multiply(sfRate);
        sfBalance = sfBalance.subtract(sfFee);
        totalSfExpend = totalSfExpend.add(sfFee);

        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(currentIncome)
                .balance(balance)
                .fundDirection(FundDirection.DEBIT)
                .operation(AccountOperation.PAY)
                .operator(userId)
                .operatorRole(Role.BUYER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.BALANCE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }

    /**
     * 订单支付成功合伙人账户操作
     *
     * @param payment          支付单
     * @param sellerRealAmount 卖家实际金额（扣除微信费率后的钱）
     * @param sellerSfRate     卖家服务费费率
     * @return 合伙人账户流水
     */
    public AccountBill partnerOrderPaid(PaymentVo payment, BigDecimal sellerRealAmount, BigDecimal sellerSfRate) {
        BigDecimal sfFee = sellerRealAmount.multiply(sellerSfRate);
        totalSfIncome = totalSfIncome.add(sfFee);

        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(sfFee)
                .balance(balance)
                .fundDirection(FundDirection.DEBIT)
                .operation(AccountOperation.PAY)
                .operator(userId)
                .operatorRole(Role.BUYER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.SERVICE_FEE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }

    /**
     * 卖家服务费充值
     *
     * @param payment 服务费充值支付单
     * @return 卖家账户流水
     */
    public AccountBill sellerSfDeposit(PaymentVo payment) {
        sfBalance = sfBalance.add(payment.getPaidAmount());
        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(payment.getPaidAmount())
                .balance(balance)
                .fundDirection(FundDirection.DEBIT)
                .operation(AccountOperation.DEPOSIT)
                .operator(userId)
                .operatorRole(Role.SELLER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.SERVICE_FEE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }

    /**
     * 卖家提现
     *
     * @param payment 账户余额提现支付单
     * @return 卖家账户流水
     */
    public AccountBill sellerWithdraw(PaymentVo payment) {
        if (sfBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("账户服务费余额不足");
        }

        if (balance.compareTo(payment.getPaidAmount()) < 0) {
            throw new ServiceException("账户余额小于提现金额");
        }

        balance = balance.subtract(payment.getPaidAmount());
        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(payment.getPaidAmount())
                .balance(balance)
                .fundDirection(FundDirection.CREDIT)
                .operation(AccountOperation.WITHDRAW)
                .operator(userId)
                .operatorRole(Role.SELLER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.BALANCE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }

    /**
     * 合伙人提现
     *
     * @param payment 合伙人服务费提现支付单
     * @return 合伙人账户流水
     */
    public AccountBill partnerWithdraw(PaymentVo payment) {
        if (sfBalance.compareTo(payment.getPaidAmount()) < 0) {
            throw new ServiceException("账户服务费余额小于提现金额");
        }

        sfBalance = sfBalance.subtract(payment.getPaidAmount());
        return AccountBill.builder()
                .accountId(id)
                .userId(userId)
                .accountType(type)
                .amount(payment.getPaidAmount())
                .balance(balance)
                .fundDirection(FundDirection.CREDIT)
                .operation(AccountOperation.WITHDRAW)
                .operator(userId)
                .operatorRole(Role.PARTNER)
                .requestId(payment.getSourceId())
                .paymentId(payment.getId())
                .sfBalance(sfBalance)
                .target(AccountOperationTarget.SERVICE_FEE)
                .totalBfExpend(totalBfExpend)
                .totalExpend(totalExpend)
                .totalIncome(totalIncome)
                .totalSfExpend(totalSfExpend)
                .totalSfIncome(totalSfIncome)
                .totalBfExpend(totalBfExpend)
                .sfRate(sfRate)
                .bfRate(bfRate)
                .build();
    }
}
