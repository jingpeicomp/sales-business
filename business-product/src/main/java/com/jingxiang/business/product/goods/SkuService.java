package com.jingxiang.business.product.goods;

import com.jingxiang.business.exception.NotFindException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * 商品SKU业务类
 * Created by liuzhaoming on 2019/8/2.
 */
@Service
@Slf4j
public class SkuService {

    @Autowired
    private SkuRepository skuRepository;

    @Transactional(timeout = 10)
    public Sku save(Sku sku) {
        return skuRepository.save(sku);
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
     * 推送商品到Feed流
     *
     * @param shopId 店铺ID
     * @param skuId  商品ID
     * @return 商品，如果操作失败返回null
     */
    public Sku publish(String shopId, String skuId) {
        Sku sku = skuRepository.findByIdAndShopId(skuId, shopId)
                .orElseThrow(NotFindException::new);
        //TODO, 调用外部接口推送feed流
        if (updatePublishTime(shopId, skuId)) {
            return sku;
        }

        return null;
    }
}
