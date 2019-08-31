package com.jingxiang.business.user.acct.account.bill;

import com.jingxiang.business.consts.Role;
import com.jingxiang.business.user.acct.common.consts.AccountOperation;
import com.jingxiang.business.user.acct.common.consts.AccountOperationTarget;
import com.jingxiang.business.user.acct.common.consts.AccountType;
import com.jingxiang.business.user.acct.common.consts.FundDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户流水
 * Created by liuzhaoming on 2019/8/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_BIZ_UC_ACCOUNT_BILL")
@EntityListeners(AuditingEntityListener.class)
public class AccountBill implements Serializable {

    /**
     * 流水ID
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '流水ID'")
    private String id;

    /**
     * 账户编号
     */
    @Column(name = "ACCOUNT_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '账户编号'")
    private String accountId;

    /**
     * 用户编号
     */
    @Column(name = "USER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '用户编号'")
    private String userId;

    /**
     * 账户类型
     */
    @Column(name = "ACCOUNT_TYPE", nullable = false, updatable = false, columnDefinition = "smallint comment '账户类型'")
    @Convert(converter = AccountType.EnumConvert.class)
    private AccountType accountType;

    /**
     * 资金流向
     */
    @Column(name = "FUND_DIRECTION", nullable = false, updatable = false, columnDefinition = "smallint comment '资金流向'")
    @Convert(converter = FundDirection.EnumConvert.class)
    private FundDirection fundDirection;

    /**
     * 账户操作
     */
    @Column(name = "OPERATION", nullable = false, updatable = false, columnDefinition = "smallint comment '账户操作'")
    @Convert(converter = AccountOperation.EnumConvert.class)
    private AccountOperation operation;

    /**
     * 操作人角色
     */
    @Column(name = "OPERATOR_ROLE", nullable = false, updatable = false, columnDefinition = "smallint comment '操作人角色'")
    @Convert(converter = Role.EnumConvert.class)
    private Role operatorRole;

    /**
     * 操作人
     */
    @Column(name = "OPERATOR", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '操作人'")
    private String operator;

    /**
     * 账户对象
     */
    @Column(name = "TARGET", nullable = false, updatable = false, columnDefinition = "smallint comment '账户对象'")
    @Convert(converter = AccountOperationTarget.EnumConvert.class)
    private AccountOperationTarget target;

    /**
     * 金额
     */
    @Column(name = "AMOUNT", columnDefinition = "decimal(20,2) comment '金额'")
    private BigDecimal amount;

    /**
     * 请求号
     */
    @Column(name = "REQUEST_ID", updatable = false, columnDefinition = "varchar(64) comment '请求号'")
    private String requestId;

    /**
     * 支付单ID
     */
    @Column(name = "PAYMENT_ID", updatable = false, columnDefinition = "varchar(32) comment '支付单ID'")
    private String paymentId;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", updatable = false, columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '创建时间'")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /* ************************************************************************************************************
     *
     * 账户信息快照，主要方便分析定位问题
     *
     * ***********************************************************************************************************/
    /**
     * 账户余额
     */
    @Builder.Default
    @Column(name = "BALANCE", columnDefinition = "decimal(20,2) comment '账户余额'")
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * 总收益
     */
    @Builder.Default
    @Column(name = "TOTAL_INCOME", columnDefinition = "decimal(20,2) comment '总收益'")
    private BigDecimal totalIncome = BigDecimal.ZERO;

    /**
     * 总支出
     */
    @Builder.Default
    @Column(name = "TOTAL_EXPEND", columnDefinition = "decimal(20,2) comment '总支出'")
    private BigDecimal totalExpend = BigDecimal.ZERO;

    /**
     * 服务费(service fee)账户余额
     */
    @Builder.Default
    @Column(name = "SF_BALANCE", columnDefinition = "decimal(20,2) comment '服务费账户余额'")
    private BigDecimal sfBalance = BigDecimal.ZERO;

    /**
     * 服务费(service fee)总收益
     */
    @Builder.Default
    @Column(name = "TOTAL_SF_INCOME", columnDefinition = "decimal(20,2) comment '服务费总收益'")
    private BigDecimal totalSfIncome = BigDecimal.ZERO;

    /**
     * 服务费(service fee)总支出
     */
    @Builder.Default
    @Column(name = "TOTAL_SF_EXPEND", columnDefinition = "decimal(20,2) comment '服务费总支出'")
    private BigDecimal totalSfExpend = BigDecimal.ZERO;

    /**
     * 银行手续费(bank fee)总支出
     */
    @Builder.Default
    @Column(name = "TOTAL_BF_EXPEND", columnDefinition = "decimal(20,2) comment '银行手续费总支出'")
    private BigDecimal totalBfExpend = BigDecimal.ZERO;

    /**
     * 服务费（service free）费率
     */
    @Column(name = "SF_RATE", columnDefinition = "decimal(10,2) comment '服务费费率'")
    private BigDecimal sfRate;

    /**
     * 银行手续费（bank free）费率
     */
    @Column(name = "BF_RATE", columnDefinition = "decimal(10,2) comment '银行手续费费率'")
    private BigDecimal bfRate;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;
}
