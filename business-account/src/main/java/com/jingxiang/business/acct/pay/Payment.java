package com.jingxiang.business.acct.pay;

import com.jingxiang.business.acct.common.consts.AccountConsts;
import com.jingxiang.business.acct.common.vo.address.PaymentCreateRequest;
import com.jingxiang.business.consts.PayStatus;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.id.IdFactory;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单
 * Created by liuzhaoming on 2019/8/8.
 */
@Entity
@Table(name = "T_BIZ_ACCT_PAYMENT")
@Data
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
     * 订单ID
     */
    @Column(name = "ORDER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '订单ID'")
    private String orderId;

    /**
     * 付款者
     */
    @Column(name = "BUYER", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '买家编号'")
    private String buyer;

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
     * 修改时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "PAY_TIME", columnDefinition = "datetime comment '支付时间'")
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
     * 支付单支付状态
     */
    @Column(name = "PAY_STATUS", nullable = false, columnDefinition = "smallint comment '支付状态'")
    @Convert(converter = PayStatus.EnumConvert.class)
    private PayStatus payStatus = PayStatus.UNPAID;

    /**
     * 支付单标题，会显示在支付平台
     */
    @Column(name = "TITLE", columnDefinition = "varchar(64) comment '支付单标题'")
    private String title;

    /**
     * 支付单描述
     */
    @Column(name = "DESCRIPTION", columnDefinition = "varchar(100) comment '支付单描述'")
    private String description;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 根据支付单创建请求创建支付单
     *
     * @param request 支付单创建请求
     * @return 支付单
     */
    public static Payment from(PaymentCreateRequest request) {
        Payment payment = new Payment();
        payment.setId(IdFactory.createAcctId(AccountConsts.ID_PREFIX_PAYMENT));
        payment.setBuyer(request.getBuyer());
        payment.setShopId(request.getShopId());
        payment.setPayType(request.getPayType());
        payment.setPayAmount(request.getPayAmount());
        payment.setOrderId(request.getOrderId());
        payment.setTitle(request.getTitle());
        payment.setDescription(request.getDescription());
        return payment;
    }
}
