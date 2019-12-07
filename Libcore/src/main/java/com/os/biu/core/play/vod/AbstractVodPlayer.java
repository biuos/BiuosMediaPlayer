package com.os.biu.core.play.vod;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.media.IListenerActionRun;
import com.os.biu.core.source.SourceType;

public abstract class AbstractVodPlayer extends AbstractPlayer {
    protected AbstractVodPlayer(@NonNull Application app,
                                @NonNull Handler handler,
                                @NonNull IListenerActionRun actionRun) {
        super(app, handler, actionRun);
    }

    @SourceType
    @Override
    public final int getType() {
        return SourceType.VOD;
    }


    protected final <T extends VodPlayerListener, U extends VodListenerAction<T>>
    void runActionInCallThread(@NonNull U action) {
        if (null != ActionRun) {
            ActionRun.runActionInCallThread(action);
        }
    }

    protected final <T extends VodPlayerListener, U extends VodListenerAction<T>>
    void runActionInBusinessThread(@NonNull U action) {
        if (null != ActionRun) {
            ActionRun.runActionInBusinessThread(action);
        }
    }

}
