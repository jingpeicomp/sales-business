package com.jingxiang.business.base;

/**
 * 结果码
 * Created by liuzhaoming on 2019/8/28.
 */
public interface ResultCode {

    /**
     * 操作成功；
     */
    String SUCCESSFUL = "C-000000";

    /**
     * 操作失败；
     */
    String FAILED = "C-000001";

    /**
     * 参数错误；
     */
    String INVALID_PARAM = "C-000003";

    /**
     * 找不到对应的资源；
     */
    String NOT_FOUND = "C-000002";
}
