package com.jingxiang.business.tc.order;

import com.jingxiang.business.product.goods.SkuRepository;
import com.jingxiang.business.tc.OrderTestApplication;
import com.jingxiang.business.tc.common.consts.OrderStatus;
import com.jingxiang.business.tc.common.vo.order.OrderProductVo;
import com.jingxiang.business.tc.common.vo.order.OrderQueryCondition;
import com.jingxiang.business.tc.common.vo.order.OrderVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 订单业务单元测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderTestApplication.class)
public class OrderServiceTest {

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository repository;

    @Test
    public void create() {
    }

    @Test
    public void close() {
    }

    @Test
    public void pay() {
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
        assertThat(order.getAmount(), is("111.44"));
        assertThat(order.getId(), is("T0001"));
        assertThat(order.getProducts(), hasSize(2));
        assertThat(order.getProducts().stream().map(OrderProductVo::getId).collect(Collectors.toList()),
                contains("TP0001", "TP0002"));
        assertThat(order.getProducts().stream().map(OrderProductVo::getBuyPrice).collect(Collectors.toList()),
                contains(BigDecimal.valueOf(100), BigDecimal.valueOf(11.44D)));
    }

    @Test
    public void queryNeedAutoCloseOrders() {
    }

    @Test
    public void queryNeedAutoConfirmOrders() {
    }
}