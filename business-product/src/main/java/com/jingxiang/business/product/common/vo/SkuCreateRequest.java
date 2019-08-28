package com.jingxiang.business.product.common.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * SKU创建请求
 * Created by liuzhaoming on 2019/8/3.
 */
@Data
public class SkuCreateRequest implements Serializable {
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Length(min = 1, max = 100, message = "商品名称长度应该在[1,100]")
    private String name;

    /**
     * 商品单位
     */
    @NotBlank(message = "商品单位不能为空")
    @Length(min = 1, max = 5, message = "商品单位长度应该在[1,5]")
    private String unit = "个";

    /**
     * 商品销售价
     */
    @NotNull(message = "商品价格不能为空")
    @Range(message = "商品价格不能小于0")
    private Double salePrice;

    /**
     * 商品图片列表
     */
    @Size(min = 1, max = 9, message = "商品图片数目应该在[1,9]")
    private List<String> images;

    /**
     * 商品描述
     */
    private String description;

}
