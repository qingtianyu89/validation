package com.yezf.validation.condition;


import com.yezf.validation.ParamVo;

/**
 * Created by pangming on 2016/11/28.
 * 自定义条件类
 */
public abstract class ConditionIdentity implements Condition {

    public abstract boolean check(Object arg, Class type, ParamVo paramVo);

    @Override
    public boolean wrapperCheck(Object arg, Class type, ParamVo paramVo) {
        boolean check = check(arg, type, paramVo);
        paramVo.pushAllCondition(this);
        if (!check) {
            paramVo.pushMatchStack(new ParamVo.LeafInfo(paramVo.getLeaf(), paramVo.getFieldName(), this));
            return false;
        }
        return check;
    }

}
