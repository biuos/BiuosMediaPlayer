package com.os.biu.core.play.loop;

import com.os.biu.core.media.IListenerAction;
import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class LoopListenerAction<T extends IMediaPlayerListener>
        implements IListenerAction<T> {

    @SourceType
    @Override
    public final int interest() {
        return SourceType.LOOP;
    }
}
