package com.os.biu.core.source;

public abstract class AbstractLoopMediaSource implements IMediaSource {

    @Override
    @SourceType
    public final int getSourceType() {
        return SourceType.LOOP;
    }

    @Override
    public IConfig getConfig() {
        return null;
    }
}
