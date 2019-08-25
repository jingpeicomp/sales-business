package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 账户操作
 * Created by liuzhaoming on 2019/8/25.
 */
public enum AccountOperation {
    /**
     * 支付
     */
    PAY(1, "支付"),

    /**
     * 收入
     */
    RECEIPT(2, "收入"),

    /**
     * 充值
     */
    DEPOSIT(3, "充值"),

    /**
     * 提现
     */
    WITHDRAWAL(4, "提现");


    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    AccountOperation(int value, String display) {
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

    public static AccountOperation fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户操作类型值:" + value));
    }

    public static AccountOperation fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户操作显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<AccountOperation, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AccountOperation attribute) {
            return attribute.getValue();
        }

        @Override
        public AccountOperation convertToEntityAttribute(Integer dbData) {
            return AccountOperation.fromValue(dbData);
        }
    }
}
