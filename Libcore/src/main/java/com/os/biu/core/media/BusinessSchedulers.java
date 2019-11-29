package com.os.biu.core.media;

import android.os.Looper;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

public final class BusinessSchedulers {

    private static Scheduler INSTANCE = null;

    static Scheduler from(Looper looper) {
        if (null == INSTANCE) {
            synchronized (BusinessSchedulers.class) {
                INSTANCE = AndroidSchedulers.from(looper);
            }
        }
        return INSTANCE;
    }

    public static Scheduler business() {
        return INSTANCE;
    }


    private BusinessSchedulers() {
        throw new AssertionError("No instances.");
    }
}

