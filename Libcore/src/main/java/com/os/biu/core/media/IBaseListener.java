package com.os.biu.core.media;

public interface IBaseListener {

    void registerListener(IMediaPlayerListener listener);

    void unRegisterListener(IMediaPlayerListener listener);
}
