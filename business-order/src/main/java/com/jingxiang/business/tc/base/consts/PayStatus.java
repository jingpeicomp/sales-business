package com.jingxiang.business.tc.base.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单支付状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum PayStatus {
    /**
     * 未支付
     */
    UNPAID(0, "未支付"),

    /**
     * 支付中
     */
    PAYING(1, "支付中"),

    /**
     * 支付失败
     */
    FAILED(2, "支付失败"),

    /**
     * 已支付
     */
    PAID(3, "已支付");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    PayStatus(int value, String display) {
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

    public static PayStatus fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单支付状态类型值:" + value));
    }

    public static PayStatus fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单支付状态类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<PayStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(PayStatus attribute) {
            return attribute.getValue();
        }

        @Override
        public PayStatus convertToEntityAttribute(Integer dbData) {
            return PayStatus.fromValue(dbData);
        }
    }
}
