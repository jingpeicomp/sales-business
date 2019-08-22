package com.jingxiang.business.user.acct.adapter.wechat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 微信支付通知请求
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxpayNotifyRequest implements Serializable {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 支付单ID
     */
    private String paymentId;

    /**
     * 支付金额，精确到分
     */
    private String payAmount;

    /**
     * 微信支付平台支付订单ID
     */
    private String transactionId;

    /**
     * 失败原因
     */
    private String errorMessage;

    /**
     * 是否成功
     *
     * @return boolean
     */
    public boolean check() {
        return StringUtils.isBlank(errorMessage);
    }
}
