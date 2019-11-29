package com.os.biu.core.media;

import android.view.SurfaceHolder;

import com.os.biu.core.source.SourceType;

public interface IMediaPlayerListener extends SurfaceHolder.Callback {

    // 调用来源
    String from();

    // 从那个功能方注册
    // - 从 直播
    // - 从 轮播
    // - 从 点播
    int registerFrom();

    void onKilled(@SourceType int oldType, @SourceType int newType);

    void onZygote(@SourceType int type);
}
