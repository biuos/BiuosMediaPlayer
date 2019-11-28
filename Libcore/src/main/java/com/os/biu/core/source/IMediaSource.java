package com.os.biu.core.source;

public interface IMediaSource {

    @SourceType
    int getSourceType();

    IConfig getConfig();
}
