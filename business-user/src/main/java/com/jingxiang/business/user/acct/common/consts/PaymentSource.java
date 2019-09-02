package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 支付单来源
 * Created by liuzhaoming on 2019/8/25.
 */
public enum PaymentSource {
    /**
     * 订单付款
     */
    ORDER_PAY(1, "订单付款"),

    /**
     * 服务费充值
     */
    SF_DEPOSIT(2, "服务费充值"),

    /**
     * 提现
     */
    WITHDRAWAL(3, "提现"),

    /**
     * 服务费提现
     */
    SF_WITHDRAWAL(4, "服务费提现");


    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    PaymentSource(int value, String display) {
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
    public static PaymentSource fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的支付单来源类型值:" + value));
    }

    public static PaymentSource fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的支付单来源类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<PaymentSource, Integer> {
        @Override
        public Integer convertToDatabaseColumn(PaymentSource attribute) {
            return attribute.getValue();
        }

        @Override
        public PaymentSource convertToEntityAttribute(Integer dbData) {
            return PaymentSource.fromValue(dbData);
        }
    }
}
