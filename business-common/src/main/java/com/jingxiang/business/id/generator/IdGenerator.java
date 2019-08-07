package com.jingxiang.business.id.generator;

/**
 * ID 生成器
 * Created by liuzhaoming on 2019/8/7.
 */
public interface IdGenerator {
    /**
     * 根据给定的系统编号生成卡号
     *
     * @return 13位卡号
     */
    long generate();

    /**
     * 解析卡号
     *
     * @param id 卡号
     * @return 解析结果依次是时间、机器编码、序列号
     */
    Object[] parse(long id);
}
