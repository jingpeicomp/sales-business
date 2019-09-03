package com.jingxiang.business.user.acct.common.vo.withdrawal;

import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 提现单查询条件
 * Created by liuzhaoming on 2019/9/2.
 */
@Data
public class WithdrawalQueryCondition implements Serializable {
    /**
     * 用户ID，无需前端传入
     */
    private String userId;

    /**
     * 页数，从0开始
     */
    private int page = 0;

    /**
     * 每页条数,
     */
    private int size = 20;

    /**
     * 提现单状态
     *
     * @see CompleteStatus
     */
    private Integer completeStatus;
}
