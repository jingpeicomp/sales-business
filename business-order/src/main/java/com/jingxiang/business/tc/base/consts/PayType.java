package com.jingxiang.business.tc.base.consts;

import java.util.stream.Stream;

/**
 * 支付类型
 * Created by liuzhaoming on 2019/8/3.
 */
public enum PayType {
    /**
     * 微信支付
     */
    WEIXIN(0),

    /**
     * 支付宝支付
     */
    ALIPAY(1);

    int value;

    PayType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PayType create(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的支付类型:" + value));
    }
}
