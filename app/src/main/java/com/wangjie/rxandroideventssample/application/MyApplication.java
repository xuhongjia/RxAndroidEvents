package com.wangjie.rxandroideventssample.application;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

//import com.alipay.euler.andfix.patch.PatchManager;
import com.wangjie.rxandroideventssample.annotation.accept.DefaultAcceptConfiguration;
import com.wangjie.rxandroideventssample.horry.rxbus.RxBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MyApplication extends Application {
    private Executor acceptExecutor = Executors.newCachedThreadPool();
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = "euler";

    private static final String APATCH_PATH = "/out.apatch";

    private static final String DIR = "apatch";//补丁文件夹
    /**
     * patch manager
     */
//    private PatchManager mPatchManager;
    @Override
    public void onCreate() {
        super.onCreate();
        RxBus.DEBUG = true;

        DefaultAcceptConfiguration.getInstance().registerAcceptConfiguration(new DefaultAcceptConfiguration.OnDefaultAcceptConfiguration() {
            @Override
            public Executor applyAcceptExecutor() {
                return acceptExecutor;
            }

            @Override
            public Handler applyAcceptHandler() {
                return handler;
            }
        });
        // initialize
//        mPatchManager = new PatchManager(this);
//        mPatchManager.init("1.0");
        Log.d(TAG, "inited.");

        // load patch
//        mPatchManager.loadPatch();
//        Log.d(TAG, "apatch loaded.");

        // add patch at runtime
//        try {
//            // .apatch file path
//            String patchFileString = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + APATCH_PATH;
//            mPatchManager.addPatch(patchFileString);
//            Log.d(TAG, "apatch:" + patchFileString + " added.");
//
//            //这里我加了个方法，复制加载补丁成功后，删除sdcard的补丁，避免每次进入程序都重新加载一次
//            File f = new File(this.getFilesDir(), DIR + APATCH_PATH);
//            if (f.exists()) {
//                boolean result = new File(patchFileString).delete();
//                if (!result)
//                    Log.e(TAG, patchFileString + " delete fail");
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "", e);
//        }
    }
}
