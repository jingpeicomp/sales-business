package com.jingxiang.business.user.acct.deposit;

import com.jingxiang.business.api.payment.PaymentPaidRequest;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.acct.common.consts.AcctConsts;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.DepositType;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositCreateRequest;
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
 * 充值单
 * Created by liuzhaoming on 2019/8/26.
 */
@Entity
@Table(name = "T_BIZ_UC_DEPOSIT")
@Data
public class Deposit implements Serializable {
    /**
     * 充值单编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '充值单编号'")
    private String id;

    /**
     * 用户编号
     */
    @Column(name = "USER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '用户编号'")
    private String userId;

    /**
     * 充值单类型
     */
    @Column(name = "TYPE", nullable = false, updatable = false, columnDefinition = "smallint comment '充值单类型'")
    @Convert(converter = DepositType.EnumConvert.class)
    private DepositType type;

    /**
     * 充值金额
     */
    @Column(name = "AMOUNT", columnDefinition = "decimal(20,2) comment '充值金额'")
    private BigDecimal amount;

    /**
     * 银行手续费
     */
    @Column(name = "BANK_FEE", columnDefinition = "decimal(20,2) comment '银行手续费'")
    private BigDecimal bankFee;

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
     * 充值单支付状态
     */
    @Column(name = "PAY_STATUS", nullable = false, updatable = false, columnDefinition = "smallint comment '充值单支付状态'")
    @Convert(converter = PayStatus.EnumConvert.class)
    private PayStatus payStatus;

    /**
     * 充值单完成状态
     */
    @Column(name = "COMPLETE_STATUS", nullable = false, updatable = false, columnDefinition = "smallint comment '充值单完成状态'")
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
     * 充值单成功时间
     */
    @Column(name = "FINISH_TIME", columnDefinition = "datetime comment '订单成功时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime finishTime;

    /**
     * 自动关闭充值单时间
     */
    @Column(name = "AUTO_CLOSE_TIME", columnDefinition = "datetime comment '自动关闭订单时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime autoCloseTime;

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
     * 更新支付单信息
     *
     * @param vo 支付单信息
     */
    public void updatePayment(PaymentVo vo) {
        paidAmount = vo.getPaidAmount();
        payTime = vo.getPayTime();
        payId = vo.getId();
        platformPayId = vo.getPlatformPayId();
        prePlatformPayId = vo.getPrePlatformPayId();
    }

    /**
     * 更新支付单支付结果回调信息
     *
     * @param request 支付单支付结果回调信息
     */
    public void updatePayment(PaymentPaidRequest request) {
        paidAmount = request.getPaidAmount();
        payId = request.getPaymentId();
        payTime = request.getPayTime();
        platformPayId = request.getPlatformPayId();
        prePlatformPayId = request.getPrePlatformPayId();
        payStatus = request.isSuccessful() ? PayStatus.PAID : PayStatus.FAILED;
        if (request.isSuccessful()) {
            completeStatus = CompleteStatus.DONE;
        }
    }


    /**
     * 创建充值单
     *
     * @param request 充值请求
     * @return 充值单
     */
    public static Deposit from(DepositCreateRequest request) {
        Deposit deposit = new Deposit();
        deposit.setAmount(request.getAmount());
        deposit.setAutoCloseTime(LocalDateTime.now().plusSeconds(AcctConsts.DEPOSIT_AUTO_CLOSE_TIME_IN_SECONDS));
        deposit.setBankFee(BigDecimal.ZERO);
        deposit.setCompleteStatus(CompleteStatus.DOING);
        deposit.setId(IdFactory.createUserId(AcctConsts.ID_PREFIX_DEPOSIT));
        deposit.setPayAmount(request.getAmount());
        deposit.setPayStatus(PayStatus.UNPAID);
        deposit.setType(DepositType.fromValue(request.getDepositType()));
        return deposit;
    }
}
