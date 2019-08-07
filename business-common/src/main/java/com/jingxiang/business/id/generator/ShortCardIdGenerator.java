package com.jingxiang.business.id.generator;

import com.jingxiang.business.id.generator.IdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 13位数字ID生成器，一共43 bit
 * 卡号固定为13位，43bit。
 * <p>
 * +=======================================================
 * | 3bit机器编号 | 29bit时间戳 |    11bit序号               |
 * +=======================================================
 * <p>
 * 29 bit的秒时间戳支持17年
 * 11 bit序号支持2048个序号（起始序号是20以内的随机数）
 * 3 bit机器编号支持7台负载（负载编号从1-7）
 * 即ID生成最大支持7台负载；每台负载每秒钟可以生成最少2028，最多2048个ID。
 * Created by liuzhaoming on 2019/8/7.
 */
@Slf4j
public class ShortCardIdGenerator implements IdGenerator {

    private final Random random = new Random();

    /**
     * 时间bit数，时间的单位为秒，29 bit位时间可以表示17年
     */
    private int timeBits = 29;

    /**
     * 机器编码bit数
     */
    private int machineBits = 3;

    /**
     * 每秒序列bit数
     */
    private int sequenceBits = 11;


    /**
     * 上一次时间戳
     */
    private long lastStamp = -1L;

    /**
     * 序列
     */
    private long sequence = randomSequence();

    /**
     * 机器编号
     */
    private long machineId = 1L;

    /**
     * 时间左移bit数
     */
    private int timeOffset = 0;

    /**
     * 机器编码左移bit数
     */
    private int machineOffset = 0;

    /**
     * 最大序列号
     */
    private long maxSequence = 0L;

    /**
     * 开始时间，默认为2018-01-01
     */
    private LocalDateTime startDateTime = LocalDateTime.parse("2018-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


    /**
     * 起始时间戳
     */
    private long startTimeStamp = 0L;

    public ShortCardIdGenerator() {
        this(1);
    }

    public ShortCardIdGenerator(int machineId) {
        int maxMachineId = ~(-1 << machineBits);
        if (machineId > maxMachineId || machineId < 1) {
            throw new IllegalArgumentException("Machine id should be between 1 and " + maxMachineId);
        }

        this.machineId = machineId;
        init();
    }

    /**
     * 根据给定的系统编号生成卡号
     *
     * @return 13位卡号
     */
    @Override
    public synchronized long generate() {
        long curStamp = getNewStamp();
        if (curStamp < lastStamp) {
            throw new IllegalArgumentException("Clock moved backwards. Refusing to generate id");
        }

        if (curStamp == lastStamp) {
            sequence = maxSequence & (sequence + 1);
            if (sequence == 0L) {
                curStamp = getNextSecond();
            }
        } else {
            sequence = randomSequence();
        }
        lastStamp = curStamp;
        return machineId << machineOffset
                | (curStamp - startTimeStamp) << timeOffset
                | sequence;
    }

    /**
     * 解析卡号
     *
     * @param id 卡号
     * @return 解析结果依次是时间、机器编码、序列号
     */
    @Override
    public Object[] parse(long id) {
        String bitString = Long.toBinaryString(id);
        int bitLength = bitString.length();
        Long timestamp = Long.parseLong(bitString.substring(bitLength - timeOffset - timeBits, bitLength - timeOffset),
                2);
        Integer machineId = Integer.parseInt(bitString.substring(0, bitLength - machineOffset), 2);
        Integer sequence = Integer.parseInt(bitString.substring(bitLength - sequenceBits, bitLength), 2);
        System.out.println(timestamp);
        return new Object[]{toDateTime(timestamp), machineId, sequence};
    }

    /**
     * 数据初始化
     */
    private void init() {
        timeOffset = sequenceBits;
        machineOffset = timeOffset + timeBits;
        maxSequence = ~(-1L << sequenceBits);
        startTimeStamp = getTimeStamp();
    }

    /**
     * 获取起始时间戳
     *
     * @return 时间戳
     */
    private long getTimeStamp() {
        ZoneId zoneId = ZoneId.systemDefault();
        return startDateTime.atZone(zoneId).toInstant().getEpochSecond();
    }

    /**
     * 将时间戳（秒）转化为时间
     *
     * @param secondStamp 秒时间戳
     * @return 时间
     */
    private LocalDateTime toDateTime(Long secondStamp) {
        return startDateTime.plusSeconds(secondStamp);
    }

    /**
     * 获取当前时间戳 单位秒
     *
     * @return 时间戳（秒）
     */
    private long getNewStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取下一秒钟
     *
     * @return 时间戳（秒）
     */
    private long getNextSecond() {
        long second = getNewStamp();
        while (second <= lastStamp) {
            second = getNewStamp();
        }
        return second;
    }

    /**
     * 生成一个随机数作为sequence的起始数
     *
     * @return sequence起始数
     */
    private long randomSequence() {
        return random.nextInt(20);
    }
}
