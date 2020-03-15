package com.f1mert.lib.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//作用在方法上
@Retention(RetentionPolicy.RUNTIME)//jvm运行时通过反获取注解的值
public @interface IPermission {
    int value();
}
