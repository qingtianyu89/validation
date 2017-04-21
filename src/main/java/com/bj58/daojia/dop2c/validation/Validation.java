package com.bj58.daojia.dop2c.validation;

import java.lang.annotation.*;

/**
 * Created by pangming on 2016/11/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Validation {

    /**
     * 描述信息，按照参数的顺序，如果只对某个位置参数设置一个描述，前边的值可用“”代替
     * 比如：两个参数，对第二个参数设置描述信息  desc={"","第二个参数不合法"}
     * item中desc的优先级高于此desc
     */
    String[] desc() default {};

    /**
     * 校验范围：1部分校验，只校验items 2全部校验
     */
    int part() default 2;

    /**
     * 不进行校验的参数，值为参数的位置，只对part=2时生效
     */
    int[] excepts() default {};

    /**
     * 配置参数的校验项
     */
    Item[] items() default {};

    /**
     * 校验规则,可扩展继承{@link AbstractRule}即可
     * 优先取item中的
     */
    Class<? extends AbstractRule> rule() default DefaultRule.class;
}
