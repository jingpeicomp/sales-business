package com.jingxiang.business.user.acct.common.vo.withdrawal;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.PayStatus;
import com.jingxiang.business.user.acct.common.consts.WithdrawalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现单值对象
 * Created by liuzhaoming on 2019/8/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawalVo implements Serializable {
    /**
     * 提现单编号
     */
    private String id;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 提现单类型
     */
    private WithdrawalType type;

    /**
     * 提现金额
     */
    private BigDecimal amount;

    /**
     * 银行手续费
     */
    @Builder.Default
    private BigDecimal bankFee = BigDecimal.ZERO;

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
     * 提现单支付状态
     */
    private PayStatus payStatus;

    /**
     * 提现单完成状态
     */
    private CompleteStatus completeStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 提现单成功时间
     */
    private LocalDateTime finishTime;

    /**
     * 支付类型，微信支付:1;支付宝:2
     */
    @Builder.Default
    private PayType payType = PayType.WEIXIN;
}
