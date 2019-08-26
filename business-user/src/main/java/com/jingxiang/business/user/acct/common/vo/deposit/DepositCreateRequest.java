package com.jingxiang.business.user.acct.common.vo.deposit;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值单创建请求
 * Created by liuzhaoming on 2019/8/26.
 */
@Data
public class DepositCreateRequest implements Serializable {
    /**
     * 用户ID，无需前端传入
     */
    private String userId;

    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 充值类型
     *
     * @see com.jingxiang.business.user.acct.common.consts.DepositType
     */
    private int depositType;
}
