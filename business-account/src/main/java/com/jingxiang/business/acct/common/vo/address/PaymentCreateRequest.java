package com.jingxiang.business.acct.common.vo.address;

import com.jingxiang.business.consts.PayType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付单创建请求
 * Created by liuzhaoming on 2019/8/9.
 */
@Data
public class PaymentCreateRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 付款者
     */
    private String buyer;

    /**
     * 支付类型，微信支付:0;支付宝:1
     */
    private PayType payType;

    /**
     * 应付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付单标题，会显示在支付平台
     */
    private String title;

    /**
     * 支付单描述
     */
    private String description;
}
