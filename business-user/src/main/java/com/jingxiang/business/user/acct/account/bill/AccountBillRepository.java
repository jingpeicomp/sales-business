package com.jingxiang.business.user.acct.account.bill;

import com.jingxiang.business.user.acct.common.consts.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户流水数据仓库
 * Created by liuzhaoming on 2019/8/25.
 */
@Repository
public interface AccountBillRepository extends JpaRepository<AccountBill, String> {
    /**
     * 根据用户ID和账户类型查询账户流水
     *
     * @param userId      用户ID
     * @param accountType 账户类型
     * @return 账户流水列表
     */
    List<AccountBill> queryByUserIdAndAccountTypeOrderByCreateTimeDesc(String userId, AccountType accountType);
}