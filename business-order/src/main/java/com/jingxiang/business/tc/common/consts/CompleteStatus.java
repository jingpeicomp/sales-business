package com.jingxiang.business.tc.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 订单完成状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum CompleteStatus {
    /**
     * 未创建
     */
    UNCREATED(1, "未创建"),

    /**
     * 正在进行
     */
    DOING(2, "正在进行"),

    /**
     * 已完成
     */
    DONE(3, "已完成"),

    /**
     * 已作废
     */
    CANCELED(4, "已作废");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，主要用于状态机，方便理解和配置
     */
    private final String display;

    CompleteStatus(int value, String display) {
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

    public static CompleteStatus fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单完成状态类型值:" + value));
    }

    public static CompleteStatus fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的订单完成状态类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<CompleteStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(CompleteStatus attribute) {
            return attribute.getValue();
        }

        @Override
        public CompleteStatus convertToEntityAttribute(Integer dbData) {
            return CompleteStatus.fromValue(dbData);
        }
    }
}
