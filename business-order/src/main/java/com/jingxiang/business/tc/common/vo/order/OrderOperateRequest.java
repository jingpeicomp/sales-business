package com.jingxiang.business.tc.common.vo.order;

import com.jingxiang.business.consts.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单操作请求
 * Created by liuzhaoming on 2019/8/21.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderOperateRequest implements Serializable {
    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 动作执行角色
     */
    private Role role;
}
