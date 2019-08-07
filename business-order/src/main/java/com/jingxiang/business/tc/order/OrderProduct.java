package com.jingxiang.business.tc.order;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单商品条目
 * Created by liuzhaoming on 2019/8/6.
 */
@Entity
@Table(name = "T_BIZ_TC_ORDER_PRODUCT")
@Data
public class OrderProduct {

    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '订单商品条目ID'")
    private String id;

    /**
     * 订单ID
     */
    @Column(name = "ORDER_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '订单ID'")
    private String orderId;

    /**
     * 店铺ID
     */
    @Column(name = "SHOP_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺编号'")
    private String shopId;

    /**
     * SKU ID
     */
    @Column(name = "SKU_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '商品ID'")
    private String skuId;

    /**
     * 商品名称
     */
    @Column(name = "SKU_NAME", nullable = false, updatable = false, length = 100, columnDefinition = "varchar(150) not null comment '商品名称'")
    private String skuName;

    /**
     * 商品单位
     */
    @Column(name = "SKU_UNIT", nullable = false, updatable = false, columnDefinition = "varchar(10) not null comment '商品单位'")
    private String skuUnit;

    /**
     * 商品默认图片
     */
    @Column(name = "PIC", updatable = false, columnDefinition = "varchar(100) comment '商品默认图片'")
    private String skuPic;

    /**
     * 商品销售价
     */
    @Column(name = "SKU_SALE_PRICE", nullable = false, updatable = false, columnDefinition = "decimal(10,2) not null comment '商品销售单价'")
    private BigDecimal skuSalePrice;

    /**
     * 当前的商品数量
     */
    @Column(name = "SKU_NUM", columnDefinition = "decimal(10,2) comment '商品数量'")
    private BigDecimal skuNum;

    /**
     * 扣除商品级优惠后购买单价，现在等同于商品销售价
     */
    @Column(columnDefinition = "decimal(10,2) comment '扣除商品级优惠后购买单价'")
    private BigDecimal buyPrice;

    /**
     * 扣除商品级优惠后的应付总额 = 扣除商品级优惠后购买单价 * 实际购买商品数量 = buyPrice * itemNum
     */
    @Column(columnDefinition = "decimal(20,2) comment '扣除商品级优惠后的应付总额'")
    private BigDecimal totalBuyPrice;
}
