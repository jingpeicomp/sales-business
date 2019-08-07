package com.jingxiang.business.tc.order;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单金额
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@Embeddable
public class OrderAmount implements Serializable{
    /**
     * 物流费用
     */
    @Column(name = "SHIP_PRICE", columnDefinition = "decimal(10,2) comment '物流费用'")
    private BigDecimal shipPrice = BigDecimal.ZERO;

    /**
     * 扣除优惠（商品级和订单级）后的应付总额
     * <p>
     * <p>扣除商品级优惠后的应付总额 - 订单级优惠总金额 - 直降总金额；现在就是各个商品价格相加</p>
     */
    @Column(name = "TOTAL_ITEM_PRICE", columnDefinition = "decimal(20,2) comment '扣除优惠（商品级和订单级）后的应付总额'")
    private BigDecimal totalItemPrice;

    /**
     * 订单总额
     * <p>
     * <p>扣除优惠（商品级和订单级）后的应付总额 + 物流费用</p>
     * <p>计算公式: totalItemPrice + shipPrice</p>
     */
    @Column(name = "TOTAL_PRICE", columnDefinition = "decimal(20,2) comment '订单总额'")
    private BigDecimal totalPrice;

    /**
     * 订单应付金额
     * -- 账务应支付金额
     * <p>
     * <p>订单总额 - 抵扣金额（积分、充值卡等）</p>
     */
    @Column(name = "TOTAL_PAY_PRICE", columnDefinition = "decimal(20,2) comment '订单应付金额'")
    private BigDecimal totalPayPrice;

    /**
     * 订单实际支付金额
     * -- 账务已支付金额
     */
    @Column(name = "TOTAL_PAID_PRICE", columnDefinition = "decimal(20,2) comment '订单实际支付金额'")
    private BigDecimal totalPaidPrice;
}
