package com.jingxiang.business.user.uc.shop;

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.uc.common.vo.shop.ShopCreateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopUpdateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * 店铺REST接口
 * Created by liuzhaoming on 2019/8/29.
 */
@Api(value = "Shop API", description = "店铺REST API接口")
@RestController
@RequestMapping("/api/business/user/shops")
@Validated
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 根据群ID查询对应的店铺信息
     *
     * @param groupId 群ID
     * @return 店铺信息
     */
    @ApiOperation(value = "根据群ID查询对应的店铺信息", notes = "根据群ID查询对应的店铺信息")
    @RequestMapping(path = "/groups/{groupId}", method = RequestMethod.GET)
    public ShopVo queryByGroupId(@PathVariable String groupId) {
        return shopService.queryByGroupId(groupId)
                .map(Shop::toVo)
                .orElse(null);
    }

    /**
     * 根据ID查询店铺信息
     *
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    @ApiOperation(value = "根据ID查询店铺信息", notes = "根据ID查询店铺信息")
    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    public ShopVo queryById(@PathVariable String shopId) {
        return shopService.query(shopId)
                .map(Shop::toVo)
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的店铺"));
    }

    /**
     * 创建店铺
     *
     * @param request 店铺创建请求
     * @return 店铺信息
     */
    @ApiOperation(value = "创建店铺", notes = "创建店铺")
    @RequestMapping(method = RequestMethod.POST)
    public ShopVo create(@Valid @RequestBody ShopCreateRequest request) {
        return Optional.ofNullable(shopService.create(request))
                .map(Shop::toVo)
                .orElseThrow(() -> new ServiceException("创建店铺失败"));
    }

    /**
     * 更新店铺信息
     *
     * @param shopId  店铺ID
     * @param request 店铺更新请求
     * @return 更新后的店铺信息
     */
    @ApiOperation(value = "更新店铺信息", notes = "更新店铺信息")
    @RequestMapping(path = "/{shopId}", method = RequestMethod.PUT)
    public ShopVo update(@PathVariable String shopId, @Valid @RequestBody ShopUpdateRequest request) {
        request.setShopId(shopId);
        return Optional.ofNullable(shopService.update(request))
                .map(Shop::toVo)
                .orElseThrow(() -> new ServiceException("更新店铺失败"));
    }
}
