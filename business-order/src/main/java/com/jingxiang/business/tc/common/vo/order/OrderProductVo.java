package com.jingxiang.business.tc.common.vo.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品值对象
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductVo implements Serializable{
    private String id;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 买家编号
     */
    private String buyer;

    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * SKU ID
     */
    private String skuId;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 商品单位
     */
    private String skuUnit;

    /**
     * 商品默认图片
     */
    private String skuPic;

    /**
     * 商品销售价
     */
    private BigDecimal skuSalePrice;

    /**
     * 当前的商品数量
     */
    private BigDecimal skuNum;

    /**
     * 扣除商品级优惠后购买单价，现在等同于商品销售价
     */
    private BigDecimal buyPrice;

    /**
     * 扣除商品级优惠后的应付总额 = 扣除商品级优惠后购买单价 * 实际购买商品数量 = buyPrice * itemNum
     */
    private BigDecimal totalBuyPrice;
}
