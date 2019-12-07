package com.os.biu.core.play.live;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.os.biu.core.media.AbstractPlayer;
import com.os.biu.core.media.IListenerActionRun;
import com.os.biu.core.source.SourceType;

public abstract class AbstractLivePlayer extends AbstractPlayer {
    protected AbstractLivePlayer(@NonNull Application app,
                                 @NonNull Handler handler,
                                 @NonNull IListenerActionRun actionRun) {
        super(app, handler, actionRun);
    }

    @Override
    @SourceType
    public final int getType() {
        return SourceType.LIVE;
    }

    protected final <T extends LivePlayerListener, U extends LiveListenerAction<T>>
    void runActionInCallThread(@NonNull U action) {
        if (null != ActionRun) {
            ActionRun.runActionInCallThread(action);
        }
    }

    protected final <T extends LivePlayerListener, U extends LiveListenerAction<T>>
    void runActionInBusinessThread(@NonNull U action) {
        if (null != ActionRun) {
            ActionRun.runActionInBusinessThread(action);
        }
    }
}
