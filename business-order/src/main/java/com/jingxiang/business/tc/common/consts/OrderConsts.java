package com.jingxiang.business.tc.common.consts;

/**
 * 订单模块常量
 * Created by liuzhaoming on 2019/8/8.
 */
public interface OrderConsts {
    /**
     * 订单ID前缀
     */
    String ID_PREFIX_ORDER = "T";

    /**
     * 订单商品ID前缀
     */
    String ID_PREFIX_ORDER_PRODUCT = "TP";

    /**
     * 订单自动关闭时间(秒)
     */
    int ORDER_AUTO_CLOSE_TIME_IN_SECONDS = 15 * 60;

    /**
     * 订单自动确认收货时间(秒)
     */
    int ORDER_AUTO_CONFIRM_TIME_IN_SECONDS = 8 * 3600;
}
