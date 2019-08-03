package com.jingxiang.business.tc.base.consts;

import java.util.stream.Stream;

/**
 * 订单发货状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum ShipStatus {
    /**
     * 未发货
     */
    UNSHIPPED(0),

    /**
     * 已发货
     */
    SHIPPED(1),

    /**
     * 已收货
     */
    RECEIVED(2);

    private final int value;

    ShipStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ShipStatus create(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单发货状态类型:" + value));
    }
}
