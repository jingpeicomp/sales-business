package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * 提现单数据仓库
 * Created by liuzhaoming on 2019/8/27.
 */
@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, String> {
    /**
     * 根据用户ID分页查询提现单
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return 提现单列表
     */
    Page<Withdrawal> findByUserIdOrderByCreateTimeDesc(String userId, Pageable pageable);

    /**
     * 根据用户ID和完成状态查询提现单
     *
     * @param userId   用户ID
     * @param status   提现单完成状态
     * @param pageable 分页信息
     * @return 提现单列表
     */
    Page<Withdrawal> findByUserIdAndCompleteStatusOrderByCreateTimeDesc(String userId, CompleteStatus status, Pageable pageable);

    /**
     * 根据支付单完成状态查询提现单
     *
     * @param status   提现单完成状态
     * @param pageable 分页信息
     * @return 提现单列表
     */
    Page<Withdrawal> findByCompleteStatusOrderByCreateTimeDesc(CompleteStatus status, Pageable pageable);

    /**
     * 查询提现单
     *
     * @param pageable 分页信息
     * @return 提现单列表
     */
    Page<Withdrawal> findAllByOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 根据ID和用户ID查询提现单
     *
     * @param id     提现单ID
     * @param userId 用户ID
     * @return 提现单
     */
    Optional<Withdrawal> findByIdAndUserId(String id, String userId);
}
