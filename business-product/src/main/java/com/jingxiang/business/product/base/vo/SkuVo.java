package com.jingxiang.business.product.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SKU值对象
 * Created by liuzhaoming on 2019/8/2.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuVo implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    private String shopId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品单位
     */
    private String unit;

    /**
     * 商品默认图片
     */
    private String pic;

    /**
     * 商品价格
     */
    private BigDecimal salePrice;

    /**
     * 商品详情
     */
    private String description;

    /**
     * 商品添加时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime addTime;

    /**
     * 商品修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 商品描述图片URL列表，多个图片列表用“;”分隔
     */
    private List<String> images;
}
