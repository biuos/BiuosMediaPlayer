package com.os.biu.core.play.live;

import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class LivePlayerListener implements IMediaPlayerListener {

    @SourceType
    @Override
    public final int registerFrom() {
        return SourceType.LIVE;
    }

    @Override
    public String from() {
        return "live";
    }
}
