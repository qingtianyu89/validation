package com.yzf.dop.validation;

import com.google.common.collect.Lists;
import com.yzf.dop.validation.condition.Condition;
import com.yzf.dop.validation.condition.ConditionEnum;

import java.lang.ref.Reference;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yezhufeng on 2016/11/25.
 * 通过参数类型获取指定类型的匹配条件
 * 条件可自定义，需要继承{@link ConditionIdentity}
 * 支持自定义类型对象，自定义对象中的field，可通过{@link NotCheck}配置是否需要匹配，以及{@link Item}来定义匹配规则
 *              自定义对象中的field支持嵌套自定义类型
 */
public enum ParamTypeEnum implements ParamType {

    STRING(String.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.EMPTY);
            paramList.add(ConditionEnum.MAXLEN);
            paramList.add(ConditionEnum.MINLEN);
            paramList.add(ConditionEnum.CONTAIN);
            paramList.add(ConditionEnum.PATTERN);
            return paramList;
        }
    }, INTEGER(Integer.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.ZERO);
            paramList.add(ConditionEnum.MAXVAL);
            paramList.add(ConditionEnum.MINVAL);
            paramList.add(ConditionEnum.IN);
            return paramList;
        }
    }, LONG(Long.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.ZERO);
            return paramList;
        }
    }, DOUBLE(Double.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.ZERO);
            return paramList;
        }
    },  BIGDECIMAL(BigDecimal.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.ZERO);
            return paramList;
        }
    }, REFERENCE(Reference.class) {
        @Override
        public List<Condition> load() {
            List<Condition> paramList = Lists.newArrayList();
            paramList.add(ConditionEnum.NULL);
            paramList.add(ConditionEnum.REFERENCE);
            return paramList;
        }
    };

    private Class type;

    public List<Condition> load(){
        List<Condition> paramList = Lists.newArrayList();
        paramList.add(ConditionEnum.NULL);
        return paramList;
    }

    ParamTypeEnum(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
