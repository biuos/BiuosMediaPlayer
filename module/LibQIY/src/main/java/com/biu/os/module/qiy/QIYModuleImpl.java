package com.biu.os.module.qiy;

import android.app.Application;
import android.os.Message;

import androidx.annotation.NonNull;

import com.biu.os.media.business.module.module;
import com.os.biu.core.annitation.WorkModule;
import com.os.biu.core.module.AbstractWorkModule;

@WorkModule(code = module.QIY, canWork = true, create = false)
final class QIYModuleImpl extends AbstractWorkModule {

    protected QIYModuleImpl(@NonNull Application app) {
        super(app);
    }

    @Override
    protected void onHandleMessage(Message msg) {
    }

    @Override
    public String name() {
        return "QIY";
    }

    @Override
    public int code() {
        return module.QIY;
    }
}
