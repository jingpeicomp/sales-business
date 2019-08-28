package com.jingxiang.business.product.goods;

import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.product.common.vo.SkuCreateRequest;
import com.jingxiang.business.product.common.vo.SkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * SKU对外接口Controller
 * Created by liuzhaoming on 2019/8/3.
 */
@RestController
@RequestMapping("/api/business/product")
@Validated
@Slf4j
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 创建SKU
     *
     * @param shopId  店铺ID
     * @param request SKU创建请求
     * @return 创建成功的SKU信息
     */
    @RequestMapping(path = "/{shopId}/skus", method = RequestMethod.POST)
    public SkuVo createSku(@PathVariable String shopId, @Valid @RequestBody SkuCreateRequest request) {
        Sku sku = Sku.fromCreateRequest(shopId, request);
        return Optional.ofNullable(skuService.save(sku))
                .map(Sku::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to create sku, shopID:{} , request:{}", shopId, request);
                    return new ServiceException("Fail to create sku, shopId:" + shopId + ", name:" + request.getName());
                });
    }

    /**
     * 更新商品信息
     *
     * @param shopId  店铺ID
     * @param skuId   SKU ID
     * @param request SKU更新请求
     * @return 更新成功后的SKU信息
     */
    @RequestMapping(path = "/{shopId}/skus/{skuId}", method = RequestMethod.PUT)
    public SkuVo updateSku(@PathVariable String shopId, @PathVariable String skuId, @Valid @RequestBody SkuCreateRequest request) {
        Sku sku = Sku.fromCreateRequest(shopId, skuId, request);
        return Optional.ofNullable(skuService.update(sku))
                .map(Sku::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to update sku, shopID:{}, skuId:{}, request:{}", shopId, skuId, request);
                    return new ServiceException("Fail to update sku, shopId:" + shopId + ", skuId:" + skuId);
                });
    }

    /**
     * 分页查询SKU
     *
     * @param shopId 店铺ID
     * @param page   页码，从0开始
     * @param size   每页数据数目
     * @return SKU分页查询结果
     */
    @RequestMapping(path = "/{shopId}/skus", method = RequestMethod.GET)
    public Page<SkuVo> query(@PathVariable String shopId,
                             @RequestParam(required = false, defaultValue = "0") int page,
                             @RequestParam(required = false, defaultValue = "30") int size) {
        Pageable pageRequest = new PageRequest(page, size);
        Page<Sku> skuPage = skuService.query(shopId, pageRequest);
        List<SkuVo> voList = skuPage.getContent().stream()
                .map(Sku::toVo)
                .collect(Collectors.toList());
        return new PageImpl<>(voList, pageRequest, skuPage.getTotalElements());
    }

    /**
     * 发布商品
     *
     * @param shopId 店铺ID
     * @param skuId  SKU ID
     * @return 发布后的商品
     */
    @RequestMapping(path = "/{shopId}/skus/{skuId}/publish", method = RequestMethod.PUT)
    public SkuVo publish(@PathVariable String shopId, @PathVariable String skuId) {
        return Optional.ofNullable(skuService.publish(shopId, skuId))
                .map(Sku::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to publish sku, shopID:{}, skuId:{}", shopId, skuId);
                    return new ServiceException("Fail to publish sku, shopId:" + shopId + ", skuId:" + skuId);
                });
    }
}
