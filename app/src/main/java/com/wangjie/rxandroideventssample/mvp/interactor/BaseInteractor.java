package com.wangjie.rxandroideventssample.mvp.interactor;

import com.wangjie.androidbucket.mvp.ABInteractor;
import com.wangjie.rxandroideventssample.mvp.api.VolleyApi;
import com.wangjie.rxandroideventssample.mvp.api.VolleyBuilder;

/**
 * Created by xuhon on 2016/4/27.
 */
public class BaseInteractor implements ABInteractor {
    protected VolleyBuilder builder = VolleyBuilder.getInstance();
    public BaseInteractor(){
    }
}
