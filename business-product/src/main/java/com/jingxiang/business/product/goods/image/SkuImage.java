package com.jingxiang.business.product.goods.image;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * SKU图片
 * Created by liuzhaoming on 2019/8/2.
 */
@Entity
@Table(name = "T_BIZ_PRODUCT_SKU_IMAGE")
@Data
public class SkuImage implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "SKU_ID", nullable = false)
    private String skuId;

    @Column(name = "SHOP_ID", nullable = false)
    private String shopId;

    @Column(name = "URL", nullable = false)
    private String url;

    @Column(name = "SORT_NO", nullable = false)
    private Integer sortNo = 1;
}
