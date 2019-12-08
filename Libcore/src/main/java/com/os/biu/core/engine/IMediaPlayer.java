package com.os.biu.core.engine;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public interface IMediaPlayer {

    int engineCode();

    String engineName();

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // MediaPlayer like API
    //

    void setDisplay(SurfaceHolder sh);

    void setDataSource(Context context, Uri uri) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(Context context, Uri uri, Map<String, String> headers) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setDataSource(FileDescriptor fd) throws
            IOException, IllegalArgumentException, IllegalStateException;

    void setDataSource(String path)
            throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;

    void pause() throws IllegalStateException;

    void setScreenOnWhilePlaying(boolean screenOn);

    int getVideoWidth();

    int getVideoHeight();

    boolean isPlaying();

    void seekTo(int msec) throws IllegalStateException;

    long getCurrentPosition();

    long getDuration();

    void release();

    void reset();

    void setVolume(float leftVolume, float rightVolume);

    void setSurface(Surface surface);


    //
    //
    // optional API
    //
    //

    int getVideoSarNum();

    int getVideoSarDen();


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // interface define
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //
    // interface OnPreparedListener
    interface OnPreparedListener {
        void onPrepared(IMediaPlayer mp);
    }

    //
    // interface OnCompletionListener
    interface OnCompletionListener {
        void onCompletion(IMediaPlayer mp);
    }

    //
    // interface OnBufferingUpdateListener
    interface OnBufferingUpdateListener {
        void onBufferingUpdate(IMediaPlayer mp, int percent);
    }

    //
    // interface OnSeekCompleteListener
    interface OnSeekCompleteListener {
        void onSeekComplete(IMediaPlayer mp);
    }

    //
    // interface OnVideoSizeChangedListener
    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den);
    }


    //
    // interface OnErrorListener
    interface OnErrorListener {
        boolean onError(IMediaPlayer mp, int what, int extra);
    }

    // interface OnInfoListener
    interface OnInfoListener {
        boolean onInfo(IMediaPlayer mp, int what, int extra);
    }

}
