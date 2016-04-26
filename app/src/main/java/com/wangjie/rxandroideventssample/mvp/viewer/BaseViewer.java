package com.wangjie.rxandroideventssample.mvp.viewer;

import com.wangjie.androidbucket.mvp.ABActivityViewer;

/**
 * Created by xuhon on 2016/4/24.
 */
public interface BaseViewer extends ABActivityViewer {
    void error(String msg);
    void noLogin(String msg);
}
