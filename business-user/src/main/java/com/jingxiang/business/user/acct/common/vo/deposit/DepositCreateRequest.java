package com.jingxiang.business.user.acct.common.vo.deposit;

import com.jingxiang.business.user.acct.common.consts.DepositType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值单创建请求
 * Created by liuzhaoming on 2019/8/26.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DepositCreateRequest implements Serializable {
    /**
     * 用户ID，无需前端传入
     */
    private String userId;

    /**
     * 充值金额
     */
    @DecimalMin(value = "0", inclusive = false, message = "充值金额必须大于0")
    private BigDecimal amount;

    /**
     * 充值类型
     *
     * @see com.jingxiang.business.user.acct.common.consts.DepositType
     */
    @Builder.Default
    @DecimalMin(value = "1", message = "充值类型不正确")
    private int depositType = DepositType.SERVICE_FEE.getValue();
}
