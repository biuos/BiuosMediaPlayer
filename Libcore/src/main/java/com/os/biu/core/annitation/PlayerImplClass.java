package com.os.biu.core.annitation;

import com.os.biu.core.media.AbstractPlayer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlayerImplClass {

    boolean canWork() default true;

    String desc() default "";

    Class<? extends AbstractPlayer> clazz();
}
