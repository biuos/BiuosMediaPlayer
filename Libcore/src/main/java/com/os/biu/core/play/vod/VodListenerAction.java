package com.os.biu.core.play.vod;

import com.os.biu.core.media.IListenerAction;
import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class VodListenerAction<T extends IMediaPlayerListener>
        implements IListenerAction<T> {

    @SourceType
    @Override
    public final int actionFrom() {
        return SourceType.VOD;
    }
}
