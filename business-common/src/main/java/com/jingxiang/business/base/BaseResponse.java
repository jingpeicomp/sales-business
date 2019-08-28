package com.jingxiang.business.base;

import com.jingxiang.business.utils.MessageSourceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;

/**
 * 响应基类
 * Created by liuzhaoming on 2019/8/28.
 */
@Data
@AllArgsConstructor
public class BaseResponse implements Serializable {

    public static BaseResponse SUCCESSFUL() {
        return new BaseResponse(ResultCode.SUCCESSFUL);
    }

    public static BaseResponse FAILED() {
        return new BaseResponse(ResultCode.FAILED);
    }

    public BaseResponse(String code) {
        this.code = code;
        this.message = MessageSourceUtils.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * 结果码
     */
    private String code;

    /**
     * 消息内容
     */
    private String message;
}
