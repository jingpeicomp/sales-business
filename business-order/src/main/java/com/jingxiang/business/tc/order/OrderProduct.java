package com.jingxiang.business.tc.order;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.product.common.vo.SkuVo;
import com.jingxiang.business.tc.common.consts.OrderConsts;
import com.jingxiang.business.tc.common.vo.order.OrderProductParam;
import com.jingxiang.business.tc.common.vo.order.OrderProductVo;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品条目
 * Created by liuzhaoming on 2019/8/6.
 */
@Entity
@Table(name = "T_BIZ_TC_ORDER_PRODUCT", indexes = {@Index(columnList = "ORDER_ID", name = "IDX_T_ORDER_ID")})
@Data
public class OrderProduct implements Serializable {

    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '订单商品条目ID'")
    private String id;

    /**
     * 订单ID
     */
    @Column(name = "ORDER_ID", columnDefinition = "varchar(32)  comment '订单ID'")
    private String orderId;

    /**
     * 买家编号
     */
    @Column(name = "BUYER", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '买家编号'")
    private String buyer;

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

    /**
     * 根据下单请求构造订单商品实体
     *
     * @param param 下单请求商品参数
     * @return 订单商品条目实体
     */
    public static OrderProduct from(OrderProductParam param) {
        SkuVo skuVo = param.getSkuVo();
        OrderProduct product = new OrderProduct();
        product.setId(IdFactory.createTcId(OrderConsts.ID_PREFIX_ORDER_PRODUCT));
        product.setSkuId(skuVo.getId());
        product.setSkuName(skuVo.getName());
        product.setSkuUnit(skuVo.getUnit());
        product.setSkuPic(skuVo.getPic());
        product.setSkuSalePrice(skuVo.getSalePrice());
        product.setSkuNum(param.getSkuNum());
        return product;
    }

    /**
     * 生成值对象
     *
     * @return 值对象
     */
    public OrderProductVo toVo() {
        return OrderProductVo.builder()
                .id(id)
                .orderId(orderId)
                .shopId(shopId)
                .buyer(buyer)
                .skuId(skuId)
                .skuName(skuName)
                .skuUnit(skuUnit)
                .skuPic(skuPic)
                .skuSalePrice(skuSalePrice)
                .skuNum(skuNum)
                .buyPrice(buyPrice)
                .totalBuyPrice(totalBuyPrice)
                .build();
    }

    /**
     * 计算金额，现在没有营销折扣等手段，等于商品销售价
     */
    public void calculateAmount() {
        buyPrice = skuSalePrice;
        totalBuyPrice = buyPrice.multiply(skuNum);
    }
}
