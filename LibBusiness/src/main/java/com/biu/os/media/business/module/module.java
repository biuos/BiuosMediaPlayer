package com.biu.os.media.business.module;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.biu.os.media.business.module.module.K4;
import static com.biu.os.media.business.module.module.PP;
import static com.biu.os.media.business.module.module.QIY;
import static com.biu.os.media.business.module.module.YOUKU;

@Target({ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.LOCAL_VARIABLE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@IntDef({K4, PP, QIY, YOUKU})
public @interface module {

    // 4K
    int K4 = 100;

    int PP = 200;

    int QIY = 300;

    int YOUKU = 400;
}
