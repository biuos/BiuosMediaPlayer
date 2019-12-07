package com.os.biu.core.module;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public abstract class AbstractWorkModule implements IModule {

    protected final Application App;
    protected final Handler H;

    private final HandlerThread mWorkThread;

    protected AbstractWorkModule(@NonNull Application app) {
        App = app;

        mWorkThread = new HandlerThread("THD@" + name() + "_" + code());
        mWorkThread.start();
        H = new ModuleHandler(this, mWorkThread.getLooper());
    }

    // 模块退出执行函数
    @Override
    public void quit() {
        mWorkThread.quit();
    }

    protected abstract void onHandleMessage(Message msg);

    //==============================================================================================
    // class ModuleHandler
    //
    private static class ModuleHandler extends Handler {
        private final AbstractWorkModule module;

        ModuleHandler(AbstractWorkModule m, Looper looper) {
            super(looper);
            module = m;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            module.onHandleMessage(msg);
        }
    }
}
