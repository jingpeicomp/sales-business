package com.jingxiang.business.tc.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 订单数据仓库
 * Created by liuzhaoming on 2019/8/3.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    /**
     * 根据订单ID和店铺ID查询订单
     *
     * @param id     订单ID
     * @param shopId 店铺ID
     * @return 订单
     */
    Optional<Order> findByIdAndShopId(String id, String shopId);
}
