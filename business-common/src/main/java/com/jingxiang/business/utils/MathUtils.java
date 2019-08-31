package com.jingxiang.business.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 数学运算
 * Created by liuzhaoming on 2019/8/30.
 */
@Slf4j
public final class MathUtils {

    private MathUtils() {
    }


    /**
     * 获取格式化的价格，精确到小数点后两位，单位为元
     *
     * @param fee 价格
     * @return 价格字符串
     */
    public static String formatDownFee(BigDecimal fee) {
        if (null == fee) {
            return "0.00";
        }

        return fee.setScale(2, BigDecimal.ROUND_DOWN).toString();
    }

    /**
     * 将分转化为BigDecimal
     *
     * @param feeString 整数字符串，单位为分
     * @return BigDecimal
     */
    public static BigDecimal fromDownFee(String feeString) {
        if (StringUtils.isBlank(feeString)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(feeString).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 两个数相乘
     *
     * @param base  被乘数
     * @param other 乘数
     * @return BigDecimal
     */
    public static BigDecimal multiply(BigDecimal base, BigDecimal other) {
        return base.multiply(other).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两个数相减
     *
     * @param base  被乘数
     * @param other 乘数
     * @return BigDecimal
     */
    public static BigDecimal subtract(BigDecimal base, BigDecimal other) {
        return base.subtract(other).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两个数相加
     *
     * @param base  被乘数
     * @param other 乘数
     * @return BigDecimal
     */
    public static BigDecimal add(BigDecimal base, BigDecimal other) {
        return base.add(other).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
