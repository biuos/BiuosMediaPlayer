package com.os.biu.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.os.biu.core.annitation.PlayerImplClass;
import com.os.biu.core.play.live.AbstractLivePlayer;
import com.os.biu.core.play.loop.AbstractLoopPlayer;
import com.os.biu.core.play.vod.AbstractVodPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            ClassLoader classLoader = getClassLoader();
            findAllPlayers4(classLoader);
        }).start();

    }


    private void findAllPlayers4(ClassLoader loader) {
        List<Class<?>> classes = new ArrayList<>();

        PathClassLoader classLoader = (PathClassLoader) loader;


        try {
            String resPath = getPackageResourcePath();
            Log.d(TAG, "PackageResourcePath: " + resPath);

            long classCouter = 0;
            long startTime = System.currentTimeMillis();

            DexFile dex = new DexFile(resPath);
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                classCouter++;
                String name = entries.nextElement();
                if (TextUtils.isEmpty(name)) {
                    continue;
                }

                if (name.contains("$")) {
                    // Log.d(TAG, ">>> class: " + name);
                    continue;
                }

                if (name.startsWith("com.os.biu.player.impl")) {
                    //Log.d(TAG, "class: " + name);
                }

                try {
                    Class<?> clazz = Class.forName(name, true, loader);
                    // skip class of not IS
                    if (clazz.isAnnotation()) {
                        continue;
                    }
                    if (clazz.isInterface()) {
                        continue;
                    }
                    if (clazz.isPrimitive()) {
                        continue;
                    }

                    if (AbstractLoopPlayer.class == clazz) {
                        Log.d(TAG, "class AbstractLoopPlayer self");
                        continue;
                    }
                    if (AbstractLoopPlayer.class.isAssignableFrom(clazz)) {
                        Log.d(TAG, "AbstractLoopPlayer subclass: " + name);

                        if (!clazz.isAnnotationPresent(PlayerImplClass.class)) {
                            Log.w(TAG, name + " NOT annotation by PlayerImplClass");
                        }

                        continue;
                    }

                    if (AbstractLivePlayer.class == clazz) {
                        Log.d(TAG, "class AbstractLoopPlayer self");
                        continue;
                    }
                    if (AbstractLivePlayer.class.isAssignableFrom(clazz)) {
                        Log.d(TAG, "AbstractLivePlayer subclass: " + name);

                        if (!clazz.isAnnotationPresent(PlayerImplClass.class)) {
                            Log.w(TAG, name + " NOT annotation by PlayerImplClass");
                        }

                        continue;
                    }

                    if (AbstractVodPlayer.class == clazz) {
                        Log.d(TAG, "class AbstractLoopPlayer self");
                        continue;
                    }
                    if (AbstractVodPlayer.class.isAssignableFrom(clazz)) {
                        Log.d(TAG, "AbstractVodPlayer subclass: " + name);
                        if (!clazz.isAnnotationPresent(PlayerImplClass.class)) {
                            Log.w(TAG, name + " NOT annotation by PlayerImplClass");
                        }


                        continue;
                    }


                } catch (ClassNotFoundException | NoClassDefFoundError e1) {
                    Log.e(TAG, "Class Found failed: " + name);
                }
            }

            long endTime = System.currentTimeMillis();
            Log.e(TAG, "can class file number " + classCouter + ", take time " + (endTime - startTime) + "ms");


        } catch (IOException ioe) {
        }
    }


    private void findAllPlayers3(ClassLoader loader) {
        Class<?> clazz = PlayerImplClass.class;
        String classPackage = clazz.getPackage().getName();
        Log.d(TAG, "pkg: " + classPackage);
        List<Class<?>> clsList = ClassUtil.getAllClassByPackageName(loader, clazz.getPackage());
        if (null == clsList || clsList.isEmpty()) {
            Log.d(TAG, "empty class");
        } else {
            for (Class cls : clsList) {
                Log.d(TAG, "class: " + cls);
            }
        }
    }


//    private void findAllPlayers2() {
//        Log.d(TAG, "findAllPlayers2");
//
//        Reflections reflections = new Reflections("java.");
//        Set<Class<? extends InputStream>> allTypes = reflections.getSubTypesOf(InputStream.class);
//        for (Class type : allTypes) {
//            //System.out.println(type.getName());
//            Log.d(TAG, type.getName());
//        }
//    }

//    private void findAllPlayers() {
//        Log.d(TAG, "findAllPlayers");
//
//        Class<?> clazz = PlayerImplClass.class;
//        String classPackage = clazz.getPackage().getName();
//        Log.d(TAG, "pkg: " + classPackage);
//
//        Reflections reflections = new Reflections(new ConfigurationBuilder()
//                .forPackages(classPackage)
//                .addScanners(new SubTypesScanner())
//                .addScanners(new TypeAnnotationsScanner())
//        );
//        //Set<Class<?>> playerClass = reflections.getTypesAnnotatedWith(PlayerImplClass.class);
//        Set<Class<? extends AbstractPlayer>> playerClass = reflections.getSubTypesOf(AbstractPlayer.class);
//        Log.d(TAG, "class size = " + (null != playerClass ? playerClass.size() : 0));
//    }
}
