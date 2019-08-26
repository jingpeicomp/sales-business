package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 充值类型
 * Created by liuzhaoming on 2019/8/26.
 */
public enum DepositType {
    /**
     * 账户余额充值
     */
    BALANCE(1, "账户余额充值"),

    /**
     * 服务费余额充值
     */
    SERVICE_FEE(2, "服务费余额充值");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    DepositType(int value, String display) {
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

    public static DepositType fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的充值类型值:" + value));
    }

    public static DepositType fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的充值类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<DepositType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(DepositType attribute) {
            return attribute.getValue();
        }

        @Override
        public DepositType convertToEntityAttribute(Integer dbData) {
            return DepositType.fromValue(dbData);
        }
    }
}
