package com.os.biu.core.media;

import com.os.biu.core.source.SourceType;

public interface ICommandExtra {

    // 向那么播放源发送命令, 例如:
    //  SourceType.LIVE
    //  SourceType.VOD
    //  SourceType.LOOP
    @SourceType
    int commandTo();


    // 相同播放源能唯一区分需要执行的命令
    int commandCode();
}
