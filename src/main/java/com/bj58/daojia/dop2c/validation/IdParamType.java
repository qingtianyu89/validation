package com.bj58.daojia.dop2c.validation;


import com.bj58.daojia.dop2c.validation.condition.Condition;

import java.util.List;

/**
 * Created by pangming on 2016/12/5.
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
