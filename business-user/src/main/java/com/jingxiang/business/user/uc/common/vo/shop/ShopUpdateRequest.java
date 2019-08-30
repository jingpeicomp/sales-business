package com.jingxiang.business.user.uc.common.vo.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 店铺更新请求
 * Created by liuzhaoming on 2019/8/30.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopUpdateRequest implements Serializable {
    /**
     * 店铺ID， 无需前端传入
     */
    private String shopId;

    /**
     * 店铺名称
     */
    @Length(min = 1, max = 64, message = "店铺名称长度应该在[1,64]")
    private String name;

    /**
     * 合伙人
     */
    private String partner;
}
