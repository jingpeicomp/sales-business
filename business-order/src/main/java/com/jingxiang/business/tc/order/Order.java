package com.jingxiang.business.tc.order;

import com.jingxiang.business.tc.base.configuration.OrderFsmFactory;
import com.jingxiang.business.tc.base.consts.*;
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
     * 支付时间
     */
    @Column(name = "PAY_TIME", columnDefinition = "datetime comment '支付时间'")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime payTime;

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
     * 物流费用
     */
    @Column(name = "SHIP_PRICE", columnDefinition = "decimal(10,2) comment '物流费用'")
    private BigDecimal shipPrice = BigDecimal.ZERO;

    /**
     * 扣除优惠（商品级和订单级）后的应付总额
     * <p>
     * <p>扣除商品级优惠后的应付总额 - 订单级优惠总金额 - 直降总金额；现在就是各个商品价格相加</p>
     */
    @Column(name = "TOTAL_ITEM_PRICE", columnDefinition = "decimal(20,2) comment '扣除优惠（商品级和订单级）后的应付总额'")
    private BigDecimal totalItemPrice;

    /**
     * 订单总额
     * <p>
     * <p>扣除优惠（商品级和订单级）后的应付总额 + 物流费用</p>
     * <p>计算公式: totalItemPrice + shipPrice</p>
     */
    @Column(name = "TOTAL_PRICE", columnDefinition = "decimal(20,2) comment '订单总额'")
    private BigDecimal totalPrice;

    /**
     * 订单应付金额
     * -- 账务应支付金额
     * <p>
     * <p>订单总额 - 抵扣金额（积分、充值卡等）</p>
     */
    @Column(name = "TOTAL_PAY_PRICE", columnDefinition = "decimal(20,2) comment '订单应付金额'")
    private BigDecimal totalPayPrice;

    /**
     * 订单实际支付金额
     * -- 账务已支付金额
     */
    @Column(name = "TOTAL_PAID_PRICE", columnDefinition = "decimal(20,2) comment '订单实际支付金额'")
    private BigDecimal totalPaidPrice;

    /**
     * 支付类型，微信支付:0;支付宝:1
     */
    @Column(name = "PAY_TYPE", nullable = false, columnDefinition = "smallint comment '支付类型，微信支付:1;支付宝:2'")
    @Convert(converter = PayType.EnumConvert.class)
    private PayType payType = PayType.WEIXIN;

    /**
     * 支付单号
     */
    @Column(name = "PAYMENT_ID", columnDefinition = "varchar(32) comment '支付单号'")
    private String paymentId;

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
     * id
     *
     * @return id
     */
    @Override
    public String id() {
        return id;
    }

    public FsmTransitionResult create() {
        Fsm fsm = getFsm();
        return null;
    }

    /**
     * 订单状态迁移
     *
     * @param role      执行角色
     * @param eventName 时间名称
     * @return 状态迁移结果
     */
    private FsmTransitionResult sendEvent(Role role, String eventName) {
        FsmContext<Order> context = buildContext(role);
        Fsm fsm = getFsm();
        return fsm.sendEvent(context, eventName);
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
     * 构造当前订单虚拟机状态
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
}
