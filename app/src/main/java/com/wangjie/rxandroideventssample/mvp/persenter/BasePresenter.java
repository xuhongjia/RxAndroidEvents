package com.wangjie.rxandroideventssample.mvp.persenter;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABBasePresenter;
import com.wangjie.androidbucket.mvp.ABInteractor;
import com.wangjie.rxandroideventssample.mvp.api.VolleyApi;

import com.wangjie.rxandroideventssample.mvp.viewer.BaseViewer;

import rx.Subscription;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BasePresenter<V extends BaseViewer, I extends ABInteractor> extends ABBasePresenter<V, I> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    private Set<Subscription> subscriptions = new HashSet<>();
    protected VolleyApi.Builder builder = new VolleyApi.Builder();

    //关闭所有的监听事件
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

    //添加一个监听事件
    public void goSubscription(Subscription subscription) {
        synchronized (this) {
            this.subscriptions.add(subscription);
        }
    }

    //
    public void removeSubscription(Subscription subscription) {
        synchronized (this) {
            Logger.i(TAG, "removeSubscription: " + subscription);
            this.subscriptions.remove(subscription);
        }
    }
}