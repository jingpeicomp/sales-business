package com.jingxiang.business.tc.address;

import lombok.Data;

/**
 * 地址
 * Created by liuzhaoming on 2019/8/7.
 */
@Data
public class Address {
    private String province;

    private String city;

    private String district;

    private String street;

    private String detail;
}
