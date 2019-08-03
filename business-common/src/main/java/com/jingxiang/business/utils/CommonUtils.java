package com.jingxiang.business.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 通用工具类
 * Created by liuzhaoming on 2019/8/2.
 */
public final class CommonUtils {
    private CommonUtils() {
    }

    /**
     * 用“;”分割字符串
     *
     * @param str 待分割的字符串
     * @return 分割后的字符串列表
     */
    public static List<String> splitStr(String str) {
        if (StringUtils.isEmpty(str)) {
            return Collections.emptyList();
        }

        return Arrays.asList(str.split(";"));
    }
}
