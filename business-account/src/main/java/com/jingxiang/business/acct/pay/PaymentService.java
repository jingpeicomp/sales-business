package com.jingxiang.business.acct.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付接口
 * Created by liuzhaoming on 2019/8/8.
 */
@Service
@Slf4j
public class PaymentService {

    /**
     * 创建支付单
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public Payment create() {
        return null;
    }

    /**
     * 取消支付单
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public Payment cancel() {
        return null;
    }

    /**
     * 支付
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public Payment pay() {
        return null;
    }
}
