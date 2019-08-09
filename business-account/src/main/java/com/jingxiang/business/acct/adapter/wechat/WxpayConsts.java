package com.jingxiang.business.acct.adapter.wechat;

/**
 * 常量
 */
public interface WxpayConsts {

    enum SignType {
        MD5, HMACSHA256
    }

    String FAIL = "FAIL";
    String SUCCESS = "SUCCESS";
    String HMACSHA256 = "HMAC-SHA256";
    String MD5 = "toMd5";

    String FIELD_SIGN = "sign";
    String FIELD_SIGN_TYPE = "sign_type";

    String MICROPAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
    String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    String DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
    String SHORTURL_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";

    // sandbox
    String SANDBOX_MICROPAY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
    String SANDBOX_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    String SANDBOX_ORDERQUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
    String SANDBOX_REVERSE_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/reverse";
    String SANDBOX_CLOSEORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
    String SANDBOX_REFUND_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
    String SANDBOX_REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
    String SANDBOX_DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
    String SANDBOX_REPORT_URL = "https://api.mch.weixin.qq.com/sandboxnew/payitil/report";
    String SANDBOX_SHORTURL_URL = "https://api.mch.weixin.qq.com/sandboxnew/tools/shorturl";
    String SANDBOX_AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/sandboxnew/tools/authcodetoopenid";
}