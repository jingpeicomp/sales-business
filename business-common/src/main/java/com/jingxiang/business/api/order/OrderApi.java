package com.jingxiang.business.api.order;

/**
 * 订单API
 * Created by liuzhaoming on 2019/8/22.
 */
public interface OrderApi {
    /**
     * 订单支付结果回调
     *
     * @param request 订单支付结果回调
     */
    void paid(OrderPaidRequest request);
}
