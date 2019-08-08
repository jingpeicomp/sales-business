package com.jingxiang.business.tc.common.vo.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单收货信息
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReceiverVo implements Serializable {
    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人电话
     */
    private String mobile;

    /**
     * 收货人所在省份
     */
    private String province;

    /**
     * 收货人所在城市
     */
    private String city;

    /**
     * 收货人所在区县
     */
    private String district;

    /**
     * 收货人所在街道
     */
    private String street;

    /**
     * 收货人详细地址
     */
    private String address;
}
