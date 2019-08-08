package com.jingxiang.business.tc.common.vo.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单支付请求
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
public class OrderPayRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 订单ID
     */
    private String orderId;
}
