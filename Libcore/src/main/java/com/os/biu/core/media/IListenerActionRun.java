package com.os.biu.core.media;

import androidx.annotation.NonNull;

public interface IListenerActionRun {

    <T extends IMediaPlayerListener> void runActionInCallThread(@NonNull IListenerAction<T> action);

    <T extends IMediaPlayerListener> void runActionInBusinessThread(@NonNull IListenerAction<T> action);
}
