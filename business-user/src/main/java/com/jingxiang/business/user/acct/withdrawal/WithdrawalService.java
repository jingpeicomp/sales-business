package com.jingxiang.business.user.acct.withdrawal;

import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.AccountService;
import com.jingxiang.business.user.acct.common.vo.withdrawal.WithdrawalCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Transactional(timeout = 10)
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
    public Withdrawal confirm(String id) {
        Withdrawal withdrawal = Optional.ofNullable(withdrawalRepository.findOne(id))
                .orElseThrow(() -> new ServiceException("找不到对应的提现单,ID:" + id));
        withdrawal.confirm();
        return withdrawalRepository.save(withdrawal);
    }
}
