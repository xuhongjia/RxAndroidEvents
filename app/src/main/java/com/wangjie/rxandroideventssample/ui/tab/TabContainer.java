package com.wangjie.rxandroideventssample.ui.tab;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.androidbucket.mvp.ABBasePresenter;
import com.wangjie.androidinject.annotation.core.base.AnnotationManager;
import com.wangjie.androidinject.annotation.present.AIPresent;
import com.wangjie.androidinject.annotation.present.common.CallbackSample;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.base.BaseViewer;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.global.GsonManager;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.rxbus.RxBusAnnotationManager;
import com.wangjie.rxandroideventssample.rxbus.RxBusSample;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class TabContainer extends FrameLayout implements AIPresent, CallbackSample, BaseViewer {
    private static final String TAG = TabContainer.class.getSimpleName();
    private RxBusAnnotationManager rxBusAnnotationManager;
    protected Gson gson = GsonManager.getInstance().getGson();
    private ABBasePresenter presenter;

    public TabContainer(Context context) {
        super(context);
        init();
    }

    private TabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private TabContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private TabContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    private View rootView;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "[" + this.getClass().getSimpleName() + "]onAttachedToWindow");
        long start = System.currentTimeMillis();
        new AnnotationManager(this).initAnnotations();
        Log.d(TAG, "[" + this.getClass().getSimpleName() + "]onAttachedToWindow supper(parser annotations) takes: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "[" + this.getClass().getSimpleName() + "]onDetachedFromWindow");
        closeAllTask();

        if (null != rxBusAnnotationManager) {
            rxBusAnnotationManager.clear();
        }
    }


    protected void setContentView(int resId) {
        setContentView(View.inflate(getContext(), resId, null));
    }

    protected void setContentView(@NonNull View view) {
        FrameLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (null == lp) {
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(lp);
        this.addView(view);
    }


    /**
     * ******** annotation support *********
     */

    @Override
    public void setContentView_(int i) {
        setContentView(i);
    }

    @Override
    public View findViewById_(int i) {
        return findViewById(i);
    }

    @Override
    public void parserTypeAnnotations(Class aClass) throws Exception {

    }

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
    public void parserFieldAnnotations(Field field) throws Exception {

    }

    @Override
    public void registerPresenter(ABBasePresenter abBasePresenter) {
        presenter = abBasePresenter;
    }

    @Override
    public void closeAllTask() {
        if (null != presenter) {
            presenter.closeAllTask();
        }
    }

    @Override
    public void onClickCallbackSample(View view) {

    }

    @Override
    public void onLongClickCallbackSample(View view) {

    }

    @Override
    public void onItemClickCallbackSample(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onItemLongClickCallbackSample(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onCheckedChangedCallbackSample(CompoundButton compoundButton, boolean b) {

    }

    /**
     * *************** ABActivityViewer ****************
     */
    @Override
    public void showToastMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInfoDialog(String s) {
        showInfoDialog(null, s, "OK");
    }

    @Override
    public void showInfoDialog(String s, String s1) {
        showInfoDialog(s, s1, "OK");
    }

    @Override
    public void showLoadingDialog(String s) {

    }

    @Override
    public void cancelLoadingDialog() {

    }

    @Override
    public void showInfoDialog(String s, String s1, String s2) {
        new AlertDialog.Builder(getContext())
                .setTitle(s)
                .setMessage(s1)
                .setPositiveButton(s2, null)
                .show();
    }

    public void onPostAccept(Object tag, Object event) {
        switch (tag.toString()){
            case ActionEvent.ERROR:
                showToastMessage(event.toString());
                return;
            case ActionEvent.NO_LOGIN:
                showToastMessage("没有登录");
                return;
        }
    }

    @Override
    public void error(String error) {

    }

    @Override
    public void noLogin() {

    }
}
