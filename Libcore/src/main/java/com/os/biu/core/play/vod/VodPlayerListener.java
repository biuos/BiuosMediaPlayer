package com.os.biu.core.play.vod;

import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class VodPlayerListener implements IMediaPlayerListener {

    @SourceType
    @Override
    public final int registerFrom() {
        return SourceType.VOD;
    }

    @Override
    public String from() {
        return "vod";
    }
}
