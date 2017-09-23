package com.yezf.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by pangming on 2017/4/19.
 */
public abstract class AbsValidationConfig {

    private ProceedingJoinPoint joinPoint;
    private String errorMsg;
    private Object[] args;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public AbsValidationConfig(ProceedingJoinPoint joinPoint, Object[] args, String errorMsg) {
        this.joinPoint = joinPoint;
        this.args = args;
        this.errorMsg = errorMsg;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(ra != null){
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            request = sra.getRequest();
            ServletWebRequest servletWebRequest = new ServletWebRequest(request);
            response = servletWebRequest.getResponse();
        }
    }

    /**
     * 参数错误返回值
     *
     * @return
     */
    public abstract Object getParamErrorReturn();

    protected Method getMethod() {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            return methodSignature.getMethod();
        }
        return null;
    }

    public ProceedingJoinPoint getJoinPoint() {
        return joinPoint;
    }

    public void setJoinPoint(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
