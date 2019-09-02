package com.jingxiang.business.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 系统操作角色
 * <p>
 * Created by liuzhaoming on 2019/8/6.
 */
public enum Role {
    /**
     * 买家
     */
    BUYER(1, "买家"),

    /**
     * 卖家
     */
    SELLER(2, "卖家"),

    /**
     * 定时任务
     */
    TIMER(3, "定时任务"),

    /**
     * 账务
     */
    ACCT(4, "账务"),

    /**
     * 平台
     */
    SYSTEM(5, "平台"),

    /**
     * 合伙人
     */
    PARTNER(6, "合伙人");


    private int value;

    private String display;

    Role(int value, String display) {
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
    public static Role fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的角色类型值:" + value));
    }

    public static Role fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的角色类型显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<Role, Integer> {
        @Override
        public Integer convertToDatabaseColumn(Role attribute) {
            return attribute.getValue();
        }

        @Override
        public Role convertToEntityAttribute(Integer dbData) {
            return Role.fromValue(dbData);
        }
    }
}
