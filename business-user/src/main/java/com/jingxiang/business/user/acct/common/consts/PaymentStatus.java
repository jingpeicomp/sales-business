package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 支付单状态
 * Created by liuzhaoming on 2019/8/3.
 */
public enum PaymentStatus {
    /**
     * 未支付
     */
    UNPAID(1, "未支付"),

    /**
     * 支付中
     */
    PAYING(2, "支付中"),

    /**
     * 支付失败
     */
    FAILED(3, "支付失败"),

    /**
     * 已支付
     */
    PAID(4, "已支付"),

    /**
     * 已作废
     */
    CANCELED(100, "已作废");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    PaymentStatus(int value, String display) {
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

    public static PaymentStatus fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的支付单状态类型值:" + value));
    }

    public static PaymentStatus fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的支付单状态类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<PaymentStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(PaymentStatus attribute) {
            return attribute.getValue();
        }

        @Override
        public PaymentStatus convertToEntityAttribute(Integer dbData) {
            return PaymentStatus.fromValue(dbData);
        }
    }
}
