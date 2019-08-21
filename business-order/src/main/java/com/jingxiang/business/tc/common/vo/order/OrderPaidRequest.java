package com.jingxiang.business.tc.common.vo.order;

import com.jingxiang.business.acct.common.vo.address.PaymentVo;
import com.jingxiang.business.consts.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单支付结果请求
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaidRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 是否成功
     */
    private boolean successful;

    /**
     * 结果信息，只有失败时才有值
     */
    private String message;

    /**
     * 字符单信息
     */
    private PaymentVo vo;

    /**
     * 操作对象
     */
    private Role role;

    /**
     * 支付金额
     */
    private BigDecimal paidPrice;
}
