package com.os.biu.app;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.os.biu.core.definition.IDefinition;
import com.os.biu.core.engine.IMediaPlayer;
import com.os.biu.core.media.ICommandExtra;
import com.os.biu.core.media.IListenerActionRun;
import com.os.biu.core.play.vod.AbstractVodPlayer;
import com.os.biu.core.source.IMediaSource;

import java.util.List;

class BadVodPlayer extends AbstractVodPlayer {

    protected BadVodPlayer(@NonNull Application app, @NonNull Handler handler, @NonNull IListenerActionRun actionRun) {
        super(app, handler, actionRun);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    @Override
    public void setDataSource(IMediaSource source, ViewGroup vg) {

    }

    @Override
    public void setPlayTo(IMediaSource source) {

    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void release() {

    }

    @Override
    public void sleep() {

    }

    @Override
    public void wakeUp() {

    }

    @Override
    public void seekTo(int whereTo) {

    }

    @Override
    public void changeQuality(int uiQuality) {

    }

    @Override
    public void changeEngine(int engineCode) {

    }

    @Override
    public List<IDefinition> supportDefinition() {
        return null;
    }

    @Override
    public List<IMediaPlayer> supportEngine() {
        return null;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    @Override
    public <CMD extends ICommandExtra> void runExtraCommand(CMD cmd) {

    }
}
