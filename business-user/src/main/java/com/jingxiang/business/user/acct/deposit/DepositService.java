package com.jingxiang.business.user.acct.deposit;

import com.jingxiang.business.api.payment.PaymentPaidRequest;
import com.jingxiang.business.exception.ResourceNotFindException;
import com.jingxiang.business.user.acct.common.consts.CompleteStatus;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import com.jingxiang.business.user.acct.common.vo.deposit.DepositCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentOperateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.user.acct.pay.PayService;
import com.jingxiang.business.user.uc.common.consts.UserConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 充值单业务类
 * Created by liuzhaoming on 2019/8/26.
 */
@Service
@Slf4j
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private PayService payService;

    /**
     * 充值支付单名称
     */
    @Value("${jingxiang.business.user.sfDepositName:服务费充值}")
    private String sfDepositName;

    /**
     * 创建充值单
     *
     * @param request 充值单创建请求
     * @return 充值单
     */
    @Transactional(timeout = 10)
    public Deposit create(DepositCreateRequest request) {
        Deposit deposit = Deposit.from(request);
        depositRepository.save(deposit);
        PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                .payer(request.getUserId())
                .payee(UserConsts.ID_SHOP_SYSTEM_OWNER)
                .sourceId(deposit.getId())
                .description("")
                .payAmount(deposit.getPayAmount())
                .payType(deposit.getPayType())
                .shopId(UserConsts.ID_SHOP_SYSTEM)
                .source(PaymentSource.SF_DEPOSIT)
                .title(sfDepositName)
                .build();
        PaymentVo payment = payService.create(paymentCreateRequest);
        log.info("Deposit {} create payment {}", deposit.getId(), payment);
        deposit.updatePayment(payment);
        depositRepository.save(deposit);
        return deposit;
    }

    /**
     * 充值单支付
     *
     * @param id 充值单 ID
     * @return 充值单
     */
    @Transactional(timeout = 10)
    public Deposit pay(String id, String userId) {
        Deposit deposit = depositRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFindException("找不到对应的充值单"));
        PaymentOperateRequest operateRequest = PaymentOperateRequest.builder()
                .paymentId(id)
                .shopId(UserConsts.ID_SHOP_SYSTEM)
                .build();
        PaymentVo paymentVo = payService.pay(operateRequest);
        deposit.updatePayment(paymentVo);
        return depositRepository.save(deposit);
    }

    /**
     * 充值单支付结果回调
     *
     * @param request 订单支付结果回调
     */
    @Transactional(timeout = 10)
    public void paid(PaymentPaidRequest request) {
        Deposit deposit = depositRepository.findOne(request.getSourceId());
        if (null == deposit) {
            log.error("Modify deposit paid result fail, because cannot find deposit {}", request);
            throw new IllegalArgumentException("无法修改支付结果，因为找不到对应的充值单" + request);
        }

        deposit.updatePayment(request);
        depositRepository.save(deposit);
    }

    /**
     * 根据用户ID和完成状态查询充值单
     *
     * @param userId   用户ID
     * @param status   支付单完成状态
     * @param pageable 分页信息
     * @return 支付单列表
     */
    @Transactional(timeout = 10, readOnly = true)
    public Page<Deposit> query(String userId, CompleteStatus status, Pageable pageable) {
        return depositRepository.findByUserIdAndCompleteStatusOrderByFinishTimeDesc(userId, status, pageable);
    }

    /**
     * 根据ID查询充值单
     *
     * @param id 充值单ID
     * @return 充值单详情
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<Deposit> queryById(String id) {
        return Optional.ofNullable(depositRepository.findOne(id));
    }

    /**
     * 充值单支付
     *
     * @param deposit 充值单
     * @return 充值单
     */
    private Deposit pay(Deposit deposit) {
        PaymentOperateRequest operateRequest = PaymentOperateRequest.builder()
                .paymentId(deposit.getPayId())
                .shopId(UserConsts.ID_SHOP_SYSTEM)
                .build();
        PaymentVo paymentVo = payService.pay(operateRequest);
        deposit.updatePayment(paymentVo);
        return depositRepository.save(deposit);
    }
}
