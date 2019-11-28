package com.os.biu.core.source;

public abstract class AbstractVodMediaSource implements IMediaSource {

    @Override
    @SourceType
    public final int getSourceType() {
        return SourceType.VOD;
    }

    @Override
    public IConfig getConfig() {
        return null;
    }
}
