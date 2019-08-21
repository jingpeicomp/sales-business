package com.jingxiang.business.tc.common.vo.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单账务信息值对象
 *
 * @see com.jingxiang.business.tc.order.OrderAmount
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAmountVo implements Serializable {
    /**
     * 物流费用
     */
    private BigDecimal shipPrice = BigDecimal.ZERO;

    /**
     * 扣除优惠（商品级和订单级）后的应付总额
     * <p>
     * <p>扣除商品级优惠后的应付总额 - 订单级优惠总金额 - 直降总金额；现在就是各个商品价格相加</p>
     */
    private BigDecimal totalItemPrice;

    /**
     * 订单总额
     * <p>
     * <p>扣除优惠（商品级和订单级）后的应付总额 + 物流费用</p>
     * <p>计算公式: totalItemPrice + shipPrice</p>
     */
    private BigDecimal totalPrice;

    /**
     * 订单应付金额
     * -- 账务应支付金额
     * <p>
     * <p>订单总额 - 抵扣金额（积分、充值卡等）</p>
     */
    private BigDecimal totalPayPrice;

    /**
     * 订单实际支付金额
     * -- 账务已支付金额
     */
    private BigDecimal totalPaidPrice;
}
