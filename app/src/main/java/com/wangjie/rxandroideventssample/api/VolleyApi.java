package com.wangjie.rxandroideventssample.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.rx.Result;
import com.wangjie.rxandroideventssample.base.BasePresenter;
import com.wangjie.rxandroideventssample.base.BaseViewer;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.events.NetWorkEvent;
import com.wangjie.rxandroideventssample.global.GsonManager;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.rxbus.RxBus;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xuhon on 2016/4/22.
 */
public class VolleyApi {
    private String url;
    private RxVolley.Builder rxVolleyBuilder=new RxVolley.Builder();
    private Gson gson = GsonManager.getInstance().getGson();
    private Type type;
    private BaseViewer viewer;
    private ResponseEntity responseEntity;
    private BasePresenter presenter;
    public HttpCallback callback = new HttpCallback() {
        @Override
        public void onFailure(int errorNo, String strMsg) {
            super.onFailure(errorNo, strMsg);
            NetWorkEvent netWorkEvent = new NetWorkEvent();
            netWorkEvent.setErrorNo(errorNo);
            netWorkEvent.setStrMsg(strMsg);
            RxBus.get().post(netWorkEvent);
//            RxBus.get().post(ActionEvent.NETWORK_ERROR,strMsg);
        }
    };

    public VolleyApi(){

    }

    public VolleyApi(String url , Type type) {
        this.url = url;
        this.type =  type;
    }

    /**
     * 获取列表数据
     *
     * @param page 　页码
     */
    public void get(int page) {
        int offset = page * 8;
        MyHttpParams params = new MyHttpParams("pagesize","8","offset",offset);
        get(url, params);
    }

    public Observable<Object> get(MyHttpParams params, int page) {
        int offset = page * 8;
        params.put("pagesize", "8");
        params.put("offset", offset + "");
        return get(url, params);
    }

    public Observable<Object> get(String url) {
        this.url = url;
        return get(url, null);
    }

    public Observable<Object> get(MyHttpParams params) {
        return get(this.url, params);
    }

    public Observable<Object> get(String url, MyHttpParams params) {
        return rxVolleyBuilder.url(url)
                .httpMethod(RxVolley.Method.GET)
                .cacheTime(1)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .shouldCache(true)
                .callback(callback)//网络错误回调
                .getResult()
                .filter(this::isSuccess)
                .map(this::returnObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Object> post(MyHttpParams params) {
        return this.post(this.url, params);
    }

    public Observable<Object> post(String url,MyHttpParams params) {
        return rxVolleyBuilder.url(url)
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .shouldCache(false)
                .callback(callback)//网络错误回调
                .getResult()
                .filter(this::isSuccess)
                .map(this::returnObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Boolean isSuccess(Result result){
        responseEntity = gson.fromJson(new String(result.data), ResponseEntity.class);
        ResponseEntity.ERROR code = ResponseEntity.ERROR.integerToEnum(responseEntity.getError());
        switch (code){
            case FAILED://返回错误信息
                presenter.error(responseEntity.getMsg());
                return false;
            case NOT_LOGIN://提示没有登陆
                presenter.noLogin(responseEntity.getMsg());
                return false;
        }
        return true;
    }

    public Object returnObject(Result result){
        return gson.fromJson(responseEntity.getData().toString(),type);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BasePresenter getPresenter() {
        return presenter;
    }

    public void setPresenter(BasePresenter presenter) {
        this.presenter = presenter;
    }

    public static class Builder{
        VolleyApi volleyApi;
        public Builder(){
            volleyApi=new VolleyApi();
        }

        public Builder setUrl(String url){
            this.volleyApi.setUrl(url);
            return this;
        }

        public Builder setType(Type type){
            this.volleyApi.setType(type);
            return this;
        }

        public Builder setPresenter(BasePresenter basePresenter){
            this.volleyApi.setPresenter(basePresenter);
            return this;
        }

        //返回VolleyApi对象
        public VolleyApi request(){
            return this.volleyApi;
        }
    }
}