package com.os.biu.core.play.loop;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLoopPlayer extends AbstractPlayer {

    @Override
    @SourceType
    public final int getType() {
        return SourceType.LOOP;
    }
}
