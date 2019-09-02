package com.jingxiang.business.exception;

import com.jingxiang.business.utils.MessageSourceUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 业务通用异常
 * Created by liuzhaoming on 2019/8/2.
 */
public class ServiceException extends RuntimeException {

    /**
     * 默认异常
     */
    private static final String DEFAULT_ERROR_CODE = "C-000001";

    private String errorCode;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ServiceException(String message) {
        this(DEFAULT_ERROR_CODE, message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = DEFAULT_ERROR_CODE;
    }

    public ServiceException(String errorCode, String... args) {
        this(errorCode, MessageSourceUtils.getMessage(errorCode, args, LocaleContextHolder.getLocale()));
    }

    private ServiceException(String errorCode, String message) {
        super(message == null ? errorCode : message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

//    @Override
//    public synchronized Throwable fillInStackTrace() {
//        return null;
//    }
}
