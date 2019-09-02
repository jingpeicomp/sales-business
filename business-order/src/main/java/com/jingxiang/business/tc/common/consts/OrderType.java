package com.jingxiang.business.tc.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单类型
 * Created by liuzhaoming on 2019/8/6.
 */
public enum OrderType {

    NORMAL(1, "普通订单");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    OrderType(int value, String display) {
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

    @JsonCreator
    public static OrderType fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单类型值:" + value));
    }

    public static OrderType fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<OrderType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(OrderType attribute) {
            return attribute.getValue();
        }

        @Override
        public OrderType convertToEntityAttribute(Integer dbData) {
            return OrderType.fromValue(dbData);
        }
    }
}
