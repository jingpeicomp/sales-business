package com.jingxiang.business.user.uc.common.vo.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
    private String userId;

    /**
     * 是否默认地址
     */
    private boolean def;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Length(min = 1, max = 32, message = "收货人姓名长度应该在[1,64]")
    private String receiverName;

    /**
     * 收货人电话
     */
    @NotBlank(message = "收货人电话不能为空")
    @Length(min = 8, max = 20, message = "收货人电话长度应该在[8,20]")
    private String receiverMobile;

    /**
     * 收货人所在省份
     */
    @NotBlank(message = "收货人省份不能为空")
    @Length(min = 2, max = 20, message = "收货人省份长度应该在[2,64]")
    private String receiverProvince;

    /**
     * 收货人所在城市
     */
    @NotBlank(message = "收货人城市不能为空")
    @Length(min = 2, max = 20, message = "收货人城市长度应该在[2,64]")
    private String receiverCity;

    /**
     * 收货人所在区县
     */
    @Length(max = 20, message = "收货人区县长度应该在[1,64]")
    private String receiverDistrict;

    /**
     * 收货人所在街道
     */
    @Length(max = 20, message = "收货人街道长度应该在[1,64]")
    private String receiverStreet;

    /**
     * 收货人详细地址
     */
    @NotBlank(message = "收货人详细地址不能为空")
    @Length(min = 1, max = 200, message = "收货人电话长度应该在[1,200]")
    private String receiverAddress;
}
