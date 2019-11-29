package com.os.biu.core.media;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.os.biu.core.source.IMediaSource;
import com.os.biu.core.source.SourceType;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

class BiuosMediaPlayerImpl {
    private final static String TAG = "BiuosMediaPlayerImpl";

    static final int CMD_DATA_SOURCE_SET = 1000;
    static final int CMD_DATA_SOURCE_NEXT = 1010;
    static final int CMD_START = 2000;
    static final int CMD_PAUSE = 3000;
    static final int CMD_RELEASE = 4000;
    static final int CMD_SLEEP = 5000;
    static final int CMD_WAKE_UP = 6000;
    static final int CMD_SEEK_TO = 7000;
    static final int CMD_CHG_DEFINITION = 8000;
    static final int CMD_CHG_ENGINE = 9000;
    static final int CMD_RUN_EXTRA_CMD = 10000;


    protected final Handler H;
    private final IListenerActionRun ActionRun;

    private final HashSet<IMediaPlayerListener> mListenerSet = new HashSet<>(4);

    BiuosMediaPlayerImpl() {
        HandlerThread businessThread = new HandlerThread("BusinessThread");
        businessThread.start();
        H = new WorkHandler(this, businessThread.getLooper());

        BusinessSchedulers.from(businessThread.getLooper());

        ActionRun = new IListenerActionRunImpl(this);
    }

    private final SparseArray<AbstractPlayer> mPlayerImpl = new SparseArray<>(3);

    <T extends AbstractPlayer> void registerPlayerImpl(T player) {
        synchronized (mPlayerImpl) {
            AbstractPlayer current = mPlayerImpl.get(player.getType());
            if (current != null) {
                mPlayerImpl.delete(current.getType());
            }

            mPlayerImpl.append(player.getType(), player);
            player.setHandle(H);
            player.setActionRun(ActionRun);
        }
    }

    private final AtomicReference<AbstractPlayer> mCurrentPlayer = new AtomicReference<>(null);

    <T extends AbstractPlayer> void setCurrentPlayer(T player) {
        mCurrentPlayer.set(player);
    }

    AbstractPlayer getLivePlayer() {
        synchronized (mPlayerImpl) {
            return mPlayerImpl.get(SourceType.LIVE);
        }
    }

    AbstractPlayer getLoop() {
        synchronized (mPlayerImpl) {
            return mPlayerImpl.get(SourceType.LOOP);
        }
    }

    AbstractPlayer getVod() {
        synchronized (mPlayerImpl) {
            return mPlayerImpl.get(SourceType.VOD);
        }
    }

    AbstractPlayer getCurrentPlayer() {
        return mCurrentPlayer.get();
    }

    private void setDataSourceImpl(SourceInner src) {
    }

    private void setPlayToImpl(@NonNull IMediaSource source) {
    }

    private void startImpl() {
    }

    private void pauseImpl() {
    }

    void releaseImpl() {
    }

    private void sleepImpl() {
    }

    private void wakeUpImpl() {
    }

    private void seekToImpl(int whereTo) {
    }

    private void changeQualityImpl(int uiQuality) {
    }

    private void changeEngine(int engineCode) {
    }

    final <CMD extends ICommandExtra> void runExtraCommand(@NonNull CMD cmd) {
        AbstractPlayer player = getCurrentPlayer();
        if (null == player) {
            return;
        }

        if (cmd.from() != player.getType()) {
            return;
        }

        player.runExtraCommand(cmd);
    }


    protected void onHandleMessage(Message msg) {
        switch (msg.what) {
            case CMD_RUN_EXTRA_CMD: {
                if (msg.obj instanceof ICommandExtra) {
                    runExtraCommand((ICommandExtra) msg.obj);
                }
                return;
            }

            case CMD_DATA_SOURCE_SET: {
                if (msg.obj instanceof SourceInner) {
                    setDataSourceImpl((SourceInner) msg.obj);
                }
                return;
            }

            case CMD_DATA_SOURCE_NEXT: {
                if (msg.obj instanceof IMediaSource) {
                    setPlayToImpl((IMediaSource) msg.obj);
                }
                return;
            }

            case CMD_START: {
                startImpl();
                return;
            }

            case CMD_PAUSE: {
                pauseImpl();
                return;
            }

            case CMD_SLEEP: {
                sleepImpl();
                return;
            }

            case CMD_WAKE_UP: {
                wakeUpImpl();
                return;
            }

            case CMD_SEEK_TO: {
                seekToImpl(msg.arg1);
                return;
            }

            case CMD_CHG_DEFINITION: {
                changeQualityImpl(msg.arg1);
                return;
            }

            case CMD_CHG_ENGINE: {
                changeEngine(msg.arg1);
                return;
            }
        }

        // 分发事件到各个播放模块
        AbstractPlayer player = mCurrentPlayer.get();
        if (null != player) {
            player.onHandleMessage(msg);
        }
    }


    public final void registerListener(IMediaPlayerListener listener) {
        if (null == listener) {
            return;
        }
        synchronized (mListenerSet) {
            mListenerSet.add(listener);
        }
    }


    public final void unRegisterListener(IMediaPlayerListener listener) {
        if (null == listener) {
            return;
        }
        synchronized (mListenerSet) {
            mListenerSet.remove(listener);
        }
    }


    private <T extends IMediaPlayerListener>
    void runActionInBusinessThread(final @NonNull IListenerAction<T> action) {
        H.post(() -> runActionImpl(action));
    }

    private <T extends IMediaPlayerListener>
    void runActionInCallThread(@NonNull IListenerAction<T> action) {
        runActionImpl(action);
    }

    private <T extends IMediaPlayerListener>
    void runActionImpl(@NonNull IListenerAction<T> action) {
        synchronized (mListenerSet) {
            for (IMediaPlayerListener cb : mListenerSet) {
                long beginTime = System.currentTimeMillis();
                try {
                    action.doAction((T) cb);
                } catch (Exception e) {
                    Log.e(TAG, "exec callback function failed, run next cb");
                } finally {
                    long endTime = System.currentTimeMillis();
                    Log.i(TAG, "Run callback [" + cb.from() + "@" + cb.hashCode() + "] take " + (endTime - beginTime) + "ms.");
                }
            }
        }
    }

    private static class IListenerActionRunImpl implements IListenerActionRun {
        private final BiuosMediaPlayerImpl impl;

        private IListenerActionRunImpl(BiuosMediaPlayerImpl impl) {
            this.impl = impl;
        }

        @Override
        public <T extends IMediaPlayerListener>
        void runActionInCallThread(@NonNull IListenerAction<T> action) {
            impl.runActionInCallThread(action);
        }

        @Override
        public <T extends IMediaPlayerListener>
        void runActionInBusinessThread(@NonNull IListenerAction<T> action) {
            impl.runActionInBusinessThread(action);
        }
    }

    private static class WorkHandler extends Handler {
        private final BiuosMediaPlayerImpl impl;

        WorkHandler(BiuosMediaPlayerImpl impl, Looper looper) {
            super(looper);
            this.impl = impl;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            impl.onHandleMessage(msg);
        }
    }


    static class SourceInner {
        private final IMediaSource source;
        private final ViewGroup viewGroup;

        SourceInner(IMediaSource src, ViewGroup vg) {
            source = src;
            viewGroup = vg;
        }
    }

}
