package com.yzf.dop.validation;

import java.lang.annotation.*;

/**
 * Created by yezhufeng on 2016/11/21.
 */
@Target(value = ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NotCheck {

}
