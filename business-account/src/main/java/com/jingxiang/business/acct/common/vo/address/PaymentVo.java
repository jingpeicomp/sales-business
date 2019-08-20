package com.jingxiang.business.acct.common.vo.address;

import com.jingxiang.business.acct.common.consts.PaymentStatus;
import com.jingxiang.business.consts.PayType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付单值对象
 * Created by liuzhaoming on 2019/8/20.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVo implements Serializable {
    /**
     * ID
     */
    private String id;

    /**
     * 店铺ID
     */
    private String shopId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 付款者
     */
    private String buyer;


    /**
     * 支付类型，微信支付:0;支付宝:1
     */
    private PayType payType;

    /**
     * 应付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改时间
     */
    private LocalDateTime payTime;

    /**
     * 支付平台的支付单号
     */
    private String platformPayId;

    /**
     * 支付网关预支付单ID，只有微信支付有
     */
    private String prePlatformPayId;

    /**
     * 支付单状态
     */
    private PaymentStatus status = PaymentStatus.UNPAID;

    /**
     * 支付单标题，会显示在支付平台
     */
    private String title;

    /**
     * 支付单描述
     */
    private String description;
}
