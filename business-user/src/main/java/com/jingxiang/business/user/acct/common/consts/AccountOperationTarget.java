package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 账户操作目标
 * Created by liuzhaoming on 2019/8/25.
 */
public enum AccountOperationTarget {
    /**
     * 账户余额
     */
    BALANCE(1, "账户余额"),

    /**
     * 服务费
     */
    SERVICE_FEE(2, "服务费"),

    /**
     * 银行手续费
     */
    BANK_FEE(3, "银行手续费");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    AccountOperationTarget(int value, String display) {
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

    public static AccountOperationTarget fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户操作目标类型值:" + value));
    }

    public static AccountOperationTarget fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户操作目标类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<AccountOperationTarget, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AccountOperationTarget attribute) {
            return attribute.getValue();
        }

        @Override
        public AccountOperationTarget convertToEntityAttribute(Integer dbData) {
            return AccountOperationTarget.fromValue(dbData);
        }
    }
}
