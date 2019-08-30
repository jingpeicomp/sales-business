package com.jingxiang.business.user.uc.shop;

import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.user.uc.common.vo.shop.ShopCreateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopUpdateRequest;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by liuzhaoming on 2019/8/25.
 */
@Service
@Slf4j
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * 创建新的店铺
     *
     * @param request 店铺创建请求
     * @return 店铺
     */
    @Transactional(timeout = 10)
    public Shop create(ShopCreateRequest request) {
        Shop shop = Shop.from(request);
        return shopRepository.save(shop);
    }

    /**
     * 更新的店铺
     *
     * @param request 店铺更新请求
     * @return 店铺
     */
    @Transactional(timeout = 10)
    public Shop update(ShopUpdateRequest request) {
        Shop shop = query(request.getShopId())
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的店铺"));
        shop.update(request);
        return shopRepository.save(shop);
    }

    /**
     * 根据店铺ID查询店铺对象
     *
     * @param id 店铺ID
     * @return 对应的店铺对象
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<Shop> query(String id) {
        return Optional.ofNullable(shopRepository.findOne(id));
    }

    /**
     * 根据店铺ID查询店铺值对象
     *
     * @param id 店铺ID
     * @return 对应的店铺值对象
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<ShopVo> queryVo(String id) {
        return Optional.ofNullable(shopRepository.findOne(id))
                .map(Shop::toVo);
    }

    /**
     * 获取群关联的店铺信息
     *
     * @param groupId 群ID
     * @return 店铺信息
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<Shop> queryByGroupId(String groupId) {
        return shopRepository.findByGroupId(groupId);
    }

    /**
     * 查询店铺店主
     *
     * @param id 店铺ID
     * @return 店主用户ID
     */
    @Transactional(timeout = 10, readOnly = true)
    public String queryOwner(String id) {
        return shopRepository.findOwnerById(id);
    }

    /**
     * 查询店铺合伙人
     *
     * @param id 店铺ID
     * @return 合伙人用户ID
     */
    @Transactional(timeout = 10, readOnly = true)
    public String queryPartner(String id) {
        return shopRepository.findPartnerById(id);
    }
}
