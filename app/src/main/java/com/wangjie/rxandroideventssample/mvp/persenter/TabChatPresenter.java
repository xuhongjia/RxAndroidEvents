package com.wangjie.rxandroideventssample.mvp.persenter;

import com.wangjie.androidbucket.log.Logger;

import com.wangjie.rxandroideventssample.mvp.interactor.UserInteractor;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.mvp.viewer.TabChatViewer;

import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by xuhon on 2016/4/22.
 */
public class TabChatPresenter extends BasePresenter<TabChatViewer, UserInteractor> {
    private static final String TAG = TabChatPresenter.class.getSimpleName();
    private static int feedCount = 0;
    private Random random = new Random();
    private static final int ONE_HOUR = 1000 * 60 * 60;

    //发送请求 ，一行代码实现网络请求
    public void getValidate(String phone){
        interactor.sendValidate(phone, o -> {viewer.validateReturn((PhoneValidate) o);});
//        goSubscription(
//                builder.setType(PhoneValidate.class).setUrl(APIInterface.SEND_VALIDATE_CODE_API).getVolley()
//                .post(param)
//                .subscribe(phoneValidate -> {
//                    Log.i("kymjs", "======网络请求" + ((PhoneValidate)phoneValidate).getCode());
//                    AppManager.getAppManager().getTopActivity().getClass().getName();
//                    viewer.validateReturn((PhoneValidate) phoneValidate);
//                }));
    }

    void sendValidate(PhoneValidate phoneValidate){
        goSubscription(
                Observable
                        .just(phoneValidate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(viewer::validateReturn, throwable -> Logger.w(TAG, "error: " + throwable.getMessage()))
        );
    }

}
