package com.bj58.daojia.dop2c.validation;

import com.google.common.collect.Lists;
import com.bj58.daojia.dop2c.validation.condition.Condition;
import com.bj58.daojia.dop2c.validation.condition.ConditionEnum;
import com.bj58.daojia.dop2c.validation.condition.ConditionIdentity;

import java.util.Collections;
import java.util.List;

/**
 * Created by pangming on 2016/12/5.
 * 抽象类型，为自定义类型提供扩展
 */
public abstract class AbstractParamType implements ParamType {

    private final List<Condition> conditionList = Lists.newArrayList();

    @Override
    public List<Condition> load() {
        conditionList.add(ConditionEnum.NULL);
        ConditionIdentity conditionIdentity = new ConditionIdentity() {
            @Override
            public boolean check(Object arg, Class type, ParamVo paramVo) {
                return checkIdentity(arg, type);
            }

            @Override
            public String getConditionDesc(ParamVo paramVo) {
                return "参数错误";
            }
        };
        conditionList.add(conditionIdentity);
        setConditionEnums(conditionList);
        Collections.unmodifiableCollection(conditionList);
        return conditionList;
    }

    /**
     * 自定义匹配
     *
     * @param arg
     * @param type
     * @return
     */
    public abstract boolean checkIdentity(Object arg, Class type);

    /**
     * 用于添加枚举中的指定条件{@link com.bj58.daojia.dop2c.custom.interceptors.validation.condition.ConditionEnum}
     * 可重写
     *
     * @param conditionList
     */
    public void setConditionEnums(List<Condition> conditionList) {
        //需要使用ConditionEnums中的条件时可以在这添加
    }
}
