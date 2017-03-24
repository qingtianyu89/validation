package com.yzf.dop.validation;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.yzf.dop.validation.condition.Condition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yezhufeng on 2016/11/25.
 */
public class ParamVo {

    private String fieldName;
    private String desc;
    private int maxLen = RuleUtils.MAX_LEN;
    private int minLen = RuleUtils.MIN_LEN;
    private int maxVal = RuleUtils.MAX_VAL;
    private int minVal = RuleUtils.MIN_VAL;
    private List<Integer> in;
    private String contain;
    private String pattern;
    private int argIndex;//参数在方法中的索引
    private Class<?> paramType = ParamTypeEnum.class;
    private List<Condition> allConditions;//改arg支持的所有Condition
    private Object arg;//参数值
    /**
     * 节点号 自定义对象下边的节点号 默认为0 每进一层对象增加1
     */
    private int leaf;
    private Stack<LeafInfo> matchStack;
    private AbstractRule rule;

    public ParamVo(Item item, Object arg, String fieldName, int argIndex)
            throws IllegalAccessException, InstantiationException {
        this.argIndex = argIndex;
        this.fieldName = fieldName;
        if (item == null) {
            in = Lists.newArrayList();
            this.arg = arg;
            if (StringUtils.isEmpty(fieldName)) {
                this.fieldName = "参数args[" + argIndex + "]";
            }
            return;
        }
        this.desc = item.desc();
        this.maxLen = item.maxLen();
        this.minLen = item.minLen();
        this.maxVal = item.maxVal();
        this.minVal = item.minVal();
        this.in = Ints.asList(item.in());
        this.contain = item.contain();
        this.pattern = item.pattern();
        this.argIndex = item.index();
        this.paramType = item.paramType();
        if (StringUtils.isEmpty(fieldName)) {
            this.fieldName = "参数args[" + argIndex + "]";
        }
        this.arg = arg;
        if (arg == null && item.init() != 0) {
            this.arg = item.init();
        }
    }

    public final static class LeafInfo {
        private int leaf;
        private String argDesc;
        private Condition matchCondition;
        private String desc;

        public LeafInfo(int leaf, String argDesc, Condition matchCondition) {
            this.leaf = leaf;
            this.argDesc = argDesc;
            this.matchCondition = matchCondition;
        }

        public int getLeaf() {
            return leaf;
        }

        public void setLeaf(int leaf) {
            this.leaf = leaf;
        }

        public String getArgDesc() {
            return argDesc;
        }

        public void setArgDesc(String argDesc) {
            this.argDesc = argDesc;
        }

        public Condition getMatchCondition() {
            return matchCondition;
        }

        public void setMatchCondition(Condition matchCondition) {
            this.matchCondition = matchCondition;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    public int getMinLen() {
        return minLen;
    }

    public void setMinLen(int minLen) {
        this.minLen = minLen;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public List<Integer> getIn() {
        return in;
    }

    public void setIn(List<Integer> in) {
        this.in = in;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getArgIndex() {
        return argIndex;
    }

    public void setArgIndex(int argIndex) {
        this.argIndex = argIndex;
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public void setParamType(Class<?> paramType) {
        this.paramType = paramType;
    }

    public List<Condition> getAllConditions() {
        return allConditions;
    }

    public void pushAllCondition(Condition condition) {
        if (allConditions == null) {
            allConditions = new ArrayList<Condition>();
        }
        allConditions.add(condition);
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }

    public Stack<LeafInfo> getMatchStack() {
        return matchStack;
    }

    public LeafInfo popMatchStack() {
        LeafInfo pop = this.matchStack.pop();
        this.leaf--;
        return pop;
    }

    public void pushMatchStack(LeafInfo leafInfo) {
        if (this.matchStack == null) {
            this.matchStack = new Stack<LeafInfo>();
        }
        this.matchStack.push(leafInfo);
        this.leaf++;
    }

    public AbstractRule getRule() {
        return rule;
    }

    public void setRule(AbstractRule rule) {
        this.rule = rule;
    }
}
