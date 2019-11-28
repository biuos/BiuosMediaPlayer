package com.os.biu.core.play.vod;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractVodPlayer extends AbstractPlayer {

    @SourceType
    @Override
    public final int getType() {
        return SourceType.VOD;
    }
}
