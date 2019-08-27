package com.jingxiang.business.user.acct.adapter.wechat;

import com.jingxiang.business.utils.CommonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.jingxiang.business.user.acct.adapter.wechat.WxpayConsts.SignType;

/**
 * 微信支付配置参数
 * Created by liuzhaoming on 2019/8/8.
 */
@Data
@Slf4j
@ConfigurationProperties("jingxiang.business.acct.wxpay")
@Configuration
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
    private String localIp;

    /**
     * 连接请求超时时间
     */
    private int connectTimeoutInMills = 6000;

    /**
     * 数据读取超时时间
     */
    private int readTimeoutInMills = 15000;

    /**
     * 是否使用沙箱
     */
    private boolean useSandbox = false;

    /**
     * 加密方式
     */
    private SignType signType = SignType.MD5;

    /**
     * 用户证书文件地址
     */
    private String certFile;

    public String getLocalIp() {
        if (StringUtils.isBlank(localIp)) {
            return CommonUtils.getLocalIp();
        }

        return localIp;
    }

    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    public InputStream getCertStream() {
        if (StringUtils.isBlank(certFile)) {
            return null;
        }

        try {
            return Files.newInputStream(Paths.get(certFile));
        } catch (IOException e) {
            log.error("Cannot load cert file {}", certFile, e);
            return null;
        }
    }
}
