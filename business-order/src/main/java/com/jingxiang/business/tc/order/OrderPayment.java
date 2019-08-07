package com.jingxiang.business.tc.order;

import com.jingxiang.business.tc.common.consts.PayType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单支付信息
 * Created by liuzhaoming on 2019/8/7.
 */
@Data
@Embeddable
public class OrderPayment implements Serializable {
    /**
     * 支付类型，微信支付:0;支付宝:1
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
    @Column(name = "TRADE_NO")
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    @Column(name = "PRE_PAY_ID")
    private String prePlatformPayId;
}
