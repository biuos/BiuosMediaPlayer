package com.os.biu.core.annitation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkModule {

    // 改模块是否正常工作
    boolean canWork() default true;

    // 初始化时创建示例
    boolean create() default false;

    // 作者
    String author() default "";

    // 模块唯一值
    int code();
}
