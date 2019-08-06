package com.jingxiang.business.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 通用工具类
 * Created by liuzhaoming on 2019/8/2.
 */
@Slf4j
public final class CommonUtils {
    /**
     * 读取有限状态机规则配置文件（JSON格式）并转换成Fsm对象
     */
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        JsonFactory jf = new JsonFactory();
        // 支持在JSON配置文件中写注释，格式如下
        // -     /* 未支付已发货场景 */
        // -    // 申请退款订单已完成
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
        //允许未识别属性
        jf.enable(JsonParser.Feature.IGNORE_UNDEFINED);
        OBJECT_MAPPER = new ObjectMapper(jf);
    }

    private CommonUtils() {
    }

    /**
     * 用“;”分割字符串
     *
     * @param str 待分割的字符串
     * @return 分割后的字符串列表
     */
    public static List<String> splitStr(String str) {
        return splitStr(str, ";");
    }

    /**
     * 用指定的分隔符分割字符串
     *
     * @param str  待分割的字符串
     * @param sign 分隔符
     * @return 分割后的字符串列表
     */
    public static List<String> splitStr(String str, String sign) {
        if (StringUtils.isBlank(str)) {
            return Collections.emptyList();
        }

        return Arrays.asList(str.split(sign));
    }

    /**
     * 将Java对象转化为JSON字符串
     *
     * @param obj obj对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Fail convert object to json string {}", obj, e);
        }

        return "";
    }

    /**
     * 将Json字符串转化为Java对象
     *
     * @param jsonStr json字符串
     * @param tClass  Java对象类
     * @param <T>     泛型
     * @return Java对象
     */
    public static <T> T fromJson(String jsonStr, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, tClass);
        } catch (IOException e) {
            log.error("Fail convert json string to object {} , {}", jsonStr, tClass, e);
        }

        return null;
    }

    /**
     * 将Json字符串转化为Java对象
     *
     * @param jsonStr   json字符串
     * @param reference Java对象类型
     * @param <T>       泛型
     * @return Java对象
     */
    public static <T> T fromJson(String jsonStr, TypeReference<T> reference) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, reference);
        } catch (IOException e) {
            log.error("Fail convert json string to object {} , {}", jsonStr, reference, e);
        }

        return null;
    }

    /**
     * 将指定文件的内容（Json字符串）转化为对应的Java对象
     *
     * @param filePath json文件路径
     * @param tClass   Java对象类
     * @param <T>      泛型
     * @return Java对象
     */
    public static <T> T fromJsonFile(String filePath, Class<T> tClass) {
        try (
                InputStream is = CommonUtils.class.getClassLoader().getResourceAsStream(filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            return OBJECT_MAPPER.readValue(br, tClass);
        } catch (IOException e) {
            log.error("Fail convert json file to object {} , {}", filePath, tClass, e);
        }

        return null;
    }
}
