package com.jingxiang.business.api.payment;

import com.jingxiang.business.consts.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单支付结果请求
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPaidRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 来源ID，订单支付结果是订单ID，充值单支付结果是充值单ID
     */
    private String sourceId;

    /**
     * 是否成功
     */
    private boolean successful;

    /**
     * 结果信息，只有失败时才有值
     */
    private String message;

    /**
     * 修改时间
     */
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    private String prePlatformPayId;

    /**
     * 支付单ID
     */
    private String paymentId;

    /**
     * 操作对象
     */
    private Role role;

    /**
     * 支付金额
     */
    private BigDecimal paidAmount;
}
