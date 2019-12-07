package com.os.biu.core.media;

import androidx.annotation.NonNull;

import com.os.biu.core.source.SourceType;

public interface IListenerAction<T extends IMediaPlayerListener> {

    void doAction(@NonNull T cb);

    @SourceType
    int actionFrom();
}
