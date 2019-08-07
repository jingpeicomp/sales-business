package com.jingxiang.business.acct.common.vo.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 默认收货地址VO
 * Created by liuzhaoming on 2019/8/7.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingAddressVo implements Serializable {

    /**
     * ID
     */
    private String id;

    /**
     * 用户账户ID
     */
    private String accountId;

    /**
     * 是否默认地址
     */
    private boolean def;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverMobile;

    /**
     * 收货人所在省份
     */
    private String receiverProvince;

    /**
     * 收货人所在城市
     */
    private String receiverCity;

    /**
     * 收货人所在区县
     */
    private String receiverDistrict;

    /**
     * 收货人所在街道
     */
    private String receiverStreet;

    /**
     * 收货人详细地址
     */
    private String receiverAddress;
}
