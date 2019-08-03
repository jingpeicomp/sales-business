package com.jingxiang.business.tc.base.consts;

import java.util.stream.Stream;

/**
 * 订单支付状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum PayStatus {
    /**
     * 未支付
     */
    UNPAID(0),

    /**
     * 支付中
     */
    PAYING(1),

    /**
     * 支付失败
     */
    FAILED(2),

    /**
     * 已支付
     */
    PAID(3);

    private final int value;

    PayStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PayStatus create(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单支付状态类型:" + value));
    }
}
