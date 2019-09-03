package com.jingxiang.business.user.acct.pay;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.user.acct.adapter.wechat.WxpayNotifyRequest;
import com.jingxiang.business.user.acct.common.consts.AcctConsts;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import com.jingxiang.business.user.acct.common.consts.PaymentStatus;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单
 * Created by liuzhaoming on 2019/8/8.
 */
@Entity
@Table(name = "T_BIZ_UC_PAYMENT", indexes = {@Index(columnList = "PAYER", name = "IDX_P_PAYER"),
        @Index(columnList = "PAY_TIME", name = "IDX_P_PAY_TIME"),
        @Index(columnList = "CREATE_TIME", name = "IDX_P_CREATE_TIME")})
@Data
@EntityListeners(AuditingEntityListener.class)
public class Payment implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '支付单编号'")
    private String id;

    /**
     * 店铺ID
     */
    @Column(name = "SHOP_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺编号'")
    private String shopId;

    /**
     * 订单ID， 如果是订单支付单，就是订单ID；如果是充值支付单，就是充值单ID
     */
    @Column(name = "SOURCE_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '请求源ID。如果是订单支付单，就是订单ID'")
    private String sourceId;

    /**
     * 付款者
     */
    @Column(name = "PAYER", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '付款者'")
    private String payer;

    /**
     * 收款者
     */
    @Column(name = "PAYEE", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '收款者'")
    private String payee;


    /**
     * 支付类型，微信支付:0;支付宝:1
     */
    @Column(name = "PAY_TYPE", nullable = false, columnDefinition = "smallint comment '支付类型，微信支付:1;支付宝:2'")
    @Convert(converter = PayType.EnumConvert.class)
    private PayType payType;

    /**
     * 应付金额
     */
    @Column(name = "PAY_AMOUNT", columnDefinition = "decimal(20,2) comment '应付金额'")
    private BigDecimal payAmount;

    /**
     * 实付金额
     */
    @Column(name = "PAID_AMOUNT", columnDefinition = "decimal(20,2) comment '实付金额'")
    private BigDecimal paidAmount;

    /**
     * 支付单来源
     */
    @Column(name = "SOURCE", nullable = false, updatable = false, columnDefinition = "smallint comment '支付单来源'")
    @Convert(converter = PaymentSource.EnumConvert.class)
    private PaymentSource source;

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
     * 支付时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "PAY_TIME", columnDefinition = "datetime comment '支付时间'")
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    @Column(name = "PLATFORM_PAY_ID", columnDefinition = "varchar(64) comment '支付平台的支付单号'")
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    @Column(name = "PRE_PLATFORM_PAY_ID", columnDefinition = "varchar(64) comment '支付网关预支付单ID'")
    private String prePlatformPayId;

    /**
     * 支付单状态
     */
    @Column(name = "STATUS", nullable = false, columnDefinition = "smallint comment '支付单状态'")
    @Convert(converter = PaymentStatus.EnumConvert.class)
    private PaymentStatus status = PaymentStatus.UNPAID;

    /**
     * 支付单标题，会显示在支付平台
     */
    @Column(name = "TITLE", columnDefinition = "varchar(64) comment '支付单标题'")
    private String title;

    /**
     * 支付单描述
     */
    @Column(name = "DESCRIPTION", columnDefinition = "varchar(200) comment '支付单描述'")
    private String description;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 更新微信支付成功回调信息
     *
     * @param request 微信支付回调信息
     */
    public void updateWxpaySuccessNotification(WxpayNotifyRequest request) {
        setPlatformPayId(request.getTransactionId());
        setStatus(PaymentStatus.PAID);
        setPaidAmount(payAmount);
        setPayTime(LocalDateTime.now());
    }

    /**
     * 更新微信支付失败回调信息，主要是金额不对
     *
     * @param request 微信支付回调信息
     */
    public void updateWxpayFailNotification(WxpayNotifyRequest request) {
        setPlatformPayId(request.getTransactionId());
        setStatus(PaymentStatus.FAILED);
        setPaidAmount(new BigDecimal(request.getPayAmount()));
        setPayTime(LocalDateTime.now());
    }

    /**
     * 取消支付单
     */
    public void cancel() {
        if (status == PaymentStatus.PAID) {
            throw new ServiceException("已经支付成功的支付单不允许取消");
        }

        if (status == PaymentStatus.PAYING) {
            throw new ServiceException("正在支付中的支付单不允许取消");
        }

        setStatus(PaymentStatus.CANCELED);
    }

    /**
     * 订单是否可以支付， 只有未支付或者支付失败的支付单才可以支付
     *
     * @return boolean 可以支付返回true，反之false
     */
    public boolean canPay() {
        return status == PaymentStatus.UNPAID || status == PaymentStatus.FAILED;
    }

    /**
     * 返回支付单值对象
     *
     * @return 支付单值对象
     */
    public PaymentVo toVo() {
        return PaymentVo.builder()
                .id(id)
                .shopId(shopId)
                .payer(payer)
                .payee(payee)
                .payType(payType)
                .payAmount(payAmount)
                .paidAmount(paidAmount)
                .sourceId(sourceId)
                .payTime(payTime)
                .status(status)
                .title(title)
                .description(description)
                .platformPayId(platformPayId)
                .prePlatformPayId(prePlatformPayId)
                .updateTime(updateTime)
                .createTime(createTime)
                .source(source)
                .build();
    }

    /**
     * 根据支付单创建请求创建支付单
     *
     * @param request 支付单创建请求
     * @return 支付单
     */
    public static Payment from(PaymentCreateRequest request) {
        Payment payment = new Payment();
        payment.setId(IdFactory.createUserId(AcctConsts.ID_PREFIX_PAYMENT));
        payment.setPayer(request.getPayer());
        payment.setPayee(request.getPayee());
        payment.setShopId(request.getShopId());
        payment.setPayType(request.getPayType());
        payment.setPayAmount(request.getPayAmount());
        payment.setSourceId(request.getSourceId());
        payment.setTitle(request.getTitle());
        payment.setDescription(request.getDescription());
        payment.setSource(request.getSource());
        return payment;
    }
}
