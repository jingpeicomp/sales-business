package com.jingxiang.business.tc.order;

import com.jingxiang.business.consts.PayStatus;
import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.consts.Role;
import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.tc.common.consts.*;
import com.jingxiang.business.tc.common.vo.order.OrderCreateRequest;
import com.jingxiang.business.tc.configuration.OrderFsmFactory;
import com.jingxiang.business.tc.fsm.Fsm;
import com.jingxiang.business.tc.fsm.FsmContext;
import com.jingxiang.business.tc.fsm.FsmState;
import com.jingxiang.business.tc.fsm.FsmTransitionResult;
import com.jingxiang.business.vo.Describable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单实体
 * Created by liuzhaoming on 2019/8/3.
 */
@Entity
@Table(name = "T_BIZ_TC_ORDER")
@Data
public class Order implements Serializable, Describable {
    /**
     * 订单编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "varchar(32) not null comment '订单编号'")
    private String id;

    /**
     * 店铺ID
     */
    @Column(name = "SHOP_ID", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '店铺编号'")
    private String shopId;

    /**
     * 买家
     */
    @Column(name = "BUYER", nullable = false, updatable = false, columnDefinition = "varchar(32) not null comment '买家编号'")
    private String buyer;

    /**
     * 订单类型
     */
    @Column(name = "ORDER_TYPE", nullable = false, updatable = false, columnDefinition = "smallint comment '订单类型'")
    @Convert(converter = OrderType.EnumConvert.class)
    private OrderType type = OrderType.NORMAL;

    /**
     * 支付状态
     */
    @Column(name = "PAY_STATUS", nullable = false, columnDefinition = "smallint comment '订单支付状态'")
    @Convert(converter = PayStatus.EnumConvert.class)
    private PayStatus payStatus;

    /**
     * 订单完成状态
     */
    @Column(name = "COMPLETE_STATUS", nullable = false, columnDefinition = "smallint comment '订单完成状态'")
    @Convert(converter = CompleteStatus.EnumConvert.class)
    private CompleteStatus completeStatus;

    /**
     * 订单发货状态
     */
    @Column(name = "SHIP_STATUS", nullable = false, columnDefinition = "smallint comment '订单发货状态'")
    @Convert(converter = ShipStatus.EnumConvert.class)
    private ShipStatus shipStatus;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", updatable = false, columnDefinition = "datetime not null default CURRENT_TIMESTAMP comment '创建时间'")
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "UPDATE_TIME", columnDefinition = "datetime comment '修改时间'")
    private LocalDateTime updateTime;

    /**
     * 订单成功时间
     */
    @Column(name = "FINISH_TIME", columnDefinition = "datetime comment '订单成功时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime finishTime;

    /**
     * 自动关闭订单时间
     */
    @Column(name = "AUTO_CLOSE_TIME", columnDefinition = "datetime comment '自动关闭订单时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime autoCloseTime;

    /**
     * 卖家发货时间
     */
    @Column(name = "SHIP_TIME", columnDefinition = "datetime comment '卖家发货时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime shipTime;

    /**
     * 确认收货自动时间
     */
    @Column(name = "AUTO_CONFIRM_TIME", columnDefinition = "datetime comment '确认收货自动时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime autoConfirmTime;

    /**
     * 自动确认收货时间（秒）
     */
    @Column(name = "AUTO_CONFIRM_SECONDS", columnDefinition = "int comment '自动确认收货时间（秒）'")
    private Integer autoConfirmSeconds;

    /**
     * 订单账务信息
     */
    @Embedded
    private OrderAmount amount;

    /**
     * 收货人
     */
    @Embedded
    private OrderReceiver receiver;

    /**
     * 支付信息
     */
    @Embedded
    private OrderPayment payment;

    /**
     * 订单商品条目
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    private List<OrderProduct> products;

    /**
     * 版本号
     */
    @Version
    @Column(name = "VERSION", columnDefinition = "bigint comment '版本号'")
    private Long version;

    /**
     * 设置订单商品信息
     *
     * @param products 订单商品信息
     */
    public void setProducts(List<OrderProduct> products) {
        products.forEach(product -> {
            product.setOrderId(id);
            product.setShopId(shopId);
            product.setBuyer(buyer);
        });
        this.products = products;
    }

    /**
     * id
     *
     * @return id
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * 订单创建
     *
     * @param role 角色
     */
    public FsmTransitionResult create(Role role) {
        return transit(role, OrderFsmEventNames.CREATE);
    }

    /**
     * 关闭订单
     *
     * @param role 角色
     */
    public FsmTransitionResult close(Role role) {
        return transit(role, OrderFsmEventNames.CLOSE);
    }

    /**
     * 支付订单
     *
     * @param role 角色
     */
    public FsmTransitionResult pay(Role role) {
        return transit(role, OrderFsmEventNames.PAY);
    }

    /**
     * 订单发货
     *
     * @param role 角色
     */
    public FsmTransitionResult deliver(Role role) {
        return transit(role, OrderFsmEventNames.DELIVER);
    }

    /**
     * 确认收货
     *
     * @param role 角色
     */
    public FsmTransitionResult confirm(Role role) {
        return transit(role, OrderFsmEventNames.CONFIRM);
    }

    /**
     * 是否需要支付，订单应付金额是否大于0
     *
     * @return true 是， false 否
     */
    public boolean needPay() {
        return amount.getTotalPayPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 设置自动关闭和确认收货时间
     *
     * @param autoConfirmSeconds 多长时间（秒）后自动确认收货
     */
    public void initAutoTime(int autoConfirmSeconds) {
        this.autoConfirmSeconds = autoConfirmSeconds;
        this.autoCloseTime = LocalDateTime.now().plusSeconds(OrderConsts.ORDER_AUTO_CLOSE_TIME_IN_SECONDS);
    }

    /**
     * 构造订单实体数据
     *
     * @param request 下单请求
     * @return 订单实体数据
     */
    public static Order from(OrderCreateRequest request) {
        Order order = new Order();
        order.setId(IdFactory.createTcId(OrderConsts.ID_PREFIX_ORDER));
        order.setShopId(request.getShopId());
        order.setBuyer(request.getBuyer());
        order.setPayStatus(PayStatus.UNPAID);
        order.setCompleteStatus(CompleteStatus.UNCREATED);
        order.setShipStatus(ShipStatus.UNSHIPPED);

        OrderReceiver receiver = OrderReceiver.from(request.getReceiver());
        order.setReceiver(receiver);

        OrderPayment payment = new OrderPayment();
        //现在只支持微信支付
        payment.setPayType(PayType.WEIXIN);
        order.setPayment(payment);

        List<OrderProduct> products = request.getProducts().stream()
                .map(OrderProduct::from)
                .collect(Collectors.toList());
        order.setProducts(products);

        OrderAmount amount = new OrderAmount();
        //邮费为0
        amount.setShipPrice(BigDecimal.ZERO);
        order.setAmount(amount);
        order.calculateAmount();
        return order;
    }

    /**
     * 计算订单金额
     */
    private void calculateAmount() {
        products.forEach(OrderProduct::calculateAmount);
        BigDecimal totalItemPrice = products.stream()
                .map(OrderProduct::getTotalBuyPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        amount.setTotalItemPrice(totalItemPrice);
        BigDecimal totalPrice = totalItemPrice.add(amount.getShipPrice());
        amount.setTotalPrice(totalPrice);
        amount.setTotalPayPrice(totalPrice);
    }

    /**
     * 订单状态迁移
     *
     * @param role      执行角色
     * @param eventName 时间名称
     * @return 状态迁移结果
     */
    private FsmTransitionResult transit(Role role, String eventName) {
        FsmContext<Order> context = buildContext(role);
        Fsm fsm = getFsm();
        FsmTransitionResult result = fsm.sendEvent(context, eventName);
        updateByFsmState(result.getToState());
        return result;
    }

    /**
     * 构造有限状态机上下文
     *
     * @param role 角色
     * @return 有限状态机上下文
     */
    @SuppressWarnings("unchecked")
    private FsmContext<Order> buildContext(Role role) {
        return (FsmContext) FsmContext.builder()
                .roleName(role.getDisplay())
                .target(this)
                .fromState(buildCurrentFsmState())
                .build();
    }

    /**
     * 构造当前订单有限状态机状态
     *
     * @return 订单状态
     */
    private FsmState buildCurrentFsmState() {
        if (null == completeStatus) {
            throw new IllegalArgumentException("订单完成状态不能为空");
        }
        if (null == payStatus) {
            throw new IllegalArgumentException("订单支付状态不能为空");
        }
        if (null == shipStatus) {
            throw new IllegalArgumentException("订单发货状态不能为空");
        }

        String[] subStates = new String[]{completeStatus.getDisplay(), payStatus.getDisplay(), shipStatus.getDisplay()};
        // 根据子状态生成当前状态
        return FsmState.builder()
                .subStates(subStates)
                .build();
    }

    /**
     * 获取订单有限状态机
     *
     * @return 订单有限状态机
     */
    private Fsm getFsm() {
        return Optional.ofNullable(OrderFsmFactory.getFsm(type.name()))
                .orElseThrow(() -> new IllegalArgumentException("FSM不支持订单类型:" + type));
    }

    /**
     * FSM迁移状态设置
     *
     * @param toState fsm状态
     */
    private void updateByFsmState(FsmState toState) {
        String[] subStates = toState.getSubStates();
        completeStatus = CompleteStatus.fromDisplay(subStates[0]);
        payStatus = PayStatus.fromDisplay(subStates[1]);
        shipStatus = ShipStatus.fromDisplay(subStates[2]);
    }
}
