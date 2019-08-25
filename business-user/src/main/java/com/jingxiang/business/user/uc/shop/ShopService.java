package com.jingxiang.business.user.uc.shop;

import com.jingxiang.business.user.uc.common.vo.shop.ShopCreateRequest;
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
     * 根据店铺ID查询店铺值对象
     *
     * @param id 店铺ID
     * @return 对应的店铺值对象
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<ShopVo> queryVoById(String id) {
        return Optional.ofNullable(shopRepository.findOne(id))
                .map(Shop::toVo);
    }
}
