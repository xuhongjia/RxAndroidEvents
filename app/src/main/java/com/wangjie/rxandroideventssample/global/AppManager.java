package com.wangjie.rxandroideventssample.global;

import android.app.Activity;


import com.wangjie.rxandroideventssample.base.BaseActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private List<BaseActivity> mActivityList = new LinkedList<BaseActivity>();
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(BaseActivity activity) {
        mActivityList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            mActivityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (mActivityList.size() > 0) {
            BaseActivity activity = mActivityList.get(mActivityList.size() - 1);
            mActivityList.remove(mActivityList.size() - 1);

        }
    }
    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    public Activity getTopActivity(){
        return mActivityList.get(mActivityList.size()-1);
    }
}