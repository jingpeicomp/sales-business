package com.jingxiang.business.exception;

/**
 * 找不到对应的资源异常
 * Created by liuzhaoming on 2019/8/2.
 */
public class ResourceNotFindException extends ServiceException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ResourceNotFindException(String message) {
        super("K-000003", message);
    }

    public ResourceNotFindException() {
        this("Cannot find resource, please check the condition!");
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
    public ResourceNotFindException(String message, Throwable cause) {
        super(message, cause);
    }

//    @Override
//    public synchronized Throwable fillInStackTrace() {
//        return null;
//    }
}
