package com.os.biu.core.play.live;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLivePlayer extends AbstractPlayer {

    @Override
    @SourceType
    public final int getType() {
        return SourceType.LIVE;
    }
}
