package com.jingxiang.business.api.order;

import com.jingxiang.business.api.payment.PaymentPaidRequest;

/**
 * 订单回调API
 * Created by liuzhaoming on 2019/8/22.
 */
public interface OrderCallbackApi {
    /**
     * 订单支付结果回调
     *
     * @param request 订单支付结果回调
     */
    void paid(PaymentPaidRequest request);
}
