package com.yezf.validation.condition;


import com.yezf.validation.ParamVo;

/**
 * Created by pangming on 2016/11/28.
 * 匹配条件
 */
public interface Condition {

    /**
     * 对参数进行匹配,如果参数错误返回false，正确返回true
     * @param arg
     * @param type
     * @param paramVo
     * @return
     */
    public boolean check(Object arg, Class type, ParamVo paramVo);

    /**
     * match扩展
     * @param arg
     * @param type
     * @param paramVo
     * @return
     */
    public boolean wrapperCheck(Object arg, Class type, ParamVo paramVo);

    public String getConditionDesc(ParamVo paramVo);
}
