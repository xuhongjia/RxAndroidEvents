package com.wangjie.rxandroideventssample.base;

import com.wangjie.androidinject.annotation.present.AIAppCompatActivity;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.global.AppManager;
import com.wangjie.rxandroideventssample.rxbus.RxBusAnnotationManager;
import com.wangjie.rxandroideventssample.rxbus.RxBusSample;

import java.lang.reflect.Method;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class BaseActivity extends AIAppCompatActivity implements RxBusSample{
    public static final String TAG = BaseActivity.class.getSimpleName();
    private RxBusAnnotationManager rxBusAnnotationManager;
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
    protected void onDestroy() {
        super.onDestroy();
        if (null != rxBusAnnotationManager) {
            rxBusAnnotationManager.clear();
        }
    }

    @Override
    @Accept(
            {
                    @AcceptType(tag = ActionEvent.NO_LOGIN, clazz = String.class),
                    @AcceptType(tag = ActionEvent.ERROR, clazz = String.class)
            }
    )
    public void onPostAccept(Object tag, Object event) {
        //可能会被调用多次
    }
}
