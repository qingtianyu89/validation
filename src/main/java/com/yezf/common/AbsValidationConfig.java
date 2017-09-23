package com.yezf.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by pangming on 2017/4/19.
 */
public abstract class AbsValidationConfig {

    private ProceedingJoinPoint joinPoint;
    private String errorMsg;
    private Object[] args;

    public AbsValidationConfig(ProceedingJoinPoint joinPoint, Object[] args, String errorMsg) {
        this.joinPoint = joinPoint;
        this.args = args;
        this.errorMsg = errorMsg;
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
}
