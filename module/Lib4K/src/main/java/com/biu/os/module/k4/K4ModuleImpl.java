package com.biu.os.module.k4;

import android.app.Application;
import android.os.Message;

import androidx.annotation.NonNull;

import com.biu.os.media.business.module.module;
import com.os.biu.core.annitation.WorkModule;
import com.os.biu.core.module.AbstractWorkModule;

@WorkModule(author = "YourName", code = module.K4, canWork = true, create = false)
final class K4ModuleImpl extends AbstractWorkModule {

    protected K4ModuleImpl(@NonNull Application app) {
        super(app);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    @Override
    public String name() {
        return "4K";
    }

    @Override
    public int code() {
        return module.K4;
    }
}
