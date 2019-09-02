package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 提现类型
 * Created by liuzhaoming on 2019/8/26.
 */
public enum WithdrawalType {
    /**
     * 账户余额提现
     */
    BALANCE(1, "账户余额提现"),

    /**
     * 服务费余额提现
     */
    SERVICE_FEE(2, "服务费余额提现");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    WithdrawalType(int value, String display) {
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
    public static WithdrawalType fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的提现类型值:" + value));
    }

    public static WithdrawalType fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的提现类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<WithdrawalType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(WithdrawalType attribute) {
            return attribute.getValue();
        }

        @Override
        public WithdrawalType convertToEntityAttribute(Integer dbData) {
            return WithdrawalType.fromValue(dbData);
        }
    }
}
