package com.jingxiang.business.user.acct.deposit;

import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 充值单数据仓库
 * Created by liuzhaoming on 2019/8/26.
 */
@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    /**
     * 根据用户ID和完成状态查询充值单
     *
     * @param userId         用户ID
     * @param completeStatus 支付单完成状态
     * @param pageable       分页信息
     * @return 支付单列表
     */
    Page<Deposit> findByUserIdAndCompleteStatusOrderByFinishTimeDesc(String userId, CompleteStatus completeStatus, Pageable pageable);

    /**
     * 根据ID和用户ID查询充值单
     *
     * @param id     充值单ID
     * @param userId 用户ID
     * @return 充值单详细信息
     */
    Optional<Deposit> findByIdAndUserId(String id, String userId);
}
