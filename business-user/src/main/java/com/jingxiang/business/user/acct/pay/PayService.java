package com.jingxiang.business.user.acct.pay;

import com.jingxiang.business.api.order.OrderCallbackApi;
import com.jingxiang.business.api.payment.PaymentPaidRequest;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.consts.Role;
import com.jingxiang.business.exception.NotFindException;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.user.acct.account.AccountService;
import com.jingxiang.business.user.acct.adapter.wechat.WxpayNotifyRequest;
import com.jingxiang.business.user.acct.adapter.wechat.WxpayService;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentOperateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.user.acct.deposit.DepositService;
import com.jingxiang.business.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付接口
 * Created by liuzhaoming on 2019/8/8.
 */
@Service
@Slf4j
public class PayService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WxpayService wxpayService;

    @Autowired
    private OrderCallbackApi orderApi;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepositService depositService;

    /**
     * 创建支付单
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public PaymentVo create(PaymentCreateRequest request) {
        Payment payment = Payment.from(request);
        return paymentRepository.save(payment).toVo();
    }

    /**
     * 取消支付单
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public PaymentVo cancel(PaymentOperateRequest request) {
        Payment payment = paymentRepository.findByIdAndShopId(request.getPaymentId(), request.getShopId())
                .orElseThrow(() -> new NotFindException("找不到对应的支付单,店铺ID：" + request.getShopId() + ",支付单ID：" + request.getPaymentId()));
        payment.cancel();
        return paymentRepository.save(payment).toVo();
    }

    /**
     * 支付
     *
     * @return 支付单
     */
    @Transactional(timeout = 10)
    public PaymentVo pay(PaymentOperateRequest request) {
        Payment payment = paymentRepository.findByIdAndShopId(request.getPaymentId(), request.getShopId())
                .orElseThrow(() -> new NotFindException("找不到对应的支付单,店铺ID：" + request.getShopId() + ",支付单ID：" + request.getPaymentId()));
        if (!payment.canPay()) {
            log.error("The payment cannot pay {}", payment);
            throw new ServiceException("支付单" + payment.getId() + "不符合支付条件");
        }

        if (payment.getPayType() == PayType.WEIXIN) {
            String prePlatformPayId = wxpayService.pay(payment);
            payment.setPrePlatformPayId(prePlatformPayId);
            return paymentRepository.save(payment).toVo();
        }
        throw new ServiceException("支付单" + payment.getId() + "不支持该支付类型");
    }

    /**
     * 根据支付单ID和订单ID查询支付单
     *
     * @param id      支付单ID
     * @param orderId 订单ID
     * @return 符合条件的支付单
     */
    @Transactional(timeout = 10, readOnly = true)
    public Optional<PaymentVo> queryByIdAndOrderId(String id, String orderId) {
        return paymentRepository.findByIdAndSourceId(id, orderId).map(Payment::toVo);
    }

    /**
     * 更新微信支付回调信息
     *
     * @param request 微信回调信息
     * @return 返回微信的回调响应结果
     */
    @Transactional(timeout = 10)
    public String updateWxpayNotification(WxpayNotifyRequest request) {
        Optional<Payment> paymentOptional = paymentRepository.findByIdAndSourceId(request.getPaymentId(), request.getOrderId());
        if (!paymentOptional.isPresent()) {
            log.error("Wxpay notify fail..... cannot find payment by id {} and order id {}", request.getPaymentId(), request.getOrderId());
            return wxpayService.buildFailNotifyResponse("Cannot find payment");
        }

        Payment payment = paymentOptional.get();
        BigDecimal dbAmount = payment.getPayAmount();
        if (!Objects.equals(request.getPayAmount(), CommonUtils.formatDownFee(dbAmount))) {
            log.error("Wxpay notify fail..... pay amount is invalid by id {} and order id {}, {}", request.getPaymentId(), request.getOrderId(), request);
            wxpayPaidFail(request, payment);
            return wxpayService.buildFailNotifyResponse("Pay amount is invalid");
        }

        wxpayPaidSuccessfully(request, payment);
        return wxpayService.buildSuccessNotifyResponse();
    }

    /**
     * 支付单微信支付成功
     *
     * @param request 微信回调请求
     * @param payment 支付单
     */
    private void wxpayPaidSuccessfully(WxpayNotifyRequest request, Payment payment) {
        payment.updateWxpaySuccessNotification(request);
        paymentRepository.save(payment);
        PaymentPaidRequest paidRequest = PaymentPaidRequest.builder()
                .paidAmount(payment.getPaidAmount())
                .sourceId(payment.getSourceId())
                .shopId(payment.getShopId())
                .successful(true)
                .paymentId(payment.getId())
                .platformPayId(payment.getPlatformPayId())
                .prePlatformPayId(payment.getPrePlatformPayId())
                .payTime(payment.getPayTime())
                .role(Role.BUYER)
                .build();
        if (payment.getSource() == PaymentSource.ORDER_PAY) {
            orderApi.paid(paidRequest);
            accountService.orderPaid(payment.toVo());
        } else if (payment.getSource() == PaymentSource.SF_DEPOSIT) {
            depositService.paid(paidRequest);
            accountService.depositPaid(payment.toVo());
        }
    }

    /**
     * 支付单微信支付失败
     *
     * @param request 微信回调请求
     * @param payment 支付单
     */
    private void wxpayPaidFail(WxpayNotifyRequest request, Payment payment) {
        payment.updateWxpayFailNotification(request);
        paymentRepository.save(payment);
        PaymentPaidRequest paidRequest = PaymentPaidRequest.builder()
                .paidAmount(payment.getPaidAmount())
                .sourceId(payment.getSourceId())
                .shopId(payment.getShopId())
                .successful(false)
                .paymentId(payment.getId())
                .platformPayId(payment.getPlatformPayId())
                .prePlatformPayId(payment.getPrePlatformPayId())
                .payTime(payment.getPayTime())
                .role(Role.BUYER)
                .message(request.getErrorMessage())
                .build();
        if (payment.getSource() == PaymentSource.ORDER_PAY) {
            orderApi.paid(paidRequest);
        } else if (payment.getSource() == PaymentSource.SF_DEPOSIT) {
            depositService.paid(paidRequest);
        }
    }
}
