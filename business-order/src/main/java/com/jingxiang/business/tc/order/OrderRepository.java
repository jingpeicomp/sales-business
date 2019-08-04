package com.jingxiang.business.tc.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 订单数据仓库
 * Created by liuzhaoming on 2019/8/3.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}
