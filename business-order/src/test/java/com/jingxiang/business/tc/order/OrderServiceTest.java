package com.jingxiang.business.tc.order;

import com.jingxiang.business.consts.Role;
import com.jingxiang.business.tc.common.consts.CompleteStatus;
import com.jingxiang.business.tc.common.consts.OrderStatus;
import com.jingxiang.business.tc.common.consts.PayStatus;
import com.jingxiang.business.tc.common.consts.ShipStatus;
import com.jingxiang.business.tc.common.vo.order.*;
import com.jingxiang.business.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 订单业务单元测试
 */
@TestPropertySource(locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository repository;

    @Test
    public void create() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setBuyer("U002");
        request.setShopId("S001");
        request.setReceiver(OrderReceiverVo.builder().name("张三").province("江苏").build());
        OrderProductParam productParam1 = OrderProductParam.builder().skuId("K0001").skuNum(BigDecimal.valueOf(2)).build();
        OrderProductParam productParam2 = OrderProductParam.builder().skuId("K0002").skuNum(BigDecimal.valueOf(1)).build();
        request.setProducts(Arrays.asList(productParam1, productParam2));
        Order order = orderService.create(request);
        log.info("Create order is {}", CommonUtils.toJson(order));
        assertThat(order.getAmount().getTotalPayPrice(), is(BigDecimal.valueOf(28.99D).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(35.10D))));
        assertThat(order.getCompleteStatus(), is(CompleteStatus.DOING));
        assertThat(order.getPayStatus(), is(PayStatus.UNPAID));
        assertThat(order.getShipStatus(), is(ShipStatus.UNSHIPPED));
        OrderProduct product1 = order.getProducts().stream()
                .filter(product -> product.getSkuId().equals("K0001"))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(product1.getBuyer(), is("U002"));
        assertThat(product1.getBuyPrice(), is(BigDecimal.valueOf(28.99D)));
        assertThat(product1.getOrderId(), is(order.getId()));
        assertThat(product1.getSkuNum(), is(BigDecimal.valueOf(2)));
        assertThat(product1.getTotalBuyPrice(), is(BigDecimal.valueOf(28.99D).multiply(BigDecimal.valueOf(2))));
        OrderProduct product2 = order.getProducts().stream()
                .filter(product -> product.getSkuId().equals("K0002"))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        assertThat(product2.getBuyer(), is("U002"));
        assertThat(product2.getBuyPrice(), is(BigDecimal.valueOf(35.10D).setScale(2, BigDecimal.ROUND_HALF_UP)));
        assertThat(product2.getOrderId(), is(order.getId()));
        assertThat(product2.getSkuNum(), is(BigDecimal.valueOf(1)));
        assertThat(product2.getTotalBuyPrice(), is(BigDecimal.valueOf(35.10D).setScale(2, BigDecimal.ROUND_HALF_UP)));
        repository.delete(order.getId());
    }

    @Test
    public void close() {
        OrderOperateRequest request = OrderOperateRequest.builder().orderId("T0001").role(Role.BUYER).shopId("S001").build();
        Order order = orderService.close(request);
        log.info("Close order is {}", CommonUtils.toJson(order));
        assertThat(order.getAmount().getTotalPayPrice(), is(BigDecimal.valueOf(100.00D).add(BigDecimal.valueOf(11.44D))));
        assertThat(order.getCompleteStatus(), is(CompleteStatus.CANCELED));
        assertThat(order.getPayStatus(), is(PayStatus.UNPAID));
        assertThat(order.getShipStatus(), is(ShipStatus.UNSHIPPED));
    }

    @Test
    public void pay() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setBuyer("U002");
        request.setShopId("S001");
        request.setReceiver(OrderReceiverVo.builder().name("张三").province("江苏").build());
        OrderProductParam productParam1 = OrderProductParam.builder().skuId("K0001").skuNum(BigDecimal.valueOf(2)).build();
        OrderProductParam productParam2 = OrderProductParam.builder().skuId("K0002").skuNum(BigDecimal.valueOf(1)).build();
        request.setProducts(Arrays.asList(productParam1, productParam2));
        Order order = orderService.create(request);
        log.info("Create order is {}", CommonUtils.toJson(order));
        OrderOperateRequest operateRequest = OrderOperateRequest.builder().orderId(order.getId()).role(Role.BUYER).shopId("S001").build();
        order = orderService.pay(operateRequest);
        assertThat(order.getAmount().getTotalPayPrice(), is(BigDecimal.valueOf(28.99D).multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(35.10D))));
        assertThat(order.getCompleteStatus(), is(CompleteStatus.DOING));
        assertThat(order.getPayStatus(), is(PayStatus.PAYING));
        assertThat(order.getShipStatus(), is(ShipStatus.UNSHIPPED));
    }

    @Test
    public void paid() {
    }

    @Test
    public void deliver() {
    }

    @Test
    public void confirm() {
    }

    @Test
    public void query() {
        OrderQueryCondition condition = OrderQueryCondition.builder()
                .buyer("U001")
                .orderStatus(OrderStatus.WAIT_PAY.getValue())
                .build();
        Page<OrderVo> orderPage = orderService.query(condition);
        assertThat(orderPage.getTotalElements(), is(1L));
        OrderVo order = orderPage.getContent().get(0);
        assertThat(order.getAmount().getTotalPrice(), is(BigDecimal.valueOf(111.44D)));
        assertThat(order.getId(), is("T0001"));
        assertThat(order.getProducts(), hasSize(2));
        assertThat(order.getProducts().stream().map(OrderProductVo::getId).collect(Collectors.toList()),
                contains("TP0001", "TP0002"));
        assertThat(order.getProducts().stream().map(OrderProductVo::getBuyPrice).collect(Collectors.toList()),
                contains(BigDecimal.valueOf(100.00D).setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.valueOf(11.44D)));
    }

    @Test
    public void queryNeedAutoCloseOrders() {
    }

    @Test
    public void queryNeedAutoConfirmOrders() {
    }
}