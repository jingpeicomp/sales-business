package com.jingxiang.business.user.acct.common.vo.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付单操作请求
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
