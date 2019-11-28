package com.os.biu.core.ui;

import com.os.biu.core.source.SourceType;

public abstract class AbstractUiInfo4Live implements IUiInfo {

    @Override
    @SourceType
    public final int getSourceType() {
        return SourceType.LIVE;
    }
}
