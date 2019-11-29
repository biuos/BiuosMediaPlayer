package com.os.biu.core.media;

import com.os.biu.core.source.SourceType;

public interface ICommandExtra {

    @SourceType
    int from();

    // 唯一区分的命令 code
    int command();
}
