package com.jingxiang.business.user.acct.withdrawal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 提现单数据仓库
 * Created by liuzhaoming on 2019/8/27.
 */
@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, String>{
}
