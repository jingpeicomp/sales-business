package com.jingxiang.business.id;

import com.jingxiang.business.id.generator.IdGenerator;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * ID生成器对外接口
 * Created by liuzhaoming on 2019/8/7.
 */
public class IdFactory {
    static IdFactory INSTANCE;

    /**
     * 商品模块ID生成器
     */
    private IdGenerator productIdGenerator;

    /**
     * 订单模块ID生成器
     */
    private IdGenerator tcIdGenerator;

    /**
     * 账户ID生成器
     */
    private IdGenerator acctIdGenerator;

    IdFactory(IdGenerator productIdGenerator, IdGenerator tcIdGenerator, IdGenerator acctIdGenerator) {
        this.productIdGenerator = productIdGenerator;
        this.tcIdGenerator = tcIdGenerator;
        this.acctIdGenerator = acctIdGenerator;
    }

    /**
     * 生成商品模块字符串ID
     *
     * @param prefix ID前缀
     * @return 商品模块字符串ID
     */
    public static String createProductId(String prefix) {
        return prefix + INSTANCE.productIdGenerator.generate();
    }

    /**
     * 解析商品模块ID，
     *
     * @param id 商品模块ID
     * @return 解析结果依次是时间、机器编码、序列号
     */
    public static Object[] parseProductId(String id) {
        return INSTANCE.productIdGenerator.parse(toLongId(id));
    }

    /**
     * 生成订单模块字符串ID
     *
     * @param prefix ID前缀
     * @return 订单模块字符串ID
     */
    public static String createTcId(String prefix) {
        return prefix + INSTANCE.tcIdGenerator.generate();
    }

    /**
     * 解析订单模块ID，
     *
     * @param id 订单模块ID
     * @return 解析结果依次是时间、机器编码、序列号
     */
    public static Object[] parseTcId(String id) {
        return INSTANCE.productIdGenerator.parse(toLongId(id));
    }

    /**
     * 生成账户模块字符串ID
     *
     * @param prefix ID前缀
     * @return 账户模块字符串ID
     */
    public static String createAcctId(String prefix) {
        return prefix + INSTANCE.acctIdGenerator.generate();
    }

    /**
     * 解析账户模块ID，
     *
     * @param id 账户模块ID
     * @return 解析结果依次是时间、机器编码、序列号
     */
    public static Object[] parseAccttId(String id) {
        return INSTANCE.productIdGenerator.parse(toLongId(id));
    }

    /**
     * 将字符串开头的字符截去，获取数字;
     * "p1223"->"123"   "ab23333"->"23333"
     *
     * @param id 字符串ID
     * @return ID
     */
    private static Long toLongId(String id) {
        if (StringUtils.isBlank(id)) {
            return 0L;
        }

        StringBuilder sb = new StringBuilder();
        id.chars()
                .filter(curChar -> CharUtils.isAsciiNumeric((char) curChar))
                .forEach(curChar -> sb.append((char) curChar));
        return Long.valueOf(sb.toString());
    }
}
