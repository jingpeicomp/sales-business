package com.jingxiang.business.tc.common.consts;

/**
 * 订单状态机事件名称常量
 * Created by liuzhaoming on 2019/8/6.
 */
public interface OrderFsmEventNames {

    /**
     * 下单
     */
    String CREATE = "下单";

    /**
     * 支付
     */
    String PAY = "支付";

    /**
     * 发货
     */
    String DELIVER = "发货";

    /**
     * 确认收货
     */
    String CONFIRM = "确认收货";

    /**
     * 关闭订单
     */
    String CLOSE = "关闭订单";
}
