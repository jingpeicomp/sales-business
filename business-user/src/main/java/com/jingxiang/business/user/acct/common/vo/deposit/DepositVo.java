package com.jingxiang.business.user.acct.common.vo.deposit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.DepositType;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值单值对象
 * Created by liuzhaoming on 2019/9/2.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DepositVo implements Serializable {
    /**
     * 充值单编号
     */
    private String id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 充值单类型
     */
    private DepositType type;

    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 银行手续费
     */
    private BigDecimal bankFee;

    /**
     * 应付金额
     * payAmount = amount + bankFee
     */
    private BigDecimal payAmount;

    /**
     * 实付金额
     */
    private BigDecimal paidAmount;

    /**
     * 充值单支付状态
     */
    private PayStatus payStatus;

    /**
     * 充值单完成状态
     */
    private CompleteStatus completeStatus;

    /**
     * 创建时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 充值单成功时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime finishTime;

    /**
     * 自动关闭充值单时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime autoCloseTime;

    /**
     * 支付类型，微信支付:1;支付宝:2
     */
    @Builder.Default
    private PayType payType = PayType.WEIXIN;

    /**
     * 支付单号
     */
    private String payId;

    /**
     * 支付时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    private String prePlatformPayId;
}
