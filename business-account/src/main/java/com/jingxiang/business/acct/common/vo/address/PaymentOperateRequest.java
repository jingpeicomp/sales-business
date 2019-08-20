package com.jingxiang.business.acct.common.vo.address;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付单操作请求
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
public class PaymentOperateRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 支付单ID
     */
    private String paymentId;
}
