package com.bj58.daojia.dop2c.validation;

import java.lang.annotation.*;

/**
 * Created by pangming on 2016/11/25.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Item {

    int index();//索引 从0开始 必填(对应参数的索引顺序)

    String fieldName() default "";//field名字

    String desc() default "";//描述信息

    int maxLen() default RuleUtils.MAX_LEN;//字符串最大长度

    int minLen() default RuleUtils.MIN_LEN;//字符串最小长度

    int maxVal() default RuleUtils.MAX_VAL;//最小值

    int minVal() default RuleUtils.MIN_VAL;//最大值

    int[] in() default {};//包含

    String contain() default "";//包含

    String pattern() default "";//正则

    /**
     * 支持的参数类型, 默认根据参数类型匹配, 可查看{@link ParamTypeEnum}
     * 参数支持的条件规则可查看{@link com.bj58.daojia.dop2c.custom.interceptors.validation.condition.ConditionEnum}
     * 可自定义类型和匹配规则, 需继承{@link AbstractParamType}
     */
    Class<?> paramType() default ParamTypeEnum.class;

    Class<? extends AbstractRule> subRule() default DefaultRule.class;

    int init() default 0;
}
