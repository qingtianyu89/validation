package com.yzf.dop.validation;


import com.yzf.dop.validation.condition.Condition;

import java.util.List;

/**
 * Created by yezhufeng on 2016/11/26.
 */
public interface ParamType {

    /**
     * 获取指定参数类型条件
     * @return
     */
    public List<Condition> load();
}
