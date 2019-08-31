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
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

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

    private static final String LOCALHOST = "127.0.0.1";

    private static final String ANY_HOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static final DateTimeFormatter STANDARD_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String LOCAL_IP;

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

    /**
     * 格式化时间
     *
     * @param time 时间
     * @return 时间格式化字符串
     */
    public static String formatTime(LocalDateTime time) {
        return STANDARD_DATETIME_FORMATTER.format(time);
    }

    /**
     * 格式化时间
     *
     * @param timestamp 时间戳
     * @return 时间格式化字符串
     */
    public static String formatTime(long timestamp) {
        LocalDateTime time = toLocalDateTime(timestamp);
        return STANDARD_DATETIME_FORMATTER.format(time);
    }

    /**
     * 将timestamp转化为LocalDateTime
     *
     * @param millTimestamp 毫秒偏移值
     * @return 将timestamp转化为LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long millTimestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millTimestamp), ZoneId.systemDefault());
    }

    /**
     * 按照指定的时间格式将字符串时间转化为LocalDateTime
     *
     * @param strTime       字符串时间
     * @param timeFormatter 时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String strTime, DateTimeFormatter timeFormatter) {
        if (null == strTime || null == timeFormatter) {
            return null;
        }

        try {
            return LocalDateTime.parse(strTime, timeFormatter);
        } catch (Exception e) {
            log.info("Parse local date time error strTime={}  , timeFormatter={}", strTime, timeFormatter);
            return null;
        }
    }

    /**
     * 安装标准时间格式解析时间 "2011-12-03 10:15:30"
     *
     * @param strTime 字符串时间
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String strTime) {
        return parseLocalDateTime(strTime, STANDARD_DATETIME_FORMATTER);
    }

    /**
     * 获取本地IP
     *
     * @return 本地IP
     */
    public static String getLocalIp() {
        if (StringUtils.isBlank(LOCAL_IP)) {
            synchronized (LOCALHOST) {
                if (StringUtils.isBlank(LOCAL_IP)) {
                    LOCAL_IP = calculateLocalIP();
                }
            }
        }
        return LOCAL_IP;
    }

    /**
     * 获取本地IP地址
     *
     * @return 本地IP地址
     */
    private static String calculateLocalIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (null != interfaces) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        try {
                            InetAddress address = addresses.nextElement();
                            if (isValidAddress(address)) {
                                return address.getHostAddress();
                            }
                        } catch (Exception e) {
                            return LOCALHOST;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return LOCALHOST;
        }

        return LOCALHOST;
    }

    /**
     * 是否是正确的IP地址
     *
     * @param address IP地址
     * @return IP是否合法，合法返回true，反之false
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && !ANY_HOST.equals(name)
                && !LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }
}
