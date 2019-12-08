package com.biu.os.module.yk;

import android.app.Application;
import android.os.Message;

import androidx.annotation.NonNull;

import com.biu.os.media.business.module.module;
import com.os.biu.core.annitation.WorkModule;
import com.os.biu.core.module.AbstractWorkModule;

@WorkModule(code = module.YOUKU, create = true)
final class YouKuModuleImpl extends AbstractWorkModule {
    protected YouKuModuleImpl(@NonNull Application app) {
        super(app);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    @Override
    public String name() {
        return "YouKu";
    }

    @Override
    public int code() {
        return module.YOUKU;
    }
}
