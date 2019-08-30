package com.jingxiang.business.user.uc.address;

import com.jingxiang.business.user.uc.common.vo.address.ShippingAddressVo;
import com.jingxiang.business.utils.AuthUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址Rest接口
 * Created by liuzhaoming on 2019/8/7.
 */
@Api(value = "Shipping Address API", description = "用户收货地址REST API接口")
@RestController
@RequestMapping("/api/business/user/shippingaddress")
@Validated
@Slf4j
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    /**
     * 查询用户的默认下单地址（上一次下单地址）
     *
     * @return 用户的默认下单地址
     */
    @ApiOperation(value = "查询用户的默认下单地址", notes = "查询用户的默认下单地址（上一次下单地址）")
    @RequestMapping(path = "/default", method = RequestMethod.GET)
    public ShippingAddressVo queryDefault() {
        return shippingAddressService.queryDefault(AuthUtils.getUserId())
                .map(ShippingAddress::toVo)
                .orElse(null);
    }

    /**
     * 查询用户的所有下单地址
     *
     * @return 用户所有下单地址列表
     */
    @ApiOperation(value = "查询用户的所有下单地址", notes = "查询用户的所有下单地址", responseContainer = "List", response = ShippingAddressVo.class)
    @RequestMapping(method = RequestMethod.GET)
    public List<ShippingAddressVo> query() {
        return shippingAddressService.query(AuthUtils.getUserId()).stream()
                .map(ShippingAddress::toVo)
                .collect(Collectors.toList());
    }

    /**
     * 新建下单地址
     *
     * @param vo 待创建的下单地址信息参数
     * @return 创建成功的下单地址信息
     */
    @ApiOperation(value = "新建下单地址", notes = "新建下单地址")
    @RequestMapping(method = RequestMethod.POST)
    public ShippingAddressVo create(@Valid @RequestBody ShippingAddressVo vo) {
        vo.setUserId(AuthUtils.getUserId());
        return shippingAddressService.save(vo).toVo();
    }

    /**
     * 修改下单地址
     *
     * @param vo 待修改的下单地址信息参数
     * @return 修改成功的下单地址信息
     */
    @ApiOperation(value = "修改下单地址", notes = "修改下单地址")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ShippingAddressVo update(@PathVariable String id, @Valid @RequestBody ShippingAddressVo vo) {
        vo.setId(id);
        vo.setUserId(AuthUtils.getUserId());
        return shippingAddressService.save(vo).toVo();
    }

    /**
     * 删除收货地址
     *
     * @param id 收货地址ID
     */
    @ApiOperation(value = "删除下单地址", notes = "删除下单地址")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        shippingAddressService.delete(AuthUtils.getUserId(), id);
    }
}
