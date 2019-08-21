package com.jingxiang.business.tc.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /**
     * 查询买家待支付订单
     *
     * @param buyer    买家
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.buyer=?0 and t.payStatus<>4")
    Page<Order> findBuyerWaitPayOrders(String buyer, Pageable pageable);

    /**
     * 查询买家待发货订单
     *
     * @param buyer    买家
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.buyer=?0 and t.payStatus=4 and t.shipStatus=1")
    Page<Order> findBuyerWaitShipOrders(String buyer, Pageable pageable);

    /**
     * 查询买家待收货订单
     *
     * @param buyer    买家
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.buyer=?0 and t.payStatus=4 and t.shipStatus=2")
    Page<Order> findBuyerWaitReceiveOrders(String buyer, Pageable pageable);

    /**
     * 查询买家已完成订单
     *
     * @param buyer    买家
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.buyer=?0 and t.completeStatus=3")
    Page<Order> findBuyerCompleteOrders(String buyer, Pageable pageable);

    /**
     * 查询卖家待处理订单
     *
     * @param shopId   店铺ID
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.shopId=?0 and t.payStatus=4 and t.shipStatus=1")
    Page<Order> findSellerWaitDealOrders(String shopId, Pageable pageable);

    /**
     * 查询卖家待收货订单
     *
     * @param shopId   店铺ID
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.shopId=?0 and t.payStatus=4 and t.shipStatus=2")
    Page<Order> findSellerWaitReceiveOrders(String shopId, Pageable pageable);

    /**
     * 查询卖家已完成订单
     *
     * @param shopId   店铺ID
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query("select t from Order t where t.shopId=?0 and t.completeStatus=3")
    Page<Order> findSellerCompleteOrders(String shopId, Pageable pageable);
}
