package com.jingxiang.business.tc.task;

import com.jingxiang.business.consts.Role;
import com.jingxiang.business.tc.common.vo.order.OrderOperateRequest;
import com.jingxiang.business.tc.order.Order;
import com.jingxiang.business.tc.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单定时任务
 * Created by liuzhaoming on 2019/8/21.
 */
@EnableScheduling
@Component
@Slf4j
public class OrderTaskScheduler {

    @Autowired
    private OrderService orderService;

    /**
     * 自动关闭订单
     */
    @Scheduled(cron = "${jingxiang.business.order.autoCloseCron:0 0/1 * * * ?}")
    public void doAutoClose() {
        List<Order> orders = orderService.queryNeedAutoCloseOrders();
        orders.forEach(order -> {
            OrderOperateRequest request = new OrderOperateRequest();
            request.setShopId(order.getShopId());
            request.setOrderId(order.getId());
            request.setRole(Role.TIMER);
            orderService.close(request);
        });
    }


    /**
     * 订单自动收货
     */
    @Scheduled(cron = "${jingxiang.business.order.autoConfirmCron:20 0/5 * * * ?}")
    public void doAutoConfirm() {
        List<Order> orders = orderService.queryNeedAutoConfirmOrders();
        orders.forEach(order -> {
            OrderOperateRequest request = new OrderOperateRequest();
            request.setShopId(order.getShopId());
            request.setOrderId(order.getId());
            request.setRole(Role.TIMER);
            orderService.confirm(request);
        });
    }
}
