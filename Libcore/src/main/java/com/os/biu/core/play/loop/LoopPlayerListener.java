package com.os.biu.core.play.loop;

import com.os.biu.core.media.IMediaPlayerListener;
import com.os.biu.core.source.SourceType;

public abstract class LoopPlayerListener implements IMediaPlayerListener {

    @SourceType
    @Override
    public final int registerFrom() {
        return SourceType.LOOP;
    }

    @Override
    public String from() {
        return "loop";
    }
}
