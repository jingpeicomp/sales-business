package com.jingxiang.business.user.acct.account.bill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 账户流水数据仓库
 * Created by liuzhaoming on 2019/8/25.
 */
@Repository
public interface AccountBillRepository extends JpaRepository<AccountBill, String> {
}
