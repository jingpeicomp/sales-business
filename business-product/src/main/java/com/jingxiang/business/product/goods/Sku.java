package com.jingxiang.business.product.goods;

import com.jingxiang.business.product.goods.vo.SkuCreateRequest;
import com.jingxiang.business.product.goods.vo.SkuVo;
import com.jingxiang.business.utils.CommonUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU实体
 * Created by liuzhaoming on 2019/8/2.
 */
@Entity
@Table(name = "T_BIZ_PRODUCT_SKU")
@Data
public class Sku implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SHOP_ID", nullable = false, updatable = false)
    private String shopId;

    /**
     * 商品名称
     */
    @Column(name = "NAME", nullable = false, length = 100, columnDefinition = "varchar(150)")
    private String name;

    /**
     * 商品单位
     */
    @Column(name = "UNIT", nullable = false)
    private String unit;

    /**
     * 商品默认图片
     */
    @Column(name = "PIC")
    private String pic;

    /**
     * 商品库存
     */
    @Column(name = "STOCK", nullable = false)
    private BigDecimal stock = BigDecimal.ZERO;

    /**
     * 商品价格
     */
    @Column(name = "SALE_PRICE", nullable = false)
    private BigDecimal salePrice;

    /**
     * 商品详情
     */
    @Column(length = 3000, columnDefinition = "TEXT")
    private String description;

    /**
     * 商品添加时间
     */
    @Column(name = "ADD_TIME")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime addTime;

    /**
     * 商品修改时间
     */
    @Column(name = "UPDATE_TIME")
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    @Column(name = "PUBLISH_TIME")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime publishTime;

    /**
     * 商品添加人
     */
    @Column(name = "ADDER")
    private String adder;

    /**
     * 商品修改人
     */
    @Column(name = "MODIFIER")
    private String modifier;

    /**
     * 商品发布人
     */
    @Column(name = "PUBLISHER")
    private String publisher;

    /**
     * 商品描述图片URL列表，多个图片列表用“;”分隔
     */
    @Column(length = 1000)
    private String images;

    /**
     * 版本号
     */
    @Version
    private Long version;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "SKU_ID", referencedColumnName = "ID")
//    private List<SkuImage> images;

    /**
     * 将聚合根转换为值对象
     *
     * @return SKU值对象
     */
    public SkuVo toVo() {
        return SkuVo.builder()
                .id(id)
                .shopId(shopId)
                .name(name)
                .unit(unit)
                .pic(pic)
                .salePrice(salePrice)
                .description(description)
                .addTime(addTime)
                .updateTime(updateTime)
                .images(CommonUtils.splitStr(images))
                .build();
    }

    /**
     * 根据商品创建请求构建SKU实体
     *
     * @param shopId  店铺ID
     * @param request 商品创建请求
     * @return SKU实体
     */
    public static Sku fromCreateRequest(String shopId, SkuCreateRequest request) {
        Sku sku = new Sku();
        sku.setName(request.getName());
        sku.setShopId(shopId);
        sku.setUnit(request.getUnit());
        sku.setPic(request.getImages().get(0));
        sku.setSalePrice(BigDecimal.valueOf(request.getSalePrice()));
        sku.setDescription(request.getDescription());
        sku.setImages(String.join(";", request.getImages()));
        return sku;
    }

    /**
     * 根据商品修改请求构建SKU实体
     *
     * @param shopId  店铺ID
     * @param skuId   待修改的SKU ID
     * @param request 商品修改请求
     * @return SKU实体
     */
    public static Sku fromCreateRequest(String shopId, String skuId, SkuCreateRequest request) {
        Sku sku = Sku.fromCreateRequest(shopId, request);
        sku.setId(skuId);
        return sku;
    }
}
