package com.bj58.daojia.dop2c.validation;


import com.bj58.daojia.dop2c.validation.condition.Condition;

import java.util.List;

/**
 * Created by pangming on 2016/11/26.
 */
public interface ParamType {

    /**
     * 获取指定参数类型条件
     * @return
     */
    public List<Condition> load();
}
