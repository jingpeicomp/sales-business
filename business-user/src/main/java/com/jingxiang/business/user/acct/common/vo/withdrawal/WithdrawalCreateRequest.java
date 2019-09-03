package com.jingxiang.business.user.acct.common.vo.withdrawal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现单创建请求
 * Created by liuzhaoming on 2019/8/26.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WithdrawalCreateRequest implements Serializable {
    /**
     * 用户ID，无需前端传入
     */
    private String userId;

    /**
     * 提现金额
     */
    @DecimalMin(value = "0", inclusive = false, message = "提现金额必须大于0")
    private BigDecimal amount;

    /**
     * 提现类型
     *
     * @see com.jingxiang.business.user.acct.common.consts.WithdrawalType
     */
    @DecimalMin(value = "1", message = "提现类型不正确")
    private int withdrawalType;
}
