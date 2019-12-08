package com.os.biu.core.engine;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

public class AndroidMediaPlayer extends AbstractMediaPlayer {
    private final MediaPlayer mInternalMediaPlayer;
    private final AndroidMediaPlayerListenerHolder mInternalListenerAdapter;

    private final Object mInitLock = new Object();
    private boolean mIsReleased;

    public AndroidMediaPlayer() {
        synchronized (mInitLock) {
            mInternalMediaPlayer = new MediaPlayer();
        }
        mInternalMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mInternalListenerAdapter = new AndroidMediaPlayerListenerHolder(this);
        attachInternalListeners();
    }

    public final MediaPlayer getInternalMediaPlayer() {
        return mInternalMediaPlayer;
    }


    @Override
    public int engineCode() {
        return engine.ANDROID;
    }


    @Override
    public String engineName() {
        return "AndroidMediaPlayer";
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        synchronized (mInitLock) {
            if (!mIsReleased) {
                mInternalMediaPlayer.setDisplay(sh);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setSurface(Surface surface) {
        mInternalMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(context, uri);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws
            IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd)
            throws IOException, IllegalArgumentException, IllegalStateException {
        mInternalMediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws IOException,
            IllegalArgumentException, SecurityException, IllegalStateException {
        //mDataSource = path;

        Uri uri = Uri.parse(path);
        String scheme = uri.getScheme();
        if (!TextUtils.isEmpty(scheme) && scheme.equalsIgnoreCase("file")) {
            mInternalMediaPlayer.setDataSource(uri.getPath());
        } else {
            mInternalMediaPlayer.setDataSource(path);
        }
    }


    @Override
    public void prepareAsync() throws IllegalStateException {
        mInternalMediaPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mInternalMediaPlayer.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mInternalMediaPlayer.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mInternalMediaPlayer.pause();
    }

    @Override
    public void seekTo(int msec) throws IllegalStateException {
        mInternalMediaPlayer.seekTo(msec);
    }

    @Override
    public long getCurrentPosition() {
        try {
            return mInternalMediaPlayer.getCurrentPosition();
        } catch (IllegalStateException ignore) {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        try {
            return mInternalMediaPlayer.getDuration();
        } catch (IllegalStateException ignore) {
            return 0;
        }
    }

    @Override
    public void release() {
        mIsReleased = true;
        mInternalMediaPlayer.release();
        resetListeners();
        attachInternalListeners();
    }

    @Override
    public void reset() {
        try {
            mInternalMediaPlayer.reset();
        } catch (IllegalStateException ignore) {
        }

        resetListeners();
        attachInternalListeners();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        mInternalMediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    @Override
    public boolean isPlaying() {
        try {
            return mInternalMediaPlayer.isPlaying();
        } catch (IllegalStateException ignore) {
            return false;
        }
    }

    @Override
    public int getVideoWidth() {
        return mInternalMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mInternalMediaPlayer.getVideoHeight();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mInternalMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getVideoSarNum() {
        return 1;
    }

    @Override
    public int getVideoSarDen() {
        return 1;
    }


    private void attachInternalListeners() {
        mInternalMediaPlayer.setOnPreparedListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnBufferingUpdateListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnCompletionListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnSeekCompleteListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnVideoSizeChangedListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnErrorListener(mInternalListenerAdapter);
        mInternalMediaPlayer.setOnInfoListener(mInternalListenerAdapter);
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // class AndroidMediaPlayerListenerHolder
    //
    private static class AndroidMediaPlayerListenerHolder implements
            MediaPlayer.OnPreparedListener,
            MediaPlayer.OnCompletionListener,
            MediaPlayer.OnBufferingUpdateListener,
            MediaPlayer.OnSeekCompleteListener,
            MediaPlayer.OnVideoSizeChangedListener,
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnInfoListener {
        private final WeakReference<AndroidMediaPlayer> mWeakMediaPlayer;

        AndroidMediaPlayerListenerHolder(AndroidMediaPlayer player) {
            mWeakMediaPlayer = new WeakReference<>(player);
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self != null) {
                self.notifyOnBufferingUpdate(percent);
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self != null) {
                self.notifyOnCompletion();
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            return self != null && self.notifyOnError(what, extra);
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            return self != null && self.notifyOnInfo(what, extra);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self != null) {
                self.notifyOnPrepared();
            }
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self != null) {
                self.notifyOnSeekComplete();
            }
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            AndroidMediaPlayer self = mWeakMediaPlayer.get();
            if (self != null) {
                self.notifyOnVideoSizeChanged(width, height, 1, 1);
            }
        }
    }


}
