package com.os.biu.core.play.live;

import com.os.biu.core.media.IListenerAction;
import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class LiveListenerAction<T extends IMediaPlayerListener>
        implements IListenerAction<T> {

    @SourceType
    @Override
    public final int interest() {
        return SourceType.LIVE;
    }
}
