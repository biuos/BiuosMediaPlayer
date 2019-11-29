package com.os.biu.core.play.loop;

import androidx.annotation.NonNull;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLoopPlayer extends AbstractPlayer {

    @Override
    @SourceType
    public final int getType() {
        return SourceType.LOOP;
    }


    protected final <T extends LoopPlayerListener, U extends LoopListenerAction<T>>
    void runActionInCallThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInCallThread(action);
        }
    }

    protected final <T extends LoopPlayerListener, U extends LoopListenerAction<T>>
    void runActionInBusinessThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInBusinessThread(action);
        }
    }
}
