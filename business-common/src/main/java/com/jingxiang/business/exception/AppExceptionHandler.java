package com.jingxiang.business.exception;

import com.jingxiang.business.base.BaseResponse;
import com.jingxiang.business.base.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常处理
 * Created by liuzhaoming on 2019/8/28.
 */
@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
    @ExceptionHandler(value = ResourceNotFindException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public BaseResponse resourceNotFoundHandler(HttpServletRequest request, ResourceNotFindException ex) {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        log.error("do [{}] on [{}] failed. exMsg:{}, {}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage(), ex);
        if (log.isDebugEnabled()) {
            log.error("queryString:{}, parameterMap: {}", request.getQueryString(), request.getParameterMap(), ex);
        }

        return new BaseResponse(ex.getErrorCode(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse serviceExceptionHandler(HttpServletRequest request, ServiceException ex) {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        log.error("do [{}] on [{}] failed. exMsg:{}, {}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage(), ex);
        if (log.isDebugEnabled()) {
            log.error("queryString:{}, parameterMap: {}", request.getQueryString(), request.getParameterMap(), ex);
        }

        return new BaseResponse(ex.getErrorCode(), ex.getLocalizedMessage());
    }

    /**
     * 返回400异常码，以及json信息，
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse illegalArgsHandler(HttpServletRequest request, ConstraintViolationException ex) {
        List<String> message = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.error("do [{}] on [{}] 参数验证错误. exMsg:{}, {}", request.getMethod(), request.getRequestURL(), message, ex);
        if (log.isDebugEnabled()) {
            log.error("queryString:{}, parameterMap: {}", request.getQueryString(), request.getParameterMap(), ex);
        }

        return new BaseResponse(ResultCode.INVALID_PARAM, ex.getLocalizedMessage());
    }

    /**
     * 返回400异常码，以及json信息，
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponse springIllegalArgsHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        log.error("do [{}] on [{}] 参数验证错误. exMsg:{}, {}", request.getMethod(), request.getRequestURL(), message, ex);
        if (log.isDebugEnabled()) {
            log.error("queryString:{}, parameterMap: {}", request.getQueryString(), request.getParameterMap(), ex);
        }

        return new BaseResponse(ResultCode.INVALID_PARAM, message);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public BaseResponse defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        log.error("do [{}] on [{}] failed. exMsg:{} ,{}", request.getMethod(), request.getRequestURL(),
                ex.getLocalizedMessage(), ex);
        if (log.isDebugEnabled()) {
            log.error("queryString:{}, parameterMap: {}", request.getQueryString(), request.getParameterMap(), ex);
        }

        return new BaseResponse(ResultCode.FAILED, ex.getLocalizedMessage());
    }

}
