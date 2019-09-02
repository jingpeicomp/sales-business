package com.jingxiang.business.user.acct.common.consts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.AttributeConverter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 账户资金流向 借、贷
 * Created by liuzhaoming on 2019/8/25.
 */
public enum FundDirection {
    /**
     * 借，表现为资金流入
     */
    DEBIT(1, "借"),

    /**
     * 贷，表现为资金流出
     */
    CREDIT(2, "贷");


    /**
     * 枚举对应的值，主要用于数据库和前端，提高效率
     */
    private final int value;

    /**
     * 枚举对应的值，方便理解和配置
     */
    private final String display;

    FundDirection(int value, String display) {
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
    public static FundDirection fromValue(int value) {
        return Stream.of(values())
                .filter(status -> status.value == value)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户资金流向类型值:" + value));
    }

    public static FundDirection fromDisplay(String display) {
        return Stream.of(values())
                .filter(status -> Objects.equals(status.display, display))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的账户资金流向显示名称:" + display));
    }

    public static class EnumConvert implements AttributeConverter<FundDirection, Integer> {
        @Override
        public Integer convertToDatabaseColumn(FundDirection attribute) {
            return attribute.getValue();
        }

        @Override
        public FundDirection convertToEntityAttribute(Integer dbData) {
            return FundDirection.fromValue(dbData);
        }
    }
}
