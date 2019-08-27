package com.jingxiang.business.user.acct.common.consts;

import java.math.BigDecimal;

/**
 * 帐号常量
 * Created by liuzhaoming on 2019/8/7.
 */
public interface AcctConsts {

    /**
     * 支付单ID前缀
     */
    String ID_PREFIX_PAYMENT = "P";

    /**
     * 账户ID前缀
     */
    String ID_PREFIX_ACCOUNT = "A";

    /**
     * 账户流水ID前缀
     */
    String ID_PREFIX_ACCOUNT_BILL = "UB";

    /**
     * 充值单ID前缀
     */
    String ID_PREFIX_DEPOSIT = "UD";

    /**
     * 提现单ID前缀
     */
    String ID_PREFIX_WITHDRAWAL = "UW";

    /**
     * 系统买家账户
     */
    String ID_ACCOUNT_SYSTEM_BUYER = ID_PREFIX_ACCOUNT + "001";

    /**
     * 系统卖家账户
     */
    String ID_ACCOUNT_SYSTEM_SELLER = ID_PREFIX_ACCOUNT + "002";

    /**
     * 系统合伙人账户
     */
    String ID_ACCOUNT_SYSTEM_PARTNER = ID_PREFIX_ACCOUNT + "003";

    /**
     * 微信支付费率
     */
    BigDecimal WXPAY_FEE_RATE = new BigDecimal("0.006");

    /**
     * 服务费费率
     */
    BigDecimal SERVICE_FEE_RATE = new BigDecimal("0.01");

    /**
     * 充值单自动关闭时间(秒)
     */
    int DEPOSIT_AUTO_CLOSE_TIME_IN_SECONDS = 15 * 60;
}
