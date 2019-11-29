package com.os.biu.core.media;

import android.os.Message;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.os.biu.core.definition.IDefinition;
import com.os.biu.core.engine.IEngine;
import com.os.biu.core.source.IMediaSource;

import java.util.List;

public final class BiuosMediaPlayer extends BiuosMediaPlayerImpl {

    private static BiuosMediaPlayer INSTANCE = null;

    public static BiuosMediaPlayer getPlayer() {
        if (null == INSTANCE) {
            synchronized (BiuosMediaPlayer.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BiuosMediaPlayer();
                }
            }
        }
        return INSTANCE;
    }

    public static <T extends AbstractPlayer> BiuosMediaPlayer registerPlayer(T player) {
        synchronized (BiuosMediaPlayer.class) {
            getPlayer().registerPlayerImpl(player);
        }
        return INSTANCE;
    }

    private BiuosMediaPlayer() {
        super();
    }


    public void setDataSource(IMediaSource source, ViewGroup vg) {
        if (null == source) {
            return;
        }
        Message message = Message.obtain();
        message.what = CMD_DATA_SOURCE_SET;
        message.obj = new SourceInner(source, vg);
        H.sendMessage(message);
    }

    // 例如:
    //   点播切集
    //   轮播切频道
    public void setPlayTo(IMediaSource source) {
        if (source == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = CMD_DATA_SOURCE_NEXT;
        message.obj = source;
        H.sendMessage(message);
    }

    public void start() {
        H.sendEmptyMessage(CMD_START);
    }

    public void pause() {
        H.sendEmptyMessage(CMD_PAUSE);
    }

    public void release() {
        releaseImpl();
    }

    public void sleep() {
        H.sendEmptyMessage(CMD_SLEEP);
    }

    public void wakeUp() {
        H.sendEmptyMessage(CMD_WAKE_UP);
    }

    public void seekTo(int whereTo) {
        Message message = Message.obtain();
        message.what = CMD_SEEK_TO;
        message.arg1 = whereTo;
        H.sendMessage(message);
    }

    public void changeQuality(int uiQuality) {
        Message message = Message.obtain();
        message.what = CMD_CHG_DEFINITION;
        message.arg1 = uiQuality;
        H.sendMessage(message);
    }

    public void changeEngine(int engineCode) {
        Message message = Message.obtain();
        message.what = CMD_CHG_ENGINE;
        message.arg1 = engineCode;
        H.sendMessage(message);
    }

    @Nullable
    public List<IDefinition> supportDefinition() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return null;
        }
        return player.supportDefinition();
    }

    @Nullable
    public List<IEngine> supportEngine() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return null;
        }
        return player.supportEngine();
    }

    public int getCurrentPosition() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return -1;
        }
        return player.getCurrentPosition();
    }

    public int getDuration() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return -1;
        }
        return player.getDuration();
    }

    public int getVideoWidth() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return -1;
        }
        return player.getVideoWidth();
    }

    public int getVideoHeight() {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return -1;
        }
        return player.getVideoHeight();
    }

    public <CMD extends ICommandExtra> void runCommandInCallThread(CMD cmd) {
        if (null == cmd) {
            return;
        }

        runExtraCommand(cmd);
    }

    public <CMD extends ICommandExtra> void runCommandInBusinessThread(CMD cmd) {
        if (null == cmd) {
            return;
        }

        Message message = Message.obtain();
        message.what = CMD_RUN_EXTRA_CMD;
        message.obj = cmd;
        H.handleMessage(message);
    }


}
