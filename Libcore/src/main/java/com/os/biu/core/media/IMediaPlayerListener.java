package com.os.biu.core.media;

import android.view.SurfaceHolder;

import com.os.biu.core.source.SourceType;

public interface IMediaPlayerListener extends SurfaceHolder.Callback2 {

    void onKilled(@SourceType int oldType, @SourceType int newType);

    void onZygote(@SourceType int type);
}
