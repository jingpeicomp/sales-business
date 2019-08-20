package com.jingxiang.business.acct.adapter.wechat;


import com.jingxiang.business.acct.pay.Payment;
import com.jingxiang.business.exception.ServiceException;
import com.jingxiang.business.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
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

    /**
     * wxpayClient sdk
     */
    @Autowired
    private WxpayClient wxpayClient;

    private static String SUCCESS_NOTIFY_RESPONSE;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", WxpayConsts.SUCCESS);
        map.put("return_msg", "OK");
        SUCCESS_NOTIFY_RESPONSE = WxpayUtil.mapToXml(map);
    }

    /**
     * 支付支付单，微信其实是生成预支付单
     *
     * @param payment 支付单
     * @return 微信预支付单ID
     */
    public String pay(Payment payment) {
        log.info("WxPay start pay {}", payment);
        Map<String, String> unifiedData = buildUnifiedRequest(payment);

        try {
            Map<String, String> response = wxpayClient.unifiedOrder(unifiedData);
            log.info("Wxpay unify order response is {}", response);
            return parseUnifiedResponse(response);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Wxpay pay payment {} {} error", payment.getId(), payment.getOrderId(), e);
            throw new ServiceException("T-020004", e.getLocalizedMessage());
        }
    }

    /**
     * 微信支付回调接口
     *
     * @param request http请求
     * @return 响应结果
     */
    public WxpayNotifyRequest parseNotifyRequest(HttpServletRequest request) {
        log.info("Wxpay notify is called");
        Map<String, String> requestData = parseNotifyHttpParam(request);
        log.info("Wxpay parse request {}", requestData);

        try {
            if (!verifyNotifyParam(requestData)) {
                log.error("Verify wxpay notify request not passed");
                return WxpayNotifyRequest.builder()
                        .errorMessage("Verify wxpay notify request not passed")
                        .build();
            }

            String orderId = requestData.get("out_trade_no");
            String tradeNo = requestData.get("transaction_id");
            String paymentId = requestData.get("attach");
            //支付单总金额，单位为分
            String totalFee = requestData.get("total_fee");
            String formattedTotalFee = new BigDecimal(totalFee)
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN)
                    .toString();
            return WxpayNotifyRequest.builder()
                    .orderId(orderId)
                    .transactionId(tradeNo)
                    .payAmount(formattedTotalFee)
                    .paymentId(paymentId)
                    .build();
        } catch (Exception e) {
            log.error("Notify signature verify error ", e);
            return WxpayNotifyRequest.builder()
                    .errorMessage("Parse wxpay notify request error")
                    .build();
        }
    }

    /**
     * 构建微信回调成功响应结果
     *
     * @return 微信回调成功响应结果
     */
    public String buildSuccessNotifyResponse() {
        return SUCCESS_NOTIFY_RESPONSE;
    }

    /**
     * 构建微信回调失败响应结果
     *
     * @param failMessage 失败原因
     * @return 微信回调失败响应结果
     */
    public String buildFailNotifyResponse(String failMessage) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", WxpayConsts.FAIL);
        map.put("return_msg", failMessage);
        return WxpayUtil.mapToXml(map);
    }

    /**
     * 构建微信回调失败响应结果
     *
     * @return 微信回调失败响应结果
     */
    public String buildFailNotifyResponse() {
        return buildFailNotifyResponse(WxpayConsts.FAIL);
    }

    /**
     * 解析微信回调请求
     *
     * @param request 微信回调请求
     * @return 回调请求
     */
    private Map<String, String> parseNotifyHttpParam(HttpServletRequest request) {
        // 获取微信调用我们notify_url的返回信息
        try (InputStream inStream = request.getInputStream();
             ByteArrayOutputStream outSteam = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String responseStr = new String(outSteam.toByteArray(), "utf-8");
            return WxpayUtil.xmlToMap(responseStr);
        } catch (Exception e) {
            log.error("Parse wxpay notify response error", e);
        }

        return Collections.emptyMap();
    }

    /**
     * 校验请求
     *
     * @param requestData 请求解析map
     * @return boolean
     */
    private boolean verifyNotifyParam(Map<String, String> requestData) {
        if (MapUtils.isEmpty(requestData)) {
            return false;
        }

        if (!wxpayClient.isPayResultNotifySignatureValid(requestData)) {
            log.error("Verify wxpay notify request not passed");
            return false;
        }

        String returnCode = requestData.get("return_code");
        if (!WxpayConsts.SUCCESS.equals(returnCode)) {
            log.error("Wxpay notify fail with error message {}", requestData.get("return_msg"));
            return false;
        }

        return true;
    }

    /**
     * 创建微信下单请求
     *
     * @param payment 支付单
     * @return 微信支付下单请求
     */
    private Map<String, String> buildUnifiedRequest(Payment payment) {
        Map<String, String> unifiedData = new HashMap<>();
        unifiedData.put("body", payment.getTitle());
        unifiedData.put("attach", payment.getId());
        unifiedData.put("out_trade_no", payment.getOrderId());
        unifiedData.put("device_info", payment.getBuyer());
        unifiedData.put("fee_type", "CNY");
        unifiedData.put("total_fee", CommonUtils.formatDownFee(payment.getPayAmount()));
        unifiedData.put("spbill_create_ip", wxpayConfigProperties.getLocalIp());
        unifiedData.put("notify_url", wxpayConfigProperties.getNotifyUrl());
        unifiedData.put("trade_type", "APP");  // APP支付
        return unifiedData;
    }

    /**
     * 微信下单响应结果解析
     *
     * @param response 响应结果
     * @return 微信预支付单ID
     */
    private String parseUnifiedResponse(Map<String, String> response) {
        String returnCode = response.get("return_code");
        if (!WxpayConsts.SUCCESS.equals(returnCode)) {
            log.error("Wxpay return fail with error message {}", response.get("return_msg"));
            throw new ServiceException("T-020004");
        }

        String resultCode = response.get("result_code");
        if (!WxpayConsts.SUCCESS.equals(resultCode)) {
            log.error("Wxpay return fail with error code {} , error message {}", response.get("err_code"),
                    response.get("err_code_des"));
            throw new ServiceException("T-020004");
        }

        //微信预支付ID
        return response.get("prepay_id");
    }
}
