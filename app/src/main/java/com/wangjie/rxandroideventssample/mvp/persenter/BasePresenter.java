package com.wangjie.rxandroideventssample.mvp.persenter;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABBasePresenter;
import com.wangjie.rxandroideventssample.mvp.api.VolleyApi;

import com.wangjie.rxandroideventssample.mvp.interactor.BaseInteractor;
import com.wangjie.rxandroideventssample.mvp.viewer.BaseViewer;

import rx.Subscription;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//BaseViewer 为Activity和Presenter的接口，Interactor是Interactor和网络请求的接口，这里我直接用Rxjava了，不需要这层接口
public class BasePresenter<V extends BaseViewer, I extends BaseInteractor> extends ABBasePresenter<V, I> {
    private static final String TAG = BasePresenter.class.getSimpleName();

    public BasePresenter(){}
    private Set<Subscription> subscriptions = new HashSet<>();

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