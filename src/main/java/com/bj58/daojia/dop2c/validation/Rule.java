package com.bj58.daojia.dop2c.validation;

/**
 * Created by pangming on 2016/11/29.
 */
public interface Rule{

    /**
     * 校验参数, 参数错误返回true，正确返回false
     * @return
     */
    public boolean match();

}
