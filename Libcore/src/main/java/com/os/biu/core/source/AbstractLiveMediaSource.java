package com.os.biu.core.source;

public abstract class AbstractLiveMediaSource implements IMediaSource {

    @Override
    @SourceType
    public final int getSourceType() {
        return SourceType.LIVE;
    }

    @Override
    public IConfig getConfig() {
        return null;
    }
}
