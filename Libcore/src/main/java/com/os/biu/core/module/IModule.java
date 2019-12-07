package com.os.biu.core.module;

public interface IModule {

    // 模块名称
    String name();

    // 模块代码
    // 该 code 需要与注解 @WorkModule 一致
    int code();

    // 模块退出执行函数
    void quit();
}
