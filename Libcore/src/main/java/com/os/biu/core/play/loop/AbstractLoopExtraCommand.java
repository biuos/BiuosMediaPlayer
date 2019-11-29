package com.os.biu.core.play.loop;

import com.os.biu.core.media.ICommandExtra;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLoopExtraCommand implements ICommandExtra {

    @Override
    @SourceType
    public final int from() {
        return SourceType.LOOP;
    }
}
