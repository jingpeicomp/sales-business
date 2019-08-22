package com.jingxiang.business.acct.pay;

import com.jingxiang.business.acct.adapter.wechat.WxpayNotifyRequest;
import com.jingxiang.business.acct.adapter.wechat.WxpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调接口
 * Created by liuzhaoming on 2019/8/20.
 */
@RestController
@RequestMapping(path = "/api/business/acct/pay/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private WxpayService wxpayService;

    @Autowired
    private PayService payService;

    /**
     * 微信支付回调接口
     *
     * @param httpRequest http请求
     * @return 响应结果
     */
    @RequestMapping(value = "/wxpay", method = RequestMethod.POST)
    public String wxpayNotify(HttpServletRequest httpRequest) {
        WxpayNotifyRequest parsedRequest = wxpayService.parseNotifyRequest(httpRequest);
        if (!parsedRequest.check()) {
            return wxpayService.buildFailNotifyResponse(parsedRequest.getErrorMessage());
        }

        return payService.updateWxpayNotification(parsedRequest);
    }
}
