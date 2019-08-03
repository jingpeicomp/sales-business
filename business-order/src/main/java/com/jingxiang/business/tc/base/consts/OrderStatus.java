package com.jingxiang.business.tc.base.consts;

/**
 * 订单状态，由多个状态合并而成
 * Created by liuzhaoming on 2019/8/3.
 */
public enum OrderStatus {

    /**
     * 创建成功
     */
    CREATED(0),

    /**
     * 支付成功
     */
    PAID(1),

    /**
     * 已发货
     */
    SHIPPED(2),

    /**
     * 确认收货
     */
    RECEIVED(3);

    /**
     * 枚举类型对应的数值
     */
    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
