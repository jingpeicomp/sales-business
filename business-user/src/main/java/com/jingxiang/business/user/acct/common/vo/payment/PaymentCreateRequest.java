package com.jingxiang.business.user.acct.common.vo.payment;

import com.jingxiang.business.consts.PayType;
import com.jingxiang.business.user.acct.common.consts.PaymentSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付单创建请求
 * Created by liuzhaoming on 2019/8/9.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest implements Serializable {
    /**
     * 店铺ID
     */
    @NotBlank(message = "店铺不能为空")
    private String shopId;

    /**
     * 订单ID
     */
    @NotBlank(message = "源ID不能为空")
    private String sourceId;

    /**
     * 付款者
     */
    @NotBlank(message = "付款者不能为空")
    private String payer;

    /**
     * 收款者
     */
    @NotBlank(message = "收款者不能为空")
    private String payee;

    /**
     * 支付类型，微信支付:0;支付宝:1
     */
    @NotNull(message = "支付类型不能为空")
    @Builder.Default
    private PayType payType = PayType.WEIXIN;

    /**
     * 应付金额
     */
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "金额必须大于0")
    private BigDecimal payAmount;

    /**
     * 支付单标题，会显示在支付平台
     */
    @NotBlank(message = "支付单标题不能为空")
    @Length(min = 2, max = 60, message = "商品名称长度应该在[2,60]")
    private String title;

    /**
     * 支付单描述
     */
    private String description;

    /**
     * 支付单来源
     */
    @NotNull(message = "支付源类型不能为空")
    @Builder.Default
    private PaymentSource source = PaymentSource.ORDER_PAY;
}
