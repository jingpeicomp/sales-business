package com.jingxiang.business.product.sku;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * SKU实体
 * Created by liuzhaoming on 2019/8/2.
 */
@Entity
@Table(name = "T_BUSINESS_PRODUCT_SKU")
@Data
public class Sku implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;


}
