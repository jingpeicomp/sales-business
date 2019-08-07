package com.jingxiang.business.tc.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单发货状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum ShipStatus {
    /**
     * 未发货
     */
    UNSHIPPED(0, "未发货"),

    /**
     * 已发货
     */
    SHIPPED(1, "已发货"),

    /**
     * 已收货
     */
    RECEIVED(2, "已收货");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    ShipStatus(int value, String display) {
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

    public static ShipStatus fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单发货状态类型值:" + value));
    }

    public static ShipStatus fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单发货状态类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<ShipStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(ShipStatus attribute) {
            return attribute.getValue();
        }

        @Override
        public ShipStatus convertToEntityAttribute(Integer dbData) {
            return ShipStatus.fromValue(dbData);
        }
    }
}
