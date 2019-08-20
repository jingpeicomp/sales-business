package com.jingxiang.business.tc.order;

import com.jingxiang.business.acct.common.vo.address.PaymentCreateRequest;
import com.jingxiang.business.acct.common.vo.address.PaymentOperateRequest;
import com.jingxiang.business.acct.common.vo.address.PaymentVo;
import com.jingxiang.business.acct.pay.PayService;
import com.jingxiang.business.consts.Role;
import com.jingxiang.business.product.base.vo.SkuVo;
import com.jingxiang.business.product.goods.SkuService;
import com.jingxiang.business.tc.common.consts.OrderConsts;
import com.jingxiang.business.tc.common.vo.order.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单业务类
 * Created by liuzhaoming on 2019/8/3.
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuService skuService;

    @Autowired
    private PayService paymentService;

    /**
     * 买家下单接口
     *
     * @param request 下单请求
     * @return 订单
     */
    public Order create(OrderCreateRequest request) {
        List<String> skuIds = request.getProducts().stream()
                .map(OrderProductParam::getSkuId)
                .collect(Collectors.toList());
        List<SkuVo> voList = skuService.queryVo(request.getShopId(), skuIds);
        request.checkOrderProduct(voList);
        request.updateProductInfo(voList);

        Order order = Order.from(request);
        order.create(Role.BUYER);
        order.initAutoTime(OrderConsts.ORDER_AUTO_CONFIRM_TIME_IN_SECONDS);

        if (order.needPay()) {
            //创建支付单
            PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                    .buyer(order.getBuyer())
                    .orderId(order.getId())
                    .payAmount(order.getAmount().getTotalPayPrice())
                    .payType(order.getPayment().getPayType())
                    .shopId(order.getShopId())
                    .title(order.calculateTitle())
                    .build();
            PaymentVo paymentVo = paymentService.create(createRequest);
            order.updatePayment(paymentVo);
        }
        return orderRepository.save(order);
    }

    /**
     * 关闭订单
     *
     * @param request 订单关闭请求
     * @return 订单
     */
    public Order close(OrderCloseRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getOrderId(), request.getShopId());
        if (!optional.isPresent()) {
            log.error("Close order fail, because cannot find order {}", request);
            throw new IllegalArgumentException("无法关闭订单，因为找不到对应的订单" + request);
        }

        Order order = optional.get();
        order.close(request.getRole());
        if (order.needPay()) {
            PaymentOperateRequest operateRequest = PaymentOperateRequest.builder()
                    .paymentId(order.getPayment().getPayId())
                    .shopId(order.getShopId())
                    .build();
            paymentService.cancel(operateRequest);
        }
        return orderRepository.save(order);
    }

    /**
     * 订单支付
     *
     * @param request 订单支付请求
     * @return 订单
     */
    public Order pay(OrderPayRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getOrderId(), request.getShopId());
        if (!optional.isPresent()) {
            log.error("Pay order fail, because cannot find order {}", request);
            throw new IllegalArgumentException("无法支付订单，因为找不到对应的订单" + request);
        }

        Order order = optional.get();
        if (order.needPay()) {
            order.pay(Role.BUYER);
            PaymentOperateRequest operateRequest = PaymentOperateRequest.builder()
                    .paymentId(order.getPayment().getPayId())
                    .shopId(order.getShopId())
                    .build();
            PaymentVo paymentVo = paymentService.pay(operateRequest);
            order.updatePayment(paymentVo);
            return orderRepository.save(order);
        }

        return order;
    }

    /**
     * 订单支付结果回调
     *
     * @param request 订单支付结果回调
     * @return 订单
     */
    public Order paid(OrderPaidRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getOrderId(), request.getShopId());
        if (!optional.isPresent()) {
            log.error("Modify order paid result fail, because cannot find order {}", request);
            throw new IllegalArgumentException("无法修改支付结果，因为找不到对应的订单" + request);
        }

        Order order = optional.get();
        if (order.needPay()) {
            if (request.isSuccessful()) {
                order.paidSuccess(request.getRole());
            } else {
                order.paidFail(request.getRole());
            }
            order.updatePayment(request.getVo());
            return orderRepository.save(order);
        }

        return order;
    }
}
