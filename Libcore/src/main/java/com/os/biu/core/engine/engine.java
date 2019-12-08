package com.os.biu.core.engine;


import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.os.biu.core.engine.engine.ANDROID;
import static com.os.biu.core.engine.engine.IJK;
import static com.os.biu.core.engine.engine.IJK_SELF;
import static com.os.biu.core.engine.engine.SELF;


@Target({ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.LOCAL_VARIABLE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@IntDef({ANDROID, IJK, IJK_SELF, SELF})
public @interface engine {

    int ANDROID = 100;

    int IJK = 200;

    int IJK_SELF = 300;

    int SELF = 400;
}
