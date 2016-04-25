package com.wangjie.rxandroideventssample.base;

import android.os.Bundle;

import com.wangjie.androidinject.annotation.present.AIAppCompatActivity;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.horry.events.ActionEvent;
import com.wangjie.rxandroideventssample.horry.events.NetWorkEvent;
import com.wangjie.rxandroideventssample.global.AppManager;
import com.wangjie.rxandroideventssample.horry.rxbus.RxBusAnnotationManager;
import com.wangjie.rxandroideventssample.horry.viewer.BaseViewer;

import java.lang.reflect.Method;

public class BaseActivity extends AIAppCompatActivity implements BaseViewer {
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
                Method method2 = this.getClass().getMethod("error",Object.class,Object.class);
                rxBusAnnotationManager.parserObservableEventAnnotations(method2);
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

    @Accept({
            @AcceptType(tag = ActionEvent.ERROR , clazz = String.class),
            @AcceptType(tag = ActionEvent.NO_LOGIN ,clazz = String.class)
    })
    public void error(Object tag, Object event){
        showToastMessage(event.toString());
    }

}
