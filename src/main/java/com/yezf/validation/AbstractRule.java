package com.yezf.validation;

import com.google.common.base.Preconditions;
import com.yezf.validation.condition.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by pangming on 2016/11/25.
 * 匹配规则
 */
public abstract class AbstractRule implements Rule {

    private static Logger logger = LoggerFactory.getLogger(AbstractRule.class);

    private ParamVo paramVo;

    public AbstractRule(Item item, Object arg, String paramName, Integer argIndex) {
        this.paramVo = this.changeItemToVo(item, arg, paramName, argIndex);
    }

    //下游暴露接口
    public abstract boolean isParamEmpty(ParamVo paramVo);

    protected String getDesc() {
        if (!StringUtils.isEmpty(paramVo.getDesc())) {
            return paramVo.getDesc();
        }
        return descParam(this.paramVo);
    }

    /**
     * 获取匹配参数后的提示文案
     *
     * @return
     */
    public abstract String descParam(ParamVo paramVo);

    @Override
    public boolean match() {
        boolean flag = isParamEmpty(getParamVo());
        return flag;
    }

    //item转换为ParamVo
    protected ParamVo changeItemToVo(Item item, Object arg, String paramName, int argIndex) {
        ParamVo paramVo = null;
        try {
            paramVo = new ParamVo(item, arg, paramName, argIndex);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return paramVo;
    }

    /**
     * 通过指定类型获取匹配条件
     *
     * @return
     */
    protected List<Condition> getConditions(Class<?> paramTypeClass, Class argType) {
        Preconditions.checkNotNull(paramTypeClass, "paramTypeClass is null");
        Preconditions.checkNotNull(argType, "argType is null");
        if (paramTypeClass.equals(ParamTypeEnum.class)) {
            for (ParamTypeEnum typeEnum : ParamTypeEnum.values()) {
                if (typeEnum.getType().equals(argType)) {
                    return typeEnum.load();
                }
            }
            return null;
        }
        try {
            //自定义类型
            Object o = paramTypeClass.newInstance();
            if (o instanceof AbstractParamType) {
                AbstractParamType abstractParamType = (AbstractParamType) o;
                return abstractParamType.load();
            }
        } catch (Exception e) {
            logger.error("paramTypeClass实例化异常");
        }
        throw new RuntimeException("paramType必须继承AbstractParamType");
    }

    protected ParamVo getParamVo() {
        return paramVo;
    }

    protected void setParamVo(ParamVo paramVo) {
        this.paramVo = paramVo;
    }
}
