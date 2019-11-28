package com.os.biu.core.media;

import android.view.ViewGroup;

import com.os.biu.core.source.IMediaSource;

public interface IMediaPlayer {

    void setDataSource(IMediaSource source, ViewGroup vg, IMediaPlayerListener cb);

    // 例如:
    //   点播切集
    //   轮播切频道
    void setPlayTo(IMediaSource source);

    void start();

    void pause();

    void release();

    void sleep();

    void wakeUp();

    void seekTo(int whereTo);


}
