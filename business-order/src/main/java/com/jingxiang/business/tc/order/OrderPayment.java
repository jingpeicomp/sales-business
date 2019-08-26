package com.jingxiang.business.tc.order;

import com.jingxiang.business.api.payment.PaymentPaidRequest;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.tc.common.vo.order.OrderPaymentVo;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
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
     * 更新支付单信息
     *
     * @param paidRequest 已支付请求
     */
    public void updatePayment(PaymentPaidRequest paidRequest) {
        payId = paidRequest.getPaymentId();
        payTime = paidRequest.getPayTime();
        platformPayId = paidRequest.getPlatformPayId();
        prePlatformPayId = paidRequest.getPrePlatformPayId();
    }

    /**
     * 更新支付单信息
     *
     * @param vo 支付单信息
     */
    public void updatePayment(PaymentVo vo) {
        payId = vo.getId();
        payTime = vo.getPayTime();
        platformPayId = vo.getPlatformPayId();
        prePlatformPayId = vo.getPrePlatformPayId();
    }

    /**
     * 转化为值对象
     *
     * @return 值对象
     */
    public OrderPaymentVo toVo() {
        return OrderPaymentVo.builder()
                .payId(payId)
                .payTime(payTime)
                .payType(payType)
                .platformPayId(platformPayId)
                .prePlatformPayId(prePlatformPayId)
                .build();
    }
}
