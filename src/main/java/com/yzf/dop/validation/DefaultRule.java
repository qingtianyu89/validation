package com.yzf.dop.validation;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yzf.dop.validation.condition.Condition;
import com.yzf.dop.validation.condition.ConditionEnum;
import org.springframework.util.StringUtils;

import java.lang.ref.Reference;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by yezhufeng on 2016/11/25.
 */
public class DefaultRule extends AbstractRule {

    public DefaultRule(Item item, Object arg, String paramName, Integer argIndex) {
        super(item, arg, paramName, argIndex);
    }

    /**
     * 返回true表示参数不合法
     *
     * @param paramVo @return
     */
    @Override
    public boolean isParamEmpty(ParamVo paramVo) {
        Preconditions.checkNotNull(paramVo, "paramVo is null");
        Object arg = paramVo.getArg();
        if (arg == null) {
            paramVo.pushMatchStack(new ParamVo.LeafInfo(paramVo.getLeaf(), paramVo.getFieldName(), ConditionEnum.NULL));
            return true;
        }
        //自定义类型，保存当前对象
        Class argType = RuleUtils.getArgType(arg);
        if (argType.equals(Reference.class)) {
            paramVo.setRule(this);
        }
        //获取条件
        List<Condition> conditionList = getConditions(paramVo.getParamType(), argType);
        if (conditionList == null) {//如果为null，该类型可能不支持
            return false;
        }
        for (Condition condition : conditionList) {//一个paramVo对应多个condition
            if (!condition.wrapperCheck(arg, argType, paramVo)) {
                return true;//TODO
            }
        }
        return false;
    }

    @Override
    public String descParam(final ParamVo paramVo) {
        final Stack<ParamVo.LeafInfo> matchStack = paramVo.getMatchStack();
        Collections.reverse(matchStack);
        List<String> transform = Lists.transform(matchStack, new Function<ParamVo.LeafInfo, String>() {
            @Override
            public String apply(ParamVo.LeafInfo input) {
                Condition matchCondition = input.getMatchCondition();
                if (!StringUtils.isEmpty(input.getDesc())) {
                    return input.getDesc();
                }
                return input.getArgDesc() + matchCondition.getConditionDesc(paramVo);
            }
        });
        String join = Joiner.on("").join(transform);
        return join;
    }

}
