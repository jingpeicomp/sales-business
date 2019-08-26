package com.jingxiang.business.tc.order;

import com.jingxiang.business.api.order.OrderCallbackApi;
import com.jingxiang.business.api.payment.PaymentPaidRequest;
import com.jingxiang.business.consts.Role;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.product.common.vo.SkuVo;
import com.jingxiang.business.product.goods.SkuService;
import com.jingxiang.business.tc.common.consts.OrderConsts;
import com.jingxiang.business.tc.common.consts.OrderStatus;
import com.jingxiang.business.tc.common.vo.order.*;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentCreateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentOperateRequest;
import com.jingxiang.business.user.acct.common.vo.payment.PaymentVo;
import com.jingxiang.business.user.acct.pay.PayService;
import com.jingxiang.business.user.uc.common.vo.shop.ShopVo;
import com.jingxiang.business.user.uc.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单业务类
 * Created by liuzhaoming on 2019/8/3.
 */
@Service
@Slf4j
public class OrderService implements OrderCallbackApi {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuService skuService;

    @Autowired
    private PayService paymentService;

    @Autowired
    private ShopService shopService;

    /**
     * 买家下单接口
     *
     * @param request 下单请求
     * @return 订单
     */
    @Transactional(timeout = 10)
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
            ShopVo shop = shopService.queryVoById(order.getShopId())
                    .orElseThrow(() -> new ServiceException("找不到对应的店铺,ID:" + order.getShopId()));
            PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                    .payer(order.getBuyer())
                    .payee(shop.getOwner())
                    .sourceId(order.getId())
                    .payAmount(order.getAmount().getTotalPayPrice())
                    .payType(order.getPayment().getPayType())
                    .shopId(order.getShopId())
                    .title(order.calculateTitle())
                    .source(PaymentSource.ORDER_PAY)
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
    @Transactional(timeout = 10)
    public Order close(OrderOperateRequest request) {
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
    @Transactional(timeout = 10)
    public Order pay(OrderOperateRequest request) {
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
     */
    @Override
    @Transactional(timeout = 10)
    public void paid(PaymentPaidRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getSourceId(), request.getShopId());
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
            order.updatePayment(request);
            orderRepository.save(order);
        }
    }

    /**
     * 卖家发货
     *
     * @param request 订单操作请求
     * @return 订单发货
     */
    @Transactional(timeout = 10)
    public Order deliver(OrderOperateRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getOrderId(), request.getShopId());
        if (!optional.isPresent()) {
            log.error("Deliver order fail, because cannot find order {}", request);
            throw new IllegalArgumentException("订单无法发货，因为找不到对应的订单" + request);
        }

        Order order = optional.get();
        order.deliver(Role.SELLER);
        order.initShipTime();
        return orderRepository.save(order);
    }

    /**
     * 确认收货
     *
     * @param request 订单操作请求
     * @return 订单确认收货
     */
    @Transactional(timeout = 10)
    public Order confirm(OrderOperateRequest request) {
        Optional<Order> optional = orderRepository.findByIdAndShopId(request.getOrderId(), request.getShopId());
        if (!optional.isPresent()) {
            log.error("Confirm order fail, because cannot find order {}", request);
            throw new IllegalArgumentException("订单确认收货，因为找不到对应的订单" + request);
        }

        Order order = optional.get();
        order.confirm(request.getRole());
        order.setConfirmTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 根据查询条件查询订单
     *
     * @param condition 订单查询条件
     * @return 符合条件的订单列表
     */
    @Transactional(timeout = 10, readOnly = true)
    public Page<OrderVo> query(OrderQueryCondition condition) {
        Pageable pageRequest = new PageRequest(condition.getPage(), condition.getSize(), Sort.Direction.DESC, "createTime");
        OrderStatus orderStatus = OrderStatus.fromValue(condition.getOrderStatus());
        Page<Order> orderPage;
        if (StringUtils.isNotBlank(condition.getShopId())) {
            orderPage = querySellerOrderByStatus(condition.getShopId(), orderStatus, pageRequest);
        } else {
            orderPage = queryBuyerOrderByStatus(condition.getBuyer(), orderStatus, pageRequest);
        }

        return orderPage.map(Order::toVo);
    }

    /**
     * 查询需要自动关闭的订单列表
     *
     * @return 需要关闭的订单列表
     */
    @Transactional(timeout = 10, readOnly = true)
    public List<Order> queryNeedAutoCloseOrders() {
        LocalDateTime datelineTime = LocalDateTime.now().plusSeconds(OrderConsts.ORDER_AUTO_CLOSE_TIME_IN_SECONDS);
        return orderRepository.findNeedAutoCloseOrders(datelineTime);
    }

    /**
     * 查询需要自动确认收货的订单列表
     *
     * @return 需要自动确认收货的订单列表
     */
    @Transactional(timeout = 10, readOnly = true)
    public List<Order> queryNeedAutoConfirmOrders() {
        return orderRepository.findNeedAutoConfirmOrders(LocalDateTime.now());
    }

    /**
     * 根据订单状态查询买家订单
     *
     * @param buyer       买家
     * @param orderStatus 订单状态
     * @param pageRequest 分页请求
     * @return 订单列表
     */
    private Page<Order> queryBuyerOrderByStatus(String buyer, OrderStatus orderStatus, Pageable pageRequest) {
        switch (orderStatus) {
            case WAIT_PAY:
                return orderRepository.findBuyerWaitPayOrders(buyer, pageRequest);
            case WAIT_SHIP:
                return orderRepository.findBuyerWaitShipOrders(buyer, pageRequest);
            case WAIT_RECEIVE:
                return orderRepository.findBuyerWaitReceiveOrders(buyer, pageRequest);
            case RECEIVED:
                return orderRepository.findBuyerCompleteOrders(buyer, pageRequest);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }

    /**
     * 根据订单状态查询卖家订单
     *
     * @param shopId      店铺编号
     * @param orderStatus 订单状态
     * @param pageRequest 分页请求
     * @return 订单列表
     */
    private Page<Order> querySellerOrderByStatus(String shopId, OrderStatus orderStatus, Pageable pageRequest) {
        switch (orderStatus) {
            case WAIT_SHIP:
                return orderRepository.findSellerWaitDealOrders(shopId, pageRequest);
            case WAIT_RECEIVE:
                return orderRepository.findSellerWaitReceiveOrders(shopId, pageRequest);
            case RECEIVED:
                return orderRepository.findSellerCompleteOrders(shopId, pageRequest);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }
}
