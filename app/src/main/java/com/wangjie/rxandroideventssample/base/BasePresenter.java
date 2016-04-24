package com.wangjie.rxandroideventssample.base;

import com.google.gson.Gson;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.androidbucket.mvp.ABBasePresenter;
import com.wangjie.androidbucket.mvp.ABInteractor;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.rxbus.RxBus;
import com.wangjie.rxandroideventssample.rxbus.RxBusAnnotationManager;
import com.wangjie.rxandroideventssample.rxbus.RxBusSample;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class BasePresenter<V extends BaseViewer, I extends ABInteractor> extends ABBasePresenter<V, I>  {
    private static final String TAG = BasePresenter.class.getSimpleName();

    private Set<Subscription> subscriptions = new HashSet<>();

    @Override
    public void closeAllTask() {
        super.closeAllTask();
        synchronized (this) {
            Iterator iter = this.subscriptions.iterator();
            while (iter.hasNext()) {
                Subscription subscription = (Subscription) iter.next();
                Logger.i(TAG, "closeAllTask[subscriptions]: " + subscription);
                if (null != subscription && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
                iter.remove();
            }
        }
    }

    public void goSubscription(Subscription subscription) {
        synchronized (this) {
            this.subscriptions.add(subscription);
        }
    }

    public void removeSubscription(Subscription subscription) {
        synchronized (this) {
            Logger.i(TAG, "removeSubscription: " + subscription);
            this.subscriptions.remove(subscription);
        }
    }

    public void error(String msg){
        viewer.error(msg);
    }

    public void noLogin(){
        viewer.noLogin();
    }


}
