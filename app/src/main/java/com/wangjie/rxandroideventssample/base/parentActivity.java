package com.wangjie.rxandroideventssample.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.wangjie.androidbucket.present.ABAppCompatActivity;
import com.wangjie.androidinject.annotation.core.base.AnnotationManager;
import com.wangjie.androidinject.annotation.present.AIPresent;
import com.wangjie.androidinject.annotation.present.common.CallbackSample;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by xuhon on 2016/4/24.
 */
public class ParentActivity extends ABAppCompatActivity implements AIPresent, CallbackSample {
    private static final String TAG = ParentActivity.class.getSimpleName();
    public Context context;
    public Class<?> clazz;

    public ParentActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        long start = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        this.context = this;
        this.clazz = this.getClass();
//        (new AnnotationManager(this)).initAnnotations();
        Log.d(TAG, "[" + this.clazz.getSimpleName() + "]onCreate supper(parser annotations) takes: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new AnnotationManager(this)).initAnnotations();
    }

    public Context getContext() {
        return this.context;
    }

    public void setContentView_(int layoutResID) {
        this.setContentView(layoutResID);
    }

    public View findViewById_(int resId) {
        return this.findViewById(resId);
    }

    public void parserTypeAnnotations(Class clazz) throws Exception {
    }

    public void parserMethodAnnotations(Method method) throws Exception {
    }

    public void parserFieldAnnotations(Field field) throws Exception {
    }

    public void onClickCallbackSample(View view) {
    }

    public void onLongClickCallbackSample(View view) {
    }

    public void onItemClickCallbackSample(AdapterView<?> parent, View view, int position, long id) {
    }

    public void onItemLongClickCallbackSample(AdapterView<?> parent, View view, int position, long id) {
    }

    public void onCheckedChangedCallbackSample(CompoundButton buttonView, boolean isChecked) {
    }
}
