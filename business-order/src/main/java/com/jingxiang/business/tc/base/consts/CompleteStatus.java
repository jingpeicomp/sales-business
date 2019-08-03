package com.jingxiang.business.tc.base.consts;

import java.util.stream.Stream;

/**
 * 订单完成状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum CompleteStatus {
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

    CompleteStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static CompleteStatus create(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单完成状态类型:" + value));
    }
}
