package com.jingxiang.business.tc.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 支付类型
 * Created by liuzhaoming on 2019/8/3.
 */
public enum PayType {
    /**
     * 微信支付
     */
    WEIXIN(1, "微信支付"),

    /**
     * 支付宝支付
     */
    ALIPAY(2, "支付宝支付");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    PayType(int value, String display) {
        this.value = value;
        this.display = display;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    public static PayType fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单支付类型值:" + value));
    }

    public static PayType fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单支付类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<PayType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(PayType attribute) {
            return attribute.getValue();
        }

        @Override
        public PayType convertToEntityAttribute(Integer dbData) {
            return PayType.fromValue(dbData);
        }
    }
}
