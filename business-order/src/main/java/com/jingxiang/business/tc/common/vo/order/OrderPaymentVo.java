package com.jingxiang.business.tc.common.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jingxiang.business.consts.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单支付信息值对象
 *
 * @see com.jingxiang.business.tc.order.OrderPayment
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentVo implements Serializable {
    /**
     * 支付类型，微信支付:1;支付宝:2
     */
    private PayType payType = PayType.WEIXIN;

    /**
     * 支付单号
     */
    private String payId;

    /**
     * 支付时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    private String prePlatformPayId;
}
