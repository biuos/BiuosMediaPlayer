package com.os.biu.core.play.vod;

import androidx.annotation.NonNull;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.source.SourceType;

public abstract class AbstractVodPlayer extends AbstractPlayer {

    @SourceType
    @Override
    public final int getType() {
        return SourceType.VOD;
    }


    protected final <T extends VodPlayerListener, U extends VodListenerAction<T>>
    void runActionInCallThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInCallThread(action);
        }
    }

    protected final <T extends VodPlayerListener, U extends VodListenerAction<T>>
    void runActionInBusinessThread(@NonNull U action) {
        if (null != mActionRun) {
            mActionRun.runActionInBusinessThread(action);
        }
    }

}
