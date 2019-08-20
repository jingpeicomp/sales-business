package com.jingxiang.business.acct.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付单数据仓库
 * Created by liuzhaoming on 2019/8/9.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    /**
     * 根据ID和店铺ID查询支付单
     *
     * @param id     支付单ID
     * @param shopId 店铺ID
     * @return 符合条件的支付单
     */
    Optional<Payment> findByIdAndShopId(String id, String shopId);

    /**
     * 根据ID和店铺ID查询支付单
     *
     * @param id      支付单ID
     * @param orderId 订单ID
     * @return 符合条件的支付单
     */
    Optional<Payment> findByIdAndOrderId(String id, String orderId);

}
