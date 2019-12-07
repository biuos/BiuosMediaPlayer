package com.os.biu.core.source;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.os.biu.core.source.SourceType.LIVE;
import static com.os.biu.core.source.SourceType.LOOP;
import static com.os.biu.core.source.SourceType.UNKNOWN;
import static com.os.biu.core.source.SourceType.VOD;

@Target({ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.LOCAL_VARIABLE,
        ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@IntDef({UNKNOWN, LIVE, LOOP, VOD})
public @interface SourceType {
    int UNKNOWN = 0;

    int LIVE = 1 << 2;

    int LOOP = 1 << 3;

    int VOD = 1 << 4;
}
