package com.jingxiang.business.user.acct.common.vo.withdrawal;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现单创建请求
 * Created by liuzhaoming on 2019/8/26.
 */
@Data
public class WithdrawalCreateRequest implements Serializable {
    /**
     * 用户ID，无需前端传入
     */
    private String userId;

    /**
     * 提现金额
     */
    private BigDecimal amount;

    /**
     * 提现类型
     *
     * @see com.jingxiang.business.user.acct.common.consts.WithdrawalType
     */
    private int withdrawalType;
}
