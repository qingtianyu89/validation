package com.bj58.daojia.dop2c.validation;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by pangming on 2016/11/28.
 */
public class RuleUtils {

    public final static int MAX_LEN = -1;
    public final static int MIN_LEN = 0;
    public final static int MAX_VAL = Integer.MAX_VALUE;
    public final static int MIN_VAL = Integer.MIN_VALUE;

    /**
     * 获取参数类型
     *
     * @param obj
     * @return
     */
    public static Class getArgType(Object obj) throws NullPointerException,
            IllegalArgumentException {
        Preconditions.checkNotNull(obj, "obj is null");
        if (obj instanceof String) {
            return String.class;
        } else if (obj.getClass().isArray()) {
            return Array.class;
        } else if (obj instanceof Integer) {
            return Integer.class;
        } else if (obj instanceof Long) {
            return Long.class;
        } else if (obj instanceof Double) {
            return Double.class;
        } else if (obj instanceof BigDecimal) {
            return BigDecimal.class;
        } else if (obj instanceof Map<?, ?>) {
            return Map.class;
        } else if (obj instanceof Collection<?>) {
            return Collections.class;
        } else if (isReference(obj)) {
            return Reference.class;
        }
        throw new IllegalArgumentException(String.format("%s对象类型现在还不支持", obj.getClass()));
    }

    /**
     * 判断是否当前报下的自定义类型
     *
     * @param obj
     * @return
     */
    public static boolean isReference(Object obj) {
        String name = obj.getClass().getPackage().getName();
        return name.startsWith(getBasePackage());
    }

    private static String getBasePackage() {
        Package aPackage = RuleUtils.class.getPackage();
        String name = aPackage.getName();
        final String item = ".";
        Iterable<String> split = Splitter.on(item).split(name);
        int count = 0;
        String basePackage = "";
        for (String s : split) {
            if (count++ == 3) {
                break;
            }
            basePackage += s + item;
        }
        return basePackage;
    }
}
