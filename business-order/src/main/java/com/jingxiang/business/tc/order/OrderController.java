package com.jingxiang.business.tc.order;

import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.tc.common.vo.order.OrderCreateRequest;
import com.jingxiang.business.tc.common.vo.order.OrderOperateRequest;
import com.jingxiang.business.tc.common.vo.order.OrderQueryCondition;
import com.jingxiang.business.tc.common.vo.order.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 订单Controller
 * Created by liuzhaoming on 2019/8/21.
 */
@RestController
@RequestMapping("/api/business/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询卖家订单
     *
     * @param shopId    店铺ID
     * @param condition 查询条件
     * @return 卖家订单列表
     */
    @RequestMapping(path = "/{shopId}/orders", method = RequestMethod.GET)
    public Page<OrderVo> querySellerOrders(@PathVariable String shopId, OrderQueryCondition condition) {
        condition.setShopId(shopId);
        condition.setBuyer(null);
        return orderService.query(condition);
    }

    /**
     * 查询买家订单
     *
     * @param condition 查询条件
     * @return 买家订单列表
     */
    @RequestMapping(path = "/orders", method = RequestMethod.GET)
    public Page<OrderVo> queryBuyerOrders(OrderQueryCondition condition) {
        condition.setShopId(null);
        return orderService.query(condition);
    }

    /**
     * 创建订单
     *
     * @param shopId  店铺ID
     * @param request 订单创建参数
     * @return order
     */
    @RequestMapping(path = "/{shopId}/orders", method = RequestMethod.POST)
    public OrderVo create(@PathVariable String shopId, OrderCreateRequest request) {
        request.setShopId(shopId);
        return Optional.ofNullable(orderService.create(request))
                .map(Order::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to create order, shopID:{},  request:{}", shopId, request);
                    return new ServiceException("Fail to create order, shopId:" + shopId);
                });
    }

    /**
     * 支付订单
     *
     * @param shopId  店铺ID
     * @param orderId 订单ID
     * @return order
     */
    @RequestMapping(path = "/{shopId}/orders/{orderId}/pay", method = RequestMethod.PUT)
    public OrderVo pay(@PathVariable String shopId, @PathVariable String orderId) {
        OrderOperateRequest request = new OrderOperateRequest();
        request.setOrderId(orderId);
        request.setShopId(shopId);
        return Optional.ofNullable(orderService.pay(request))
                .map(Order::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to pay order, shopID:{}, orderId:{}  request:{}", shopId, orderId, request);
                    return new ServiceException("Fail to pay order, shopId:" + shopId + ", orderId:" + orderId);
                });
    }

    /**
     * 订单发货
     *
     * @param shopId  店铺ID
     * @param orderId 订单ID
     * @return order
     */
    @RequestMapping(path = "/{shopId}/orders/{orderId}/deliver", method = RequestMethod.PUT)
    public OrderVo deliver(@PathVariable String shopId, @PathVariable String orderId) {
        OrderOperateRequest request = new OrderOperateRequest();
        request.setOrderId(orderId);
        request.setShopId(shopId);
        return Optional.ofNullable(orderService.deliver(request))
                .map(Order::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to deliver order, shopID:{}, orderId:{}  request:{}", shopId, orderId, request);
                    return new ServiceException("Fail to deliver order, shopId:" + shopId + ", orderId:" + orderId);
                });
    }

    /**
     * 确认收货
     *
     * @param shopId  店铺ID
     * @param orderId 订单ID
     * @return order
     */
    @RequestMapping(path = "/{shopId}/orders/{orderId}/confirm", method = RequestMethod.PUT)
    public OrderVo confirm(@PathVariable String shopId, @PathVariable String orderId) {
        OrderOperateRequest request = new OrderOperateRequest();
        request.setOrderId(orderId);
        request.setShopId(shopId);
        return Optional.ofNullable(orderService.confirm(request))
                .map(Order::toVo)
                .orElseThrow(() -> {
                    log.error("Fail to confirm order, shopID:{}, orderId:{}  request:{}", shopId, orderId, request);
                    return new ServiceException("Fail to confirm order, shopId:" + shopId + ", orderId:" + orderId);
                });
    }
}
