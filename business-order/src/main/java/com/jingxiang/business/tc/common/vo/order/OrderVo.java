package com.jingxiang.business.tc.common.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jingxiang.business.tc.common.consts.CompleteStatus;
import com.jingxiang.business.tc.common.consts.OrderType;
import com.jingxiang.business.tc.common.consts.PayStatus;
import com.jingxiang.business.tc.common.consts.ShipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单值对象
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderVo implements Serializable {
    /**
     * 订单编号
     */
    private String id;

    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 买家
     */
    private String buyer;

    /**
     * 订单类型
     */
    @Builder.Default
    private OrderType type = OrderType.NORMAL;

    /**
     * 支付状态
     */
    private PayStatus payStatus;

    /**
     * 订单完成状态
     */
    private CompleteStatus completeStatus;

    /**
     * 订单发货状态
     */
    private ShipStatus shipStatus;

    /**
     * 创建时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 订单成功时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime finishTime;

    /**
     * 自动关闭订单时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime autoCloseTime;

    /**
     * 卖家发货时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shipTime;

    /**
     * 确认收货自动时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime autoConfirmTime;

    /**
     * 确认收货时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime confirmTime;

    /**
     * 自动确认收货时间（秒）
     */
    private Integer autoConfirmSeconds;

    /**
     * 订单账务信息
     */
    private OrderAmountVo amount;

    /**
     * 收货人
     */
    private OrderReceiverVo receiver;

    /**
     * 支付信息
     */
    private OrderPaymentVo payment;

    /**
     * 订单商品条目
     */
    private List<OrderProductVo> products;
}
