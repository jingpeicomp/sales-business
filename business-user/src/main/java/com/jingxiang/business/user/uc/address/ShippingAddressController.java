package com.jingxiang.business.user.uc.address;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuzhaoming on 2019/8/7.
 */
@RestController
@RequestMapping("/api/business/account/shippingaddress")
@Validated
@Slf4j
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;


}
