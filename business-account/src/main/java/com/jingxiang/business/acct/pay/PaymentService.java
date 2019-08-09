package com.jingxiang.business.acct.pay;

import com.jingxiang.business.acct.common.vo.address.PaymentCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付接口
 * Created by liuzhaoming on 2019/8/8.
 */
@Service
@Slf4j
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * 创建支付单
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public Payment create(PaymentCreateRequest request) {
        Payment payment = Payment.from(request);
        return paymentRepository.save(payment);
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
