package com.os.biu.core.media;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.os.biu.core.source.SourceType;

public abstract class AbstractPlayer implements IMediaPlayer {

    protected final Application App;
    protected final Handler H;
    protected final IListenerActionRun ActionRun;

    protected AbstractPlayer(@NonNull Application app,
                             @NonNull Handler handler,
                             @NonNull IListenerActionRun actionRun) {
        App = app;
        H = handler;
        ActionRun = actionRun;
    }

    // 删除示例时需要做的清理工作
    protected void quit() {
    }

    @SourceType
    public abstract int getType();

    protected abstract void onHandleMessage(Message msg);
}
