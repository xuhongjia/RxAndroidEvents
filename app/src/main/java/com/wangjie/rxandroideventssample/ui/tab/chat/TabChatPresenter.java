package com.wangjie.rxandroideventssample.ui.tab.chat;

import android.util.Log;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABNoneInteractorImpl;
import com.wangjie.rxandroideventssample.api.VolleyApi;
import com.wangjie.rxandroideventssample.base.BasePresenter;
import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.global.AppManager;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.ui.tab.feed.TabFeedViewer;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xuhon on 2016/4/22.
 */
public class TabChatPresenter extends BasePresenter<TabChatViewer, ABNoneInteractorImpl> {
    private static final String TAG = TabChatPresenter.class.getSimpleName();
    private static int feedCount = 0;
    private Random random = new Random();
    private static final int ONE_HOUR = 1000 * 60 * 60;

    //发送请求
    void getValidate(String phone){
        VolleyApi api=new VolleyApi(APIInterface.SEND_VALIDATE_CODE_API);
        MyHttpParams param= new MyHttpParams("mobile", phone);
        goSubscription(api.post(param)
                .subscribe(new Action1<ResponseEntity>() {
                    @Override
                    public void call(ResponseEntity ResponseEntity) {
                        Log.i("kymjs", "======网络请求" + ResponseEntity.getMsg());
                        AppManager.getAppManager().getTopActivity().getClass().getName();
                    }
                }));
    }

    void sendValidate(PhoneValidate phoneValidate){
        goSubscription(
                Observable.just(phoneValidate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(viewer::validateReturn, throwable -> Logger.w(TAG, "error: " + throwable.getMessage()))
        );
    }

}
