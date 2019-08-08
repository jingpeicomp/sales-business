package com.jingxiang.business.tc.common.vo.order;

import com.jingxiang.business.product.base.vo.SkuVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品请求参数
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
public class OrderProductParam implements Serializable {
    /**
     * SKU ID
     */
    private String skuId;

    /**
     * 购买数目
     */
    private BigDecimal skuNum;

    /**
     * SKU信息，无需调用方传入
     */
    private SkuVo skuVo;
}
