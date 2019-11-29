package com.os.biu.core.play.live;

import com.os.biu.core.media.ICommandExtra;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLiveExtraCommand implements ICommandExtra {

    @Override
    @SourceType
    public final int from() {
        return SourceType.LIVE;
    }
}
