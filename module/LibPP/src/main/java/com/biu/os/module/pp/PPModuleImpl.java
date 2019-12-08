package com.biu.os.module.pp;

import android.app.Application;
import android.os.Message;

import androidx.annotation.NonNull;

import com.biu.os.media.business.module.module;
import com.os.biu.core.annitation.WorkModule;
import com.os.biu.core.module.AbstractWorkModule;

@WorkModule(canWork = true, create = false, author = "Name", code = module.PP)
final class PPModuleImpl extends AbstractWorkModule {

    protected PPModuleImpl(@NonNull Application app) {
        super(app);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    @Override
    public String name() {
        return "PP";
    }

    @Override
    public int code() {
        return module.PP;
    }
}
