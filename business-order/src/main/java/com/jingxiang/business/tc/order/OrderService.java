package com.jingxiang.business.tc.order;

import com.jingxiang.business.acct.pay.PaymentService;
import com.jingxiang.business.consts.Role;
import com.jingxiang.business.product.base.vo.SkuVo;
import com.jingxiang.business.product.goods.SkuService;
import com.jingxiang.business.tc.common.consts.OrderConsts;
import com.jingxiang.business.tc.common.vo.order.OrderCloseRequest;
import com.jingxiang.business.tc.common.vo.order.OrderCreateRequest;
import com.jingxiang.business.tc.common.vo.order.OrderPayRequest;
import com.jingxiang.business.tc.common.vo.order.OrderProductParam;
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
    private PaymentService paymentService;

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
        orderRepository.save(order);

        if (order.needPay()) {
            //创建支付单
            paymentService.create();
            order.pay(Role.BUYER);
            return orderRepository.save(order);
        }
        return order;
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
            paymentService.cancel();
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

    }
}
