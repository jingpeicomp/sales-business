package com.jingxiang.business.acct.adapter.wechat;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * 微信支付API
 * Created by liuzhaoming on 2019/8/9.
 */
@RestController
@RequestMapping("/api/business/acct/wxpay")
@Slf4j
public class WxpayCallbackController {

    @Autowired
    private WxpayClient wxpayClient;

    @Autowired
    private WxpayService wxpayService;

    /**
     * 微信支付回调接口
     *
     * @param request http请求
     * @return 响应结果
     */
    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    public String notify(HttpServletRequest request) {
        log.info("Wxpay notify is called");
        Map<String, String> requestData = parseWxpayRequest(request);
        log.info("Wxpay parse request {}", requestData);

        try {
            if (!verify(requestData)) {
                log.error("Verify wxpay notify request not passed");
                return WxpayConsts.FAIL;
            }

            return wxpayService.notify(requestData);
        } catch (Exception e) {
            log.error("Notify signature verify error ", e);
            return WxpayConsts.FAIL;
        }
    }

    /**
     * 解析微信回调结果
     *
     * @param request 微信回调请求
     * @return 回调字符串
     */
    private Map<String, String> parseWxpayRequest(HttpServletRequest request) {
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
    private boolean verify(Map<String, String> requestData) {
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
}
