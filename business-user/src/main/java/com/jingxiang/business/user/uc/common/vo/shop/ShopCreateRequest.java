package com.jingxiang.business.user.uc.common.vo.shop;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liuzhaoming on 2019/8/25.
 */
@Data
public class ShopCreateRequest implements Serializable {

    /**
     * 店铺所有者
     */
    private String owner;

    /**
     * 店铺关联的群ID
     */
    private String groupId;

    /**
     * 店铺名称
     */
    private String name;

    /**
     * 合伙人
     */
    private String partner;
}
