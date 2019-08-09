package com.jingxiang.business.acct.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 支付单数据仓库
 * Created by liuzhaoming on 2019/8/9.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
