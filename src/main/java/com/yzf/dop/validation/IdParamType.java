package com.yzf.dop.validation;


import com.yzf.dop.validation.condition.Condition;

import java.util.List;

/**
 * Created by yezhufeng on 2016/12/5.
 */
public class IdParamType extends AbstractParamType {

    @Override
    public boolean checkIdentity(Object arg, Class type) {
        if(arg == null){
            return false;
        }
        if(type.equals(Long.class)){
            long argVal = (Long)arg;
            return argVal > 0L;
        }
        return false;
    }

    @Override
    public void setConditionEnums(List<Condition> conditionList) {

    }
}
