package com.yezf.validation;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.yezf.common.AbsValidationConfig;
import com.yezf.utils.FileUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 校验参数
 * 支持controller，service， 匹配到参数错误时controller返回json，service抛出异常
 * 使用方法：
 * 在方法上增加: {@link Validation}注解即可，具体配置规则可以查看该注解
 *  *** 引入参数校验切面
 *            需要：1.aop 命名空间
 *                 2.aop 扫描配置  <aop:aspectj-autoproxy/>
 *                 3.bean注入  <bean id="validationInterceptor" class="ValidationInterceptor" />
 *                         或者加入扫描配置文件 <context:component-scan base-package="com.yezf"/>
 *                 4.继承 @{{@link AbsValidationConfig}} 配置参数错误时需要返回的值
 */
@Component
@Aspect
public class ValidationInterceptor implements Ordered, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(ValidationInterceptor.class);
    private static Class absValidationConfigClass;

    @Around(value = "@annotation(validation)")
    public Object before(ProceedingJoinPoint joinPoint, Validation validation) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {//没有参数直接返回
            return joinPoint.proceed();
        }
        long startTime = System.currentTimeMillis();
        //校验
        Map<Integer, String> illegalParamsMap = invokeValidation(joinPoint, validation);
        long endTime = System.currentTimeMillis();
        Signature signature = joinPoint.getSignature();
        logger.debug("方法 {}，参数校验时间为：{}", signature.getName(), endTime - startTime);
        //参数都合法
        if (illegalParamsMap.isEmpty()) {
            return joinPoint.proceed();
        }

        //参数错误描述
        String errorMsg = getParamsDesc(validation, illegalParamsMap);
        logger.info("匹配到不合法的参数：参数值={}, 错误描述={}", args.toString(), errorMsg);
        AbsValidationConfig validationConfig;
        try {
            validationConfig = getValidationConfig(joinPoint, args, errorMsg);
        } catch (Exception e) {
            return joinPoint.proceed();
        }
        try {
            return validationConfig.getParamErrorReturn();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 参数校验
     *
     * @param joinPoint
     * @param validation
     * @return
     */
    private Map<Integer, String> invokeValidation(ProceedingJoinPoint joinPoint, Validation validation) {
        Object[] args = joinPoint.getArgs();
        //获取方法参数名称
        String[] parameterNames = null;
        //校验的参数集合
        List<Integer> validationList = Lists.newArrayList();
        //匹配到的错误参数
        Map<Integer, String> illegalParamsMap = new HashMap<Integer, String>();
        for (Item item : validation.items()) {
            if (item == null) {
                continue;
            }
            int argIndex = item.index();
            if (!(argIndex >= 0 && argIndex < args.length)) {//如果item中的index值大于参数的范围不校验 并抛出异常
                throw new IllegalArgumentException(String.format("item中的index的范围为%s-%s", 0, (args.length - 1)));
            }
            try {
                Class<AbstractRule> subClass = (Class<AbstractRule>) item.subRule();
                parameterNames = parameterNames == null ? getParameterNames(joinPoint) : parameterNames;
                String fieldName = StringUtils.isEmpty(item.fieldName()) ? parameterNames[argIndex] : item.fieldName();
                AbstractRule rule = createRuleInstance(subClass, new Object[]{item, args[argIndex], fieldName, argIndex});
                validationList.add(argIndex);
                if (rule.match()) {//参数不合法
                    illegalParamsMap.put(argIndex, rule.getDesc());//默认获取rule上的desc
                    break;
                }
            } catch (Exception e) {//实例化异常
                logger.error("实例化AbstractRule异常", e);
            }
        }

        Class<? extends AbstractRule> ruleClass = validation.rule();//默认匹配规则
        List<Integer> exceptList = Ints.asList(validation.excepts());//不需要校验的参数索引
        //单一校验 还是全部校验
        if (validation.part() != 2 || !illegalParamsMap.isEmpty()) {
            return illegalParamsMap;
        }
        //全部校验
        for (int i = 0; i < args.length; i++) {
            if (validationList.contains(i) || exceptList.contains(i)) {
                continue;//已经校验过
            }
            try {
                parameterNames = parameterNames == null ? getParameterNames(joinPoint) : parameterNames;
                AbstractRule subRule = createRuleInstance((Class<AbstractRule>) ruleClass, new Object[]{null, args[i], parameterNames[i], i});
                validationList.add(i);
                if (subRule.match()) {
                    illegalParamsMap.put(i, subRule.getDesc());
                    break;
                }
            } catch (Exception e) {
                logger.error("实例化AbstractRule异常");
                e.printStackTrace();
            }
        }
        return illegalParamsMap;
    }


    /**
     * 实例化rule
     *
     * @param ruleClass
     * @param params
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private AbstractRule createRuleInstance(Class<AbstractRule> ruleClass, Object... params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<AbstractRule> constructor = ruleClass.getConstructor(Item.class, Object.class, String.class, Integer.class);
        return constructor.newInstance(params);
    }

    /**
     * 获取参数名称
     *
     * @param joinPoint
     * @return
     */
    private String[] getParameterNames(ProceedingJoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getTarget().getClass();
        Method method = null;
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            try {
                method = aClass.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (method == null) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(joinPoint.getSignature().getName())
                        && declaredMethod.getParameterTypes().length == joinPoint.getArgs().length) {
                    method = declaredMethod;
                    break;
                }
            }
        }
        Preconditions.checkNotNull(method);
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        return parameterNameDiscoverer.getParameterNames(method);
    }

    /**
     * 获取参数描述信息
     *
     * @param validation
     * @param illegalParamsMap
     * @return
     */
    private String getParamsDesc(Validation validation, Map<Integer, String> illegalParamsMap) {
        //替换匹配到不合法参数时的文案内容
        if (validation.desc().length > 0) {
            Item[] items = validation.items();
            for (int i = 0; i < validation.desc().length; i++) {
                String argDesc = (validation.desc())[i];
                if (StringUtils.isEmpty(argDesc)) {
                    continue;
                }
                Item item = this.getItemByArgIndex(items, i);
                if (illegalParamsMap.containsKey(i) && (item == null || StringUtils.isEmpty(item.desc()))) {
                    illegalParamsMap.put(i, argDesc);
                }
            }
        }
        List<String> descList = Lists.newArrayList();
        for (Map.Entry<Integer, String> entry : illegalParamsMap.entrySet()) {
            String argDesc = entry.getValue();
            descList.add(argDesc);
        }
        return Joiner.on(";").join(descList);
    }

    /**
     * 通过参数位置获取item，如果没有返回null
     *
     * @param items
     * @param argIndex
     * @return
     */
    private Item getItemByArgIndex(Item[] items, int argIndex) {
        for (Item item : items) {
            if (item.index() == argIndex) {
                return item;
            }
        }
        return null;
    }


    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //匹配到absValidationConfig
        logger.info("初始化 AbsValidationConfig");
        Class aClass = FileUtil.matchSon(AbsValidationConfig.class);
        if(aClass == null){
            logger.warn("AbsValidationConfig 继承类没有发现, validationInterceptor不生效");
            return;
        }
        absValidationConfigClass = aClass;
    }

    private AbsValidationConfig getValidationConfig(ProceedingJoinPoint joinPoint, Object[] args, String errorMsg) throws Exception {
        Preconditions.checkNotNull(absValidationConfigClass);
        Constructor<AbsValidationConfig> constructor = absValidationConfigClass.getConstructor(ProceedingJoinPoint.class,
                Object[].class, String.class);
        return constructor.newInstance(joinPoint, args, errorMsg);
    }

}
