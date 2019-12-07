package com.os.biu.core.media;

import android.view.SurfaceHolder;

import com.os.biu.core.source.SourceType;

public interface IMediaPlayerListener extends SurfaceHolder.Callback {

    // 播放毁掉注册源
    // 例如: 点播UI 直播UI 轮播UI 统计模块
    String from();


    // 该回调事件关注那个播放源, 必须注册感兴趣源
    // 例如： 点播UI - 关注从点播源事件
    //       直播UI - 关注从直播源事件
    //       轮播UI - 关注从轮播源事件
    //       统计模块  关注从 点播源 直播源 轮播源 事件，建议统计模块写三个
    // 该值内容与 IListenerAction.actionFrom()
    // 可以使用如下方式: SourceType.LIVE | SourceType.LOOP 类似这样调用方式
    int attentionTo();

    void onKilled(@SourceType int oldType, @SourceType int newType);

    void onZygote(@SourceType int type);
}
