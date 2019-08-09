package com.jingxiang.business.acct.adapter.wechat;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付服务
 * Created by liuzhaoming on 2019/8/9.
 */
@Service
@Slf4j
public class WxpayService {

    @Autowired
    private WxpayConfigProperties wxpayConfigProperties;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    /**
     * wxpayClient sdk
     */
    @Autowired
    private WxpayClient wxpayClient;

    /**
     * 支付订单，微信其实是生成预支付单
     *
     * @param order 订单
     * @return 预支付单二维码url
     */
    public String pay(Order order) {
        log.info("Start pay order {}", order);
        Map<String, String> unifiedData = buildUnifiedRequest(order);

        try {
            Map<String, String> response = wxpayClient.unifiedOrder(unifiedData);
            log.info("Wxpay unify order response is {}", response);
            String shortUrl = parseResponse(order, response);
            return QrCodeUtils.generateQrCode(shortUrl);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Wxpay pay order {} error", order.getId(), e);
            throw new ServiceException("T-020004", e.getLocalizedMessage());
        }
    }

    /**
     * 回调处理
     *
     * @param requestData 请求数据
     * @return 处理结果
     */
    public String notify(Map<String, String> requestData) {
        String orderId = requestData.get("out_trade_no");
        String tradeNo = requestData.get("transaction_id");
        //订单总金额，单位为分
        String totalFee = requestData.get("total_fee");
        String formattedTotalFee = new BigDecimal(totalFee)
                .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN)
                .toString();

        Order order = orderService.query(NumberUtils.toLong(orderId, 0L));
        if (null == order) {
            log.error("Wxpay notify fail..... Cannot find order by id {}", orderId);
            return "FAIL";
        }

        if (!order.getFormattedTotalFee().equals(formattedTotalFee)) {
            log.error("Wxpay notify fail..... total fee is invalid. Pay fee is {}, order fee is {}",
                    totalFee, order.getFormattedTotalFee());
            return "FAIL";
        }

        //更新订单状态
        LocalDateTime payTime = LocalDateTime.now();
        int count = orderService.updatePayInfo(NumberUtils.toLong(orderId),
                TradeConsts.ORDER_STATE_PAYED, tradeNo, payTime);
        log.info("Update order state successfully, and updated rows count is {}", count);
        //清空购物车
        cartService.delete(order.getUserId());
        if (count > 0) {
            orderService.addPermission(order, payTime);
        }
        return "SUCCESS";
    }

    /**
     * 创建微信下单请求
     *
     * @param order 订单
     * @return 微信支付下单请求
     */
    private Map<String, String> buildUnifiedRequest(Order order) {
        Map<String, String> unifiedData = new HashMap<>();
        unifiedData.put("body", "Tradewow数据查看和下载权限");
        String desc = order.getPayDescription();
        if (desc.length() > 100) {
            desc = desc.substring(0, 100);
        }
        unifiedData.put("attach", desc);
        unifiedData.put("out_trade_no", order.getId().toString());
        unifiedData.put("device_info", order.getUserId());
        unifiedData.put("fee_type", "CNY");
        unifiedData.put("total_fee", order.getFormattedTotalFeeByCent());
        unifiedData.put("spbill_create_ip", wxpayConfigProperties.getLocalIp());
        unifiedData.put("notify_url", wxpayConfigProperties.getNotifyUrl());
        unifiedData.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        unifiedData.put("product_id", order.getGoodsList().get(0).getId().toString());

        return unifiedData;
    }

    /**
     * 微信下单响应结果解析
     *
     * @param order    订单
     * @param response 响应结果
     * @return 响应二维码url
     */
    private String parseResponse(Order order, Map<String, String> response) {
        String returnCode = response.get("return_code");
        if (!"SUCCESS".equals(returnCode)) {
            log.error("Wxpay return fail with error message {}", response.get("return_msg"));
            throw new ServiceException("T-020004");
        }

        String resultCode = response.get("result_code");
        if (!"SUCCESS".equals(resultCode)) {
            log.error("Wxpay return fail with error code {} , error message {}", response.get("err_code"),
                    response.get("err_code_des"));
            throw new ServiceException("T-020004");
        }

        //微信预支付ID,
        String prepayId = response.get("prepay_id");
        orderService.updatePrePayId(order.getId(), prepayId);

        return response.get("code_url");
    }
}
