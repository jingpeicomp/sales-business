package com.jingxiang.business.user.acct.deposit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 充值单数据仓库
 * Created by liuzhaoming on 2019/8/26.
 */
@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
}
