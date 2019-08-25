package com.jingxiang.business.user.acct.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * 账户数据仓库
 * Created by liuzhaoming on 2019/8/25.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * 根据用户ID和账户类型查询对应的账户
     *
     * @param userId 用户ID
     * @param type   账户类型
     * @return
     * @see com.jingxiang.business.user.acct.common.consts.AccountType
     */
    Optional<Account> findByUserIdAndType(String userId, int type);

}
