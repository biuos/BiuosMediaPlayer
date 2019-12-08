package com.os.biu.core.media;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.os.biu.core.annitation.PlayerImplClass;
import com.os.biu.core.annitation.WorkModule;
import com.os.biu.core.module.AbstractWorkModule;
import com.os.biu.core.module.IModule;
import com.os.biu.core.play.live.AbstractLivePlayer;
import com.os.biu.core.play.loop.AbstractLoopPlayer;
import com.os.biu.core.play.vod.AbstractVodPlayer;
import com.os.biu.core.source.SourceType;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import dalvik.system.DexFile;

final class Manager {
    private static final String TAG = "Manager";

    private final BiuMediaPlayerImpl impl;

    Manager(BiuMediaPlayerImpl impl) {
        this.impl = impl;
    }

    final AtomicBoolean inited = new AtomicBoolean(false);

    // 播放源相关信息
    private final SparseArray<PlayerInner> mSourcePlayers = new SparseArray<>(3);
    private final AtomicReference<AbstractPlayer> mCurrentPlayer = new AtomicReference<>();


    // 模块相关信息
    private final SparseArray<ModuleInner> mModules = new SparseArray<>(4);


    private boolean canDropPlayerSource(@NonNull Class<?> clazz) {
        boolean canDrop = false;

        @SourceType int source = SourceType.UNKNOWN;

        // 1. 轮播类的子类
        if (AbstractLoopPlayer.class.isAssignableFrom(clazz)) {
            canDrop = true;
            source = SourceType.LOOP;
        }
        // 2. 点播类子类
        else if (AbstractVodPlayer.class.isAssignableFrom(clazz)) {
            canDrop = true;
            source = SourceType.VOD;
        }
        // 3. 直播类子类
        else if (AbstractLivePlayer.class.isAssignableFrom(clazz)) {
            canDrop = true;
            source = SourceType.LIVE;
        }

        // Class 被 PlayerImplClass 注解标记过
        if (canDrop && clazz.isAnnotationPresent(PlayerImplClass.class)) {
            // Class 类被 PlayerImplClass 注解标记过
            if (clazz.isAnnotationPresent(PlayerImplClass.class)) {
                PlayerImplClass annotation = clazz.getAnnotation(PlayerImplClass.class);
                if (null != annotation) {
                    if (!annotation.canWork()) {
                        Log.w(TAG, "Player Source: " + annotation.desc() + " can not work");
                        return true;
                    }

                    PlayerInner inner = new PlayerInner(this);
                    inner.clazz = clazz;
                    inner.instance = null;
                    mSourcePlayers.put(source, inner);
                }
            }
        }

        return canDrop;
    }

    private boolean canDropModule(@NonNull Class<?> clazz) {
        // 接口 IModule 的子类
        if (IModule.class.isAssignableFrom(clazz)) {
            // Class 类被 WorkModule 注解标记过
            if (clazz.isAnnotationPresent(WorkModule.class)) {
                WorkModule annotation = clazz.getAnnotation(WorkModule.class);
                if (null != annotation) {
                    if (!annotation.canWork()) {
                        Log.w(TAG, "Module: code=" + annotation.code() + " disable by " + annotation.author());
                        return true;
                    }

                    ModuleInner inner = new ModuleInner(this);
                    inner.clazz = clazz;
                    if (annotation.create()) {
                        inner.createInstance();
                    }
                    mModules.put(annotation.code(), inner);
                }
            }

            return true;
        }

        return false;
    }

    private boolean canDropClass(Class<?> clazz) {
        if (null == clazz) {
            return true;
        }

        // skip some base Object
        if (clazz.isAnnotation()) {
            return true;
        }
        if (clazz.isPrimitive()) {
            return true;
        }
        if (clazz.isInterface()) {
            return true;
        }

        //跳过非目标抽象类
        if (AbstractPlayer.class == clazz) {
            return true;
        }
        if (AbstractLoopPlayer.class == clazz) {
            return true;
        }
        if (AbstractLivePlayer.class == clazz) {
            return true;
        }
        if (AbstractVodPlayer.class == clazz) {
            return true;
        }
        if (AbstractWorkModule.class == clazz) {
            return true;
        }

        return false;
    }


    void init(Application context) {
        synchronized (inited) {
            if (inited.get()) {
                Log.w(TAG, "All had init");
                return;
            }

            String packageResourcePath = context.getPackageResourcePath();
            Log.i(TAG, "init>> packageResourcePath: " + packageResourcePath);

            long startTime = System.currentTimeMillis();
            long classCounter = 0;

            try {
                DexFile dexFile = new DexFile(packageResourcePath);
                Enumeration<String> entries = dexFile.entries();
                while (entries.hasMoreElements()) {
                    classCounter++;

                    String className = entries.nextElement();

                    // 空的 Class
                    if (TextUtils.isEmpty(className)) {
                        continue;
                    }

                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException cnfe) {
                        Log.w(TAG, "init>> not found class named: " + className);
                        clazz = null;
                    }

                    if (canDropClass(clazz)) {
                        continue;
                    }

                    // 处理播放源 Class
                    if (canDropPlayerSource(clazz)) {
                        continue;
                    }

                    if (canDropModule(clazz)) {
                        continue;
                    }
                }
                inited.set(true);

            } catch (IOException ignore) {
                inited.set(false);
            } finally {
                long take = System.currentTimeMillis() - startTime;
                Log.i(TAG, "init>> scan " + classCounter + " classes, take " + take + " ms.");
            }


            //
            StringBuilder sb = new StringBuilder();
            sb.append("--------------------------------------------------\n");
            sb.append("MODULE: ").append(mModules.size()).append("\n");
            for (int i = 0, size = mModules.size(); i < size; ++i) {
                ModuleInner inner = mModules.valueAt(i);
                sb.append(inner).append("\n");
            }
            sb.append("--------------------------------------------------\n");
            sb.append("PLAYER: ").append(mSourcePlayers.size()).append("\n");
            for (int i = 0, size = mSourcePlayers.size(); i < size; ++i) {
                PlayerInner inner = mSourcePlayers.valueAt(i);
                sb.append(inner).append("\n");
            }
            sb.append("--------------------------------------------------\n");

            Log.i(TAG, "init scan info:\n" + sb.toString());
        }
    }

    IModule getModuleByCode(int code) {
        synchronized (mModules) {
            ModuleInner inner = mModules.get(code);
            if (null == inner) {
                return null;
            }
            return inner.instance;
        }
    }


    AbstractPlayer getCurrentPlayer() {
        return mCurrentPlayer.get();
    }

    void setCurrentPlayer(AbstractPlayer player) {
        mCurrentPlayer.set(player);
    }

    AbstractPlayer getInnerLive() {
        synchronized (mSourcePlayers) {
            PlayerInner inner = mSourcePlayers.get(SourceType.LIVE);
            if (null == inner) {
                return null;
            }
            return inner.instance;
        }
    }

    AbstractPlayer getInnerLoop() {
        synchronized (mSourcePlayers) {
            PlayerInner inner = mSourcePlayers.get(SourceType.LOOP);
            if (null == inner) {
                return null;
            }
            return inner.instance;
        }
    }

    AbstractPlayer getInnerVod() {
        synchronized (mSourcePlayers) {
            PlayerInner inner = mSourcePlayers.get(SourceType.VOD);
            if (null == inner) {
                return null;
            }
            return inner.instance;
        }
    }


    //==============================================================================================
    // class IInner
    //
    private static abstract class IInner<T> {
        protected Manager manager;

        IInner(@NonNull Manager mgr) {
            manager = mgr;
        }

        Class<?> clazz;
        T instance;

        abstract void createInstance();

        @NonNull
        @Override
        public String toString() {
            return "class: " + (null != clazz ? clazz : "") + ", 0x" + (null != instance ? instance.hashCode() : "00");
        }
    }

    //==============================================================================================
    // class PlayerInner
    //
    private static class PlayerInner extends IInner<AbstractPlayer> {
        PlayerInner(@NonNull Manager mgr) {
            super(mgr);
        }

        //clazz: 播放源 Class: 例如 点播源  直播源  轮播源

        //instance: 播放源示例: 例如 点播源  直播源  轮播源

        @Override
        void createInstance() {
            if (null != instance) {
                instance.quit();
                instance = null;
            }

            if (null == clazz) {
                Log.e(TAG, "PlayerInner>> No any Class object");
                return;
            }

            try {
                Constructor<?> constructor = clazz.getConstructor(Application.class, Handler.class, IListenerActionRun.class);
                instance = (AbstractPlayer) constructor.newInstance(manager.impl.mApp, manager.impl.H, manager.impl.mAction);
            } catch (NoSuchMethodException e1) {
                Log.e(TAG, "PlayerInner>> NoSuchMethodException", e1);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "PlayerInner>> IllegalAccessException", e2);
            } catch (InstantiationException e3) {
                Log.e(TAG, "PlayerInner>> InstantiationException", e3);
            } catch (InvocationTargetException e4) {
                Log.e(TAG, "PlayerInner>> InvocationTargetException", e4);
            }
        }
    }

    //==============================================================================================
    // class ModuleInner
    //
    private static class ModuleInner extends IInner<IModule> {
        ModuleInner(@NonNull Manager mgr) {
            super(mgr);
        }

        //clazz: 执行模块 Class

        //instance: 对应模块示例

        @Override
        void createInstance() {
            if (null != instance) {
                instance.quit();
                instance = null;
            }

            if (null == clazz) {
                Log.e(TAG, "ModuleInner>> No any Class object");
                return;
            }

            try {
                Constructor<?> constructor = clazz.getConstructor(Application.class);
                instance = (AbstractWorkModule) constructor.newInstance(manager.impl.mApp);
            } catch (NoSuchMethodException e1) {
                Log.e(TAG, "ModuleInner>> NoSuchMethodException", e1);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "ModuleInner>> IllegalAccessException", e2);
            } catch (InstantiationException e3) {
                Log.e(TAG, "ModuleInner>> InstantiationException", e3);
            } catch (InvocationTargetException e4) {
                Log.e(TAG, "ModuleInner>> InvocationTargetException", e4);
            }
        }
    }


}
