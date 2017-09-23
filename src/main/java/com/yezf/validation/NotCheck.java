package com.yezf.validation;

import java.lang.annotation.*;

/**
 * Created by pangming on 2016/11/21.
 */
@Target(value = ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NotCheck {

}
