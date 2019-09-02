package com.jingxiang.business.user.acct.common.vo.deposit;

import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * 充值单查询条件
 * Created by liuzhaoming on 2019/9/2.
 */
@Data
public class DepositQueryCondition implements Serializable {
    /**
     * 用户ID
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
     * 充值单状态
     *
     * @see com.jingxiang.business.user.acct.common.consts.CompleteStatus
     */
    private int completeStatus = CompleteStatus.DONE.getValue();
}
