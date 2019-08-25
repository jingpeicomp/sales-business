package com.jingxiang.business.user.uc.common.vo.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺值对象
 * Created by liuzhaoming on 2019/8/25.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopVo implements Serializable {
    /**
     * 店铺编号
     */
    private String id;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
