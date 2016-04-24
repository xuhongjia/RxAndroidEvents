package com.wangjie.rxandroideventssample.base;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.androidinject.annotation.present.AIAppCompatActivity;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.events.NetWorkEvent;
import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.global.AppManager;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.rxbus.RxBus;
import com.wangjie.rxandroideventssample.rxbus.RxBusAnnotationManager;
import com.wangjie.rxandroideventssample.rxbus.RxBusSample;

import java.lang.reflect.Method;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class BaseActivity extends AIAppCompatActivity implements BaseViewer{
    private RxBusAnnotationManager rxBusAnnotationManager;
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected boolean isStop = false;
    @Override
    public void parserMethodAnnotations(Method method) throws Exception {
        if (method.isAnnotationPresent(Accept.class)) {
            if (null == rxBusAnnotationManager) {
                rxBusAnnotationManager = new RxBusAnnotationManager(this);
            }
            rxBusAnnotationManager.parserObservableEventAnnotations(method);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        if (rxBusAnnotationManager!=null){
            try {
                Method method = this.getClass().getMethod("netWorkError",Object.class,NetWorkEvent.class);
                rxBusAnnotationManager.parserObservableEventAnnotations(method);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (null != rxBusAnnotationManager) {
//            rxBusAnnotationManager.stopClear();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != rxBusAnnotationManager) {
            rxBusAnnotationManager.clear();
        }
        AppManager.getAppManager().finishActivity(this);
    }

    //present中调用主线程的error
    @Override
    public void error(String error) {

    }

    //present中调用主线程的noLogin
    @Override
    public void noLogin(String msg) {

    }

    //通过RxBus返回的信息
    @Accept
    public void netWorkError(Object tag, NetWorkEvent netWorkEvent) {
        showToastMessage("网络错误，错误信息："+netWorkEvent.getStrMsg());
    }


}
