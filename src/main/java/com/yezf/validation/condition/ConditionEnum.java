package com.yezf.validation.condition;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.yezf.validation.Item;
import com.yezf.validation.NotCheck;
import com.yezf.validation.ParamVo;
import com.yezf.validation.RuleUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pangming on 2016/11/25.
 * 匹配默认条件
 */
public enum ConditionEnum implements Condition {

    NULL {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            return arg != null;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "不能为null";
        }
    }, EMPTY {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (type.equals(String.class)) {
                return !"".equals(arg);
            }
            if (type.equals(Collection.class)) {
                return !CollectionUtils.isEmpty((Collection<?>) arg);
            }
            if (type.equals(Map.class)) {
                return !CollectionUtils.isEmpty((Map<?, ?>) arg);
            }
            return false;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "不能为空";
        }
    }, MAXLEN {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (paramVo.getMaxLen() == RuleUtils.MAX_LEN) {
                return true;
            }
            if (type.equals(String.class)) {
                return ((String) arg).length() <= paramVo.getMaxLen();
            }
            return false;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "最大长度为" + paramVo.getMaxLen();
        }
    }, MINLEN {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (paramVo.getMinLen() == RuleUtils.MIN_LEN) {
                return true;
            }
            if (type.equals(String.class)) {
                return ((String) arg).length() >= paramVo.getMinLen();
            }
            return false;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "最小长度为" + paramVo.getMinLen();
        }
    }, MAXVAL {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (paramVo.getMaxVal() == RuleUtils.MAX_VAL) {
                return true;
            }
            int maxVal = paramVo.getMaxVal();
            if (type.equals(Integer.class)) {
                int argVal = (Integer) arg;
                return argVal <= maxVal;
            }
            if (type.equals(Long.class)) {
                long argVal = (Long) arg;
                return argVal <= maxVal;
            }
            return false;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "最大值为" + paramVo.getMaxVal();
        }
    }, MINVAL {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (paramVo.getMinVal() == RuleUtils.MIN_VAL) {
                return true;
            }
            int minVal = paramVo.getMinVal();
            if (type.equals(Integer.class)) {
                int argVal = (Integer) arg;
                return argVal >= minVal;
            }
            if (type.equals(Long.class)) {
                long argVal = (Long) arg;
                return argVal >= minVal;
            }
            return false;
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "最小值为" + paramVo.getMinVal();
        }
    }, ZERO {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (type == null || paramVo == null) {
                return false;
            }
            if (type.equals(Integer.class)) {
                int argVal = (Integer) arg;
                return argVal != 0;
            }
            if (type.equals(Long.class)) {
                long argVal = (Long) arg;
                return argVal != 0L;
            }
            if (type.equals(Double.class)) {
                Double argVal = (Double) arg;
                return argVal != 0;
            }
            if (type.equals(BigDecimal.class)) {
                BigDecimal argVal = (BigDecimal) arg;
                return argVal.compareTo(new BigDecimal(0)) != 0;
            }
            return !arg.equals(0);
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "不能为0";
        }
    }, IN {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (!type.equals(Integer.class)) {
                return false;
            }
            List<Integer> in = paramVo.getIn();
            if (in == null || in.isEmpty()) {
                return true;
            }
            return in.contains(arg);
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "必须为" + paramVo.getIn() + "中的一个";
        }
    }, CONTAIN {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (!type.equals(String.class)) {
                return false;
            }
            if (paramVo.getContain() == null || paramVo.getContain().isEmpty()) {
                return true;
            }
            String argVal = (String) arg;
            return argVal.contains(paramVo.getContain());
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "必须包含字符串 " + paramVo.getContain();
        }
    }, PATTERN {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (!type.equals(String.class)) {
                return false;
            }
            if (StringUtils.isEmpty(paramVo.getPattern())) {
                return true;
            }
            Pattern pattern = Pattern.compile(paramVo.getPattern());
            Matcher matcher = pattern.matcher((String) arg);
            return matcher.matches();
        }

        @Override
        public String getConditionDesc(ParamVo paramVo) {
            return "与正则表达式 [" + paramVo.getPattern() + "] 不匹配";
        }
    }, REFERENCE {
        @Override
        public boolean check(Object arg, Class type, ParamVo paramVo) {
            if (!type.equals(Reference.class)) {
                throw new IllegalArgumentException("该类型不属于自定类型");
            }
            Class<?> aClass = arg.getClass();
            for (Field field : aClass.getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                try {
                    Object objArg = field.get(arg);
                    //含有notCheck注解的不校验
                    if (field.isAnnotationPresent(NotCheck.class)) {
                        continue;
                    }
                    //对象中field的规则
                    if (field.isAnnotationPresent(Item.class)) {
                        descParamVo(paramVo, field.getAnnotation(Item.class));
                    }
                    paramVo.setArg(objArg);
                    Item item = field.getAnnotation(Item.class);
                    Preconditions.checkNotNull(paramVo.getRule());
                    if (paramVo.getRule().isParamEmpty(paramVo)) {
                        ParamVo.LeafInfo leafInfo = paramVo.popMatchStack();//弹出栈顶，并获取栈顶的condition
                        ParamVo.LeafInfo info = new ParamVo.LeafInfo(paramVo.getLeaf(), field.getName(), leafInfo.getMatchCondition());
                        if (item != null && !StringUtils.isEmpty(item.desc())) {
                            info.setDesc(item.desc());
                        }
                        paramVo.pushMatchStack(info);
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        public String getConditionDesc(final ParamVo paramVo) {
            return "中的";
        }
    };


    public boolean wrapperCheck(Object arg, Class type, ParamVo paramVo) {
        boolean check = check(arg, type, paramVo);
        paramVo.pushAllCondition(this);
        if (!check) {
            paramVo.pushMatchStack(new ParamVo.LeafInfo(paramVo.getLeaf(), paramVo.getFieldName(), this));
            return false;
        }
        return check;
    }


    /**
     * 继承item部分条件
     *
     * @param paramVo
     * @param item
     */
    protected void descParamVo(ParamVo paramVo, Item item) {
        paramVo.setContain(item.contain());
        paramVo.setIn(Ints.asList(item.in()));
        paramVo.setMaxLen(item.maxLen());
        paramVo.setMinLen(item.minLen());
        paramVo.setMaxVal(item.maxVal());
        paramVo.setMinVal(item.minVal());
        paramVo.setPattern(item.pattern());
        paramVo.setParamType(item.paramType());
    }
}
