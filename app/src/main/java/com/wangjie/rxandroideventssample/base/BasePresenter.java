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

import rx.Subscription;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class BasePresenter<V extends ABActivityViewer, I extends ABInteractor> extends ABBasePresenter<V, I>  implements RxBusSample{
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


    @Override
    public void onPostAccept(Object tag, Object event) {
        if(event.toString().equals(ActionEvent.NO_LOGIN)){
            RxBus.get().post(ActionEvent.NO_LOGIN,ActionEvent.NO_LOGIN);
        }
        else{
            RxBus.get().post(ActionEvent.ERROR,event.toString());
        }
    }
}
