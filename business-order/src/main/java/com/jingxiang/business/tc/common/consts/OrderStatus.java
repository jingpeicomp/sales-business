package com.jingxiang.business.tc.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单状态，由多个子状态合并而成
 * Created by liuzhaoming on 2019/8/3.
 */
public enum OrderStatus {

    /**
     * 未创建
     */
    UNCREATED(0, "未创建"),

    /**
     * 待支付
     */
    WAIT_PAY(1, "待支付"),

    /**
     * 待发货
     */
    WAIT_SHIP(2, "待发货"),

    /**
     * 待收货
     */
    WAIT_RECEIVE(3, "待收货"),

    /**
     * 确认收货
     */
    RECEIVED(4, "确认收货");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    OrderStatus(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        return display;
    }

    @JsonCreator
    public static OrderStatus fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单状态类型值:" + value));
    }

    public static OrderStatus fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单状态类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<OrderStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(OrderStatus attribute) {
            return attribute.getValue();
        }

        @Override
        public OrderStatus convertToEntityAttribute(Integer dbData) {
            return OrderStatus.fromValue(dbData);
        }
    }
}
