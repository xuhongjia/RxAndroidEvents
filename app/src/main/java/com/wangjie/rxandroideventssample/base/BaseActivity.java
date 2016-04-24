package com.wangjie.rxandroideventssample.base;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.androidinject.annotation.present.AIAppCompatActivity;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.events.ActionEvent;
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
public class BaseActivity extends ParentActivity implements RxBusSample,BaseViewer{
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != rxBusAnnotationManager) {
            rxBusAnnotationManager.stopClear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != rxBusAnnotationManager) {
            rxBusAnnotationManager.clear();
        }
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onPostAccept(Object tag, Object event) {

    }

    @Override
    public void error(String error) {

    }

    @Override
    public void noLogin() {

    }
}
