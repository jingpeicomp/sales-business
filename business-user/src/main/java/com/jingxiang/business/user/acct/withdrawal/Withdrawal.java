package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.acct.common.consts.AcctConsts;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.consts.WithdrawalType;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalCreateRequest;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalVo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现单
 * Created by liuzhaoming on 2019/8/27.
 */
@Entity
@Table(name = "T_BIZ_UC_WITHDRAWAL")
@Data
public class Withdrawal implements Serializable {
    /**
     * 提现单编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '提现单编号'")
    private String id;

    /**
     * 用户编号
     */
    @Column(name = "USER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '用户编号'")
    private String userId;

    /**
     * 提现单类型
     */
    @Column(name = "TYPE", nullable = false, updatable = false, columnDefinition = "smallint comment '提现单类型'")
    @Convert(converter = WithdrawalType.EnumConvert.class)
    private WithdrawalType type;

    /**
     * 提现金额
     */
    @Column(name = "AMOUNT", columnDefinition = "decimal(20,2) comment '提现金额'")
    private BigDecimal amount;

    /**
     * 银行手续费
     */
    @Column(name = "BANK_FEE", columnDefinition = "decimal(20,2) comment '银行手续费'")
    private BigDecimal bankFee = BigDecimal.ZERO;

    /**
     * 应付金额
     * payAmount = amount + bankFee
     */
    @Column(name = "PAY_AMOUNT", columnDefinition = "decimal(20,2) comment '应付金额'")
    private BigDecimal payAmount;

    /**
     * 实付金额
     */
    @Column(name = "PAID_AMOUNT", columnDefinition = "decimal(20,2) comment '实付金额'")
    private BigDecimal paidAmount;

    /**
     * 提现单支付状态
     */
    @Column(name = "PAY_STATUS", nullable = false, updatable = false, columnDefinition = "smallint comment '提现单支付状态'")
    @Convert(converter = PayStatus.EnumConvert.class)
    private PayStatus payStatus;

    /**
     * 提现单完成状态
     */
    @Column(name = "COMPLETE_STATUS", nullable = false, updatable = false, columnDefinition = "smallint comment '提现单完成状态'")
    @Convert(converter = CompleteStatus.EnumConvert.class)
    private CompleteStatus completeStatus;

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
     * 提现单成功时间
     */
    @Column(name = "FINISH_TIME", columnDefinition = "datetime comment '订单成功时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime finishTime;


    /**
     * 支付类型，微信支付:1;支付宝:2
     */
    @Column(name = "PAY_TYPE", nullable = false, columnDefinition = "smallint comment '支付类型，微信支付:1;支付宝:2'")
    @Convert(converter = PayType.EnumConvert.class)
    private PayType payType = PayType.WEIXIN;

    /**
     * 支付单号
     */
    @Column(name = "PAY_ID", columnDefinition = "varchar(32) comment '支付单号'")
    private String payId;

    /**
     * 支付时间
     */
    @Column(name = "PAY_TIME", columnDefinition = "datetime comment '支付时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    @Column(name = "TRADE_NO", columnDefinition = "varchar(64) comment '支付平台的支付单号'")
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    @Column(name = "PRE_PAY_ID", columnDefinition = "varchar(64) comment '支付网关预支付单ID'")
    private String prePlatformPayId;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 人工确认提现单
     */
    public void confirm() {
        if (completeStatus != CompleteStatus.DOING) {
            throw new ServiceException("提现单不是正在处理状态，不能确认。ID:" + id);
        }
        completeStatus = CompleteStatus.DONE;
        payStatus = PayStatus.PAID;
        paidAmount = payAmount;
        payTime = LocalDateTime.now();
        finishTime = payTime;
    }

    /**
     * 获取提现单值对象
     *
     * @return 值对象
     */
    public WithdrawalVo toVo() {
        return WithdrawalVo.builder()
                .amount(amount)
                .bankFee(bankFee)
                .completeStatus(completeStatus)
                .createTime(createTime)
                .finishTime(finishTime)
                .id(id)
                .paidAmount(paidAmount)
                .payAmount(payAmount)
                .payStatus(payStatus)
                .payType(payType)
                .type(type)
                .updateTime(updateTime)
                .userId(userId)
                .build();
    }

    /**
     * 创建充值单
     *
     * @param request 充值请求
     * @return 充值单
     */
    public static Withdrawal from(WithdrawalCreateRequest request) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAmount(request.getAmount());
        withdrawal.setBankFee(BigDecimal.ZERO);
        withdrawal.setCompleteStatus(CompleteStatus.DOING);
        withdrawal.setId(IdFactory.createUserId(AcctConsts.ID_PREFIX_WITHDRAWAL));
        withdrawal.setPayAmount(request.getAmount());
        withdrawal.setPayStatus(PayStatus.UNPAID);
        withdrawal.setType(WithdrawalType.fromValue(request.getWithdrawalType()));
        return withdrawal;
    }
}
