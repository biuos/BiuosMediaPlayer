package com.os.biu.core.media;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.os.biu.core.module.IModule;
import com.os.biu.core.source.IMediaSource;
import com.os.biu.core.source.SourceType;

import java.util.HashSet;

class BiuMediaPlayerImpl {
    private final static String TAG = "BiuosImpl";

    // 固定 cmd 值
    private final static int CMD_INIT = 500;
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

    Application mApp;
    final Handler H;
    final IListenerActionRunImpl mAction;
    final Manager mManager;


    BiuMediaPlayerImpl() {
        HandlerThread businessThread = new HandlerThread("BusinessThread");
        businessThread.start();
        H = new WorkHandler(this, businessThread.getLooper());

        BusinessSchedulers.from(businessThread.getLooper());

        mAction = new IListenerActionRunImpl(this);
        mManager = new Manager(this);
    }


    // 通过模块 code 查找模块
    // 该 code 就是注解 @WorkModule() 的 code
    public final IModule getModuleByCode(int code) {
        return mManager.getModuleByCode(code);
    }


    // 播放前的初始化, 涉及:
    // 播放类型: VOD LIVE LOOP 扫描注册
    // 业务模块: 各个业务模块扫描, 实例化
    public final void init(Context context) {
        if (mManager.inited.get()) {
            Log.w(TAG, "All had inited");
            return;
        }

        synchronized (mManager.inited) {
            mApp = (Application) context.getApplicationContext();
            H.removeMessages(CMD_INIT);
            H.sendEmptyMessage(CMD_INIT);
        }
    }


    private void initImpl() {
        mManager.init(mApp);
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
        AbstractPlayer player = mManager.getCurrentPlayer();
        if (null == player) {
            return;
        }

        if (cmd.commandTo() != player.getType()) {
            return;
        }

        player.runExtraCommand(cmd);
    }


    private void onHandleMessage(Message msg) {
        switch (msg.what) {
            case CMD_INIT: {
                initImpl();
                return;
            }

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
        AbstractPlayer player = mManager.getCurrentPlayer();
        if (null != player) {
            player.onHandleMessage(msg);
        }
    }


    public final void addListener(IMediaPlayerListener listener) {
        mAction.addListener(listener);
    }


    public final void removeListener(IMediaPlayerListener listener) {
        mAction.removeListener(listener);
    }

    private <T extends IMediaPlayerListener>
    void runActionInBusinessThread(final @NonNull IListenerAction<T> action) {
        mAction.runActionInBusinessThread(action);
    }

    private <T extends IMediaPlayerListener>
    void runActionInCallThread(@NonNull IListenerAction<T> action) {
        mAction.runActionInCallThread(action);
    }


    //==============================================================================================
    // class IListenerActionRunImpl
    //
    private static class IListenerActionRunImpl implements IListenerActionRun {
        private final BiuMediaPlayerImpl impl;
        private final HashSet<IMediaPlayerListener> mListenerSet = new HashSet<>(4);

        private IListenerActionRunImpl(BiuMediaPlayerImpl impl) {
            this.impl = impl;
        }

        private void addListener(IMediaPlayerListener listener) {
            if (null == listener) {
                return;
            }
            synchronized (mListenerSet) {
                mListenerSet.add(listener);
            }
        }

        private void removeListener(IMediaPlayerListener listener) {
            if (null == listener) {
                return;
            }
            synchronized (mListenerSet) {
                mListenerSet.remove(listener);
            }
        }

        private void runActionImpl(@NonNull IListenerAction action) {
            synchronized (mListenerSet) {
                for (IMediaPlayerListener cb : mListenerSet) {
                    long beginTime = System.currentTimeMillis();
                    try {
                        final @SourceType int from = action.actionFrom();
                        final int attention = cb.attentionTo();
                        Log.i(TAG, ">>> Action.from:" + from + " --> Listener.from: " + cb.from() + ", attention:" + attention);

                        // 该 action 需要发送给所有 listener
                        if (from == 0) {
                            action.doAction(cb);
                        }
                        // 将 action 发送给感兴趣 listener
                        else if ((cb.attentionTo() & from) == from) {
                            action.doAction(cb);
                        }
                        // 其他未注册感兴趣 listener
                        else {
                            Log.w(TAG, ">>> [CB.DROP]: Listener: " + cb.from() + " not interest in action from " + from);
                        }
                    } catch (Exception e) {
                        Log.w(TAG, ">>> [CB.WARN]: Dispatch listener failed, do next.");
                    } finally {
                        long endTime = System.currentTimeMillis();
                        Log.i(TAG, ">>> Dispatch listener [0x" + cb.hashCode() + "] take:" + (endTime - beginTime) + "ms.");
                    }
                }
            }
        }


        @Override
        public <T extends IMediaPlayerListener>
        void runActionInCallThread(@NonNull IListenerAction<T> action) {
            runActionImpl(action);
        }

        @Override
        public <T extends IMediaPlayerListener>
        void runActionInBusinessThread(@NonNull IListenerAction<T> action) {
            impl.H.post(() -> runActionImpl(action));
        }
    }


    //==============================================================================================
    // class WorkHandler
    //
    private static class WorkHandler extends Handler {
        private final BiuMediaPlayerImpl impl;

        WorkHandler(BiuMediaPlayerImpl impl, Looper looper) {
            super(looper);
            this.impl = impl;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            impl.onHandleMessage(msg);
        }
    }


    //==============================================================================================
    // class SourceInner
    //
    static class SourceInner {
        private final IMediaSource source;
        private final ViewGroup viewGroup;

        SourceInner(IMediaSource src, ViewGroup vg) {
            source = src;
            viewGroup = vg;
        }
    }

}
