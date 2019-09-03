package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.base.BusinessConsts;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.AccountService;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 提现单业务类
 * Created by liuzhaoming on 2019/8/27.
 */
@Slf4j
@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private AccountService accountService;

    /**
     * 创建提现单
     *
     * @param request 提现单创建请求
     * @return 提现单
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS)
    public Withdrawal create(WithdrawalCreateRequest request) {
        Withdrawal withdrawal = Withdrawal.from(request);
        withdrawalRepository.save(withdrawal);
        accountService.withdrawalCreated(withdrawal.toVo());
        return withdrawal;
    }

    /**
     * 手工确认提现单
     *
     * @param id 提现单ID
     * @return 提现单
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS)
    public Withdrawal confirm(String id) {
        Withdrawal withdrawal = Optional.ofNullable(withdrawalRepository.findOne(id))
                .orElseThrow(() -> new ServiceException("找不到对应的提现单,ID:" + id));
        withdrawal.confirm();
        return withdrawalRepository.save(withdrawal);
    }

    /**
     * 根据用户ID和完成状态查询提现单
     *
     * @param userId   用户ID
     * @param status   提现单完成状态
     * @param pageable 分页信息
     * @return 提现单列表
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Page<Withdrawal> query(String userId, CompleteStatus status, Pageable pageable) {
        return withdrawalRepository.findByUserIdAndCompleteStatusOrderByCreateTimeDesc(userId, status, pageable);
    }

    /**
     * 根据用户ID查询提现单
     *
     * @param userId   用户ID
     * @param pageable 分页信息
     * @return 提现单列表
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Page<Withdrawal> query(String userId, Pageable pageable) {
        return withdrawalRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
    }

    /**
     * 系统根据状态查询提现单
     *
     * @param status   提现单完成状态
     * @param pageable 分页信息
     * @return 提现单列表
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Page<Withdrawal> queryAsSystem(CompleteStatus status, Pageable pageable) {
        return withdrawalRepository.findByCompleteStatusOrderByCreateTimeDesc(status, pageable);
    }

    /**
     * 系统查询提现单
     *
     * @param pageable 分页信息
     * @return 提现单列表
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Page<Withdrawal> queryAsSystem(Pageable pageable) {
        return withdrawalRepository.findAllByOrderByCreateTimeDesc(pageable);
    }

    /**
     * 根据ID查询提现单
     *
     * @param id 提现单ID
     * @return 提现单详情
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Optional<Withdrawal> queryById(String id) {
        return Optional.ofNullable(withdrawalRepository.findOne(id));
    }

    /**
     * 根据ID和用户ID查询提现单
     *
     * @param id     提现单ID
     * @param userId 用户ID
     * @return 提现单
     */
    @Transactional(timeout = BusinessConsts.TRANSACTION_TIMEOUT_IN_SECONDS, readOnly = true)
    public Optional<Withdrawal> queryById(String id, String userId) {
        return withdrawalRepository.findByIdAndUserId(id, userId);
    }
}
