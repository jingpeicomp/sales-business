package com.jingxiang.business.product.goods;

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.product.common.vo.SkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 商品SKU业务类
 * Created by liuzhaoming on 2019/8/2.
 */
@Service
@Slf4j
public class SkuService {

    @Autowired
    private SkuRepository skuRepository;

    /**
     * 新建商品
     *
     * @param sku 商品
     * @return 更新成功的商品信息
     */
    @Transactional(timeout = 10)
    public Sku save(Sku sku) {
        return skuRepository.save(sku);
    }

    /**
     * 更新商品信息
     *
     * @param sku sku
     * @return 更新成功的SKU信息
     */
    @Transactional(timeout = 10)
    public Sku update(Sku sku) {
        Sku dbSku = skuRepository.findByIdAndShopId(sku.getId(), sku.getShopId())
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的商品"));
        dbSku.update(sku);
        return skuRepository.save(dbSku);
    }

    /**
     * 修改商品发布时间
     *
     * @param shopId      店铺ID
     * @param skuId       SKU ID
     * @param publishTime 发布时间
     * @return 是否修改成功
     */
    @Transactional(timeout = 10)
    public boolean updatePublishTime(String shopId, String skuId, LocalDateTime publishTime) {
        int updateSkuNum = skuRepository.updatePublishTime(shopId, skuId, publishTime);
        return updateSkuNum > 0;
    }

    /**
     * 使用当前时间作为商品发布时间
     *
     * @param shopId 店铺ID
     * @param skuId  SKU ID
     * @return 是否修改成功
     */
    @Transactional(timeout = 10)
    public boolean updatePublishTime(String shopId, String skuId) {
        return updatePublishTime(shopId, skuId, LocalDateTime.now());
    }

    /**
     * 分页查询SKU
     *
     * @param shopId   店铺ID
     * @param pageable 分页查询请求
     * @return SKU分页查询结果
     */
    @Transactional(readOnly = true, timeout = 10)
    public Page<Sku> query(String shopId, Pageable pageable) {
        return skuRepository.findByShopIdOrderByUpdateTimeDesc(shopId, pageable);
    }

    /**
     * 根据店铺ID和商品列表查询SKU值对象
     *
     * @param shopId 店铺ID
     * @param idList SKU ID列表
     * @return SKU值对象列表
     */
    @Transactional(readOnly = true, timeout = 10)
    public List<SkuVo> queryVo(String shopId, List<String> idList) {
        List<Sku> skuList = skuRepository.findByShopIdAndIdIn(shopId, idList);
        return skuList.stream()
                .map(Sku::toVo)
                .collect(Collectors.toList());
    }

    /**
     * 推送商品到Feed流
     *
     * @param shopId 店铺ID
     * @param skuId  商品ID
     * @return 商品，如果操作失败返回null
     */
    @Transactional(timeout = 10)
    public Sku publish(String shopId, String skuId) {
        Sku sku = skuRepository.findByIdAndShopId(skuId, shopId)
                .orElseThrow(ResourceNotFindException::new);
        //TODO, 调用外部接口推送feed流
        log.info("Publish sku to feed {} {}", shopId, skuId);
        if (updatePublishTime(shopId, skuId)) {
            return sku;
        }

        return null;
    }
}
