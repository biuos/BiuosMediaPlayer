package com.os.biu.core.play.live;

import androidx.annotation.NonNull;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLivePlayer extends AbstractPlayer {

    @Override
    @SourceType
    public final int getType() {
        return SourceType.LIVE;
    }

    protected final <T extends LivePlayerListener, U extends LiveListenerAction<T>>
    void runActionInCallThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInCallThread(action);
        }
    }

    protected final <T extends LivePlayerListener, U extends LiveListenerAction<T>>
    void runActionInBusinessThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInBusinessThread(action);
        }
    }
}
