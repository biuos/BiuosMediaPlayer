package com.os.biu.core.definition;

public interface IDefinition {

    // UI 显示的画质质量
    int getUiQuality();

    // 画质权益:
    // - 免费
    // - 登录可观看
    // - 购买可观看
    int qualityRights();

}
