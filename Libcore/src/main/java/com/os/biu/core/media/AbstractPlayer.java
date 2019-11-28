package com.os.biu.core.media;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.os.biu.core.source.SourceType;

public abstract class AbstractPlayer implements IMediaPlayer {

    protected Handler H = null;

    final void setHandle(@NonNull Handler handler) {
        H = handler;
    }

    @SourceType
    public abstract int getType();

    protected abstract void onHandleMessage(Message msg);
}
