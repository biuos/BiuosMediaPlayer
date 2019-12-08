package com.os.biu.core.media;

import android.view.ViewGroup;

import com.os.biu.core.definition.IDefinition;
import com.os.biu.core.source.IMediaSource;

import java.util.List;

public interface IMediaPlayer {

    void setDataSource(IMediaSource source, ViewGroup vg);

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

    void changeQuality(int uiQuality);

    void changeEngine(int engineCode);

    List<IDefinition> supportDefinition();

    List<com.os.biu.core.engine.IMediaPlayer> supportEngine();

    int getCurrentPosition();

    int getDuration();

    int getVideoWidth();

    int getVideoHeight();

    <CMD extends ICommandExtra> void runExtraCommand(CMD cmd);
}
