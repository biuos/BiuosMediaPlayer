package com.os.biu.core.media;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.os.biu.core.source.SourceType;

import java.util.concurrent.atomic.AtomicReference;

class BiuosMediaPlayerImpl {

    protected final Handler H;

    BiuosMediaPlayerImpl() {
        HandlerThread businessThread = new HandlerThread("BusinessThread");
        businessThread.start();
        H = new WorkHandler(this, businessThread.getLooper());
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


    protected void onHandleMessage(Message msg) {
        AbstractPlayer player = mCurrentPlayer.get();
        if (null != player) {
            player.onHandleMessage(msg);
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

}
