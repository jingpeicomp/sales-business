package com.jingxiang.business.product.goods;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.product.common.consts.ProductConsts;
import com.jingxiang.business.product.common.vo.SkuCreateRequest;
import com.jingxiang.business.product.common.vo.SkuVo;
import com.jingxiang.business.utils.CommonUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * SKU实体
 * Created by liuzhaoming on 2019/8/2.
 */
@Entity
@Table(name = "T_BIZ_PC_SKU", indexes = {@Index(columnList = "SHOP_ID", name = "IDX_S_SHOP"),
        @Index(columnList = "UPDATE_TIME", name = "IDX_S_UPDATE_TIME")})
@Data
@EntityListeners(AuditingEntityListener.class)
public class Sku implements Serializable {
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '商品ID'")
    private String id;

    /**
     * 店铺ID
     */
    @Column(name = "SHOP_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺编号'")
    private String shopId;

    /**
     * 商品名称
     */
    @Column(name = "NAME", nullable = false, length = 100, columnDefinition = "varchar(150) not null comment '商品名称'")
    private String name;

    /**
     * 商品单位
     */
    @Column(name = "UNIT", nullable = false, columnDefinition = "varchar(10) not null comment '商品单位'")
    private String unit;

    /**
     * 商品默认图片
     */
    @Column(name = "PIC", columnDefinition = "varchar(100) comment '商品默认图片'")
    private String pic;

    /**
     * 商品库存
     */
    @Column(name = "STOCK", nullable = false, columnDefinition = "int not null default 0 comment '订单支付状态'")
    private BigDecimal stock = BigDecimal.ZERO;

    /**
     * 商品销售价
     */
    @Column(name = "SALE_PRICE", nullable = false, columnDefinition = "decimal(10,2) not null comment '商品销售价'")
    private BigDecimal salePrice;

    /**
     * 商品详情
     */
    @Column(length = 3000, columnDefinition = "text comment '商品详情'")
    private String description;

    /**
     * 商品添加时间
     */
    @Column(name = "CREATE_TIME", columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '商品添加时间'")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 商品修改时间
     */
    @Column(name = "UPDATE_TIME", columnDefinition = "datetime comment '商品修改时间'")
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    /**
     * 商品发布时间
     */
    @Column(name = "PUBLISH_TIME", columnDefinition = "datetime comment '商品发布时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime publishTime;

    /**
     * 商品添加人
     */
    @Column(name = "ADDER", columnDefinition = "varchar(32) comment '商品添加人'")
    private String adder;

    /**
     * 商品修改人
     */
    @Column(name = "MODIFIER", columnDefinition = "varchar(32) comment '商品修改人'")
    private String modifier;

    /**
     * 商品发布人
     */
    @Column(name = "PUBLISHER", columnDefinition = "varchar(32) comment '商品发布人'")
    private String publisher;

    /**
     * 商品描述图片URL列表，多个图片列表用“;”分隔
     */
    @Column(length = 1000, columnDefinition = "varchar(1000) comment '商品描述图片URL列表，多个图片列表用;分隔'")
    private String images;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
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
                .addTime(createTime)
                .updateTime(updateTime)
                .publishTime(publishTime)
                .images(CommonUtils.splitStr(images))
                .build();
    }

    /**
     * 更新SKU信息
     *
     * @param other 参照的SKU
     */
    public void update(Sku other) {
        if (StringUtils.isNotBlank(other.description) && !Objects.equals(description, other.description)) {
            description = other.description;
        }

        if (StringUtils.isNotBlank(other.images) && !Objects.equals(images, other.images)) {
            images = other.images;
            pic = images.split(";")[0];
        }

        if (StringUtils.isNotBlank(other.name) && !Objects.equals(name, other.name)) {
            name = other.name;
        }

        if (StringUtils.isNotBlank(other.unit) && !Objects.equals(unit, other.unit)) {
            unit = other.unit;
        }

        if (null != other.salePrice && !Objects.equals(salePrice, other.salePrice)) {
            salePrice = other.salePrice;
        }

        if (null != other.stock && !Objects.equals(stock, other.stock)) {
            stock = other.stock;
        }

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
        sku.setId(IdFactory.createProductId(ProductConsts.ID_PREFIX_SKU));
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
