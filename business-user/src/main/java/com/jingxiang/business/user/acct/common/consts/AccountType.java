package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 账户类型
 * Created by liuzhaoming on 2019/8/25.
 */
public enum AccountType {
    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 卖家
     */
    SELLER(2, "卖家"),

    /**
     * 合伙人
     */
    PARTNER(3, "合伙人"),

    /**
     * 系统账户，虚账户
     */
    SYSTEM(4, "系统账户");

    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    AccountType(int value, String display) {
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
    public static AccountType fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户类型值:" + value));
    }

    public static AccountType fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<AccountType, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AccountType attribute) {
            return attribute.getValue();
        }

        @Override
        public AccountType convertToEntityAttribute(Integer dbData) {
            return AccountType.fromValue(dbData);
        }
    }
}
