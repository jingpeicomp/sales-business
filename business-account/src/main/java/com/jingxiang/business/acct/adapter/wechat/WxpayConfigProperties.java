package com.jingxiang.business.acct.adapter.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;

/**
 * 微信支付配置参数
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@ConfigurationProperties("jingxiang.business.acct.wxpay")
public class WxpayConfigProperties {

    /**
     * App ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * API密钥
     */
    private String appKey;

    /**
     * 服务器异步通知页面路径
     */
    private String notifyUrl;

    /**
     * 本地IP
     */
    private String localIp = "localhost";

    /**
     * 连接请求超时时间
     */
    private int connectTimeoutInMills = 6000;

    /**
     * 数据读取超时时间
     */
    private int readTimeoutInMills = 15000;

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public InputStream getCertStream() {
        return null;
    }
}
