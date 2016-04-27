package com.wangjie.rxandroideventssample.mvp.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.rx.Result;
import com.wangjie.rxandroideventssample.mvp.events.ActionEvent;
import com.wangjie.rxandroideventssample.mvp.events.NetWorkEvent;
import com.wangjie.rxandroideventssample.global.GsonManager;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.mvp.rxbus.RxBus;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xuhon on 2016/4/22.
 */
public class VolleyApi {
    private String url;
    private RxVolley.Builder rxVolleyBuilder=new RxVolley.Builder();
    private Gson gson = GsonManager.getInstance().getGson();
    private Type type;
    private Subscription subscription;
    private Action1<Object> action1;

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

    public void get(MyHttpParams params, int page) {
        int offset = page * 8;
        params.put("pagesize", "8");
        params.put("offset", offset + "");
        this.get(url, params);
    }

    public void get(String url) {
        this.url = url;
        this.get(url, null);
    }

    public void get(MyHttpParams params) {
        this. get(this.url, params);
    }

    public void get(String url, MyHttpParams params) {
        rxVolleyBuilder.url(url).httpMethod(RxVolley.Method.GET)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .callback(new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        ResponseEntity responseEntity = new ResponseEntity();
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            responseEntity.fromJson(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        NetWorkEvent netWorkEvent = new NetWorkEvent();
                        netWorkEvent.setErrorNo(errorNo);
                        netWorkEvent.setStrMsg(strMsg);
                        RxBus.get().post(netWorkEvent);
                    }
                }).doTask();
//        return rxVolleyBuilder.url(url)
//                .httpMethod(RxVolley.Method.GET)
//                .cacheTime(1)
//                .params(params)
//                .contentType(RxVolley.ContentType.FORM)
//                .shouldCache(true)
//                .callback(callback)//网络错误回调
//                .getResult()
//                .filter(this::isSuccess)
//                .map(this::returnObject)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public void post(MyHttpParams params) {
        this.post(this.url, params);
    }

    public void post(String url,MyHttpParams params) {
        rxVolleyBuilder.url(url).httpMethod(RxVolley.Method.POST)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .callback(new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        ResponseEntity responseEntity = new ResponseEntity();
                        try {
                            JSONObject jsonObject = new JSONObject(t);
                            responseEntity.fromJson(jsonObject);
                            dealNetWork(responseEntity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        NetWorkEvent netWorkEvent = new NetWorkEvent();
                        netWorkEvent.setErrorNo(errorNo);
                        netWorkEvent.setStrMsg(strMsg);
                        RxBus.get().post(netWorkEvent);
                    }
                }).doTask();

//        return rxVolleyBuilder.url(url)
//                .httpMethod(RxVolley.Method.POST)
//                .params(params)
//                .contentType(RxVolley.ContentType.FORM)
//                .shouldCache(false)
//                .callback(callback)//网络错误回调
//                .getResult()
//                .filter(this::isSuccess)
//                .map(this::returnObject)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
    }


    public void dealNetWork(ResponseEntity responseEntity){
        int error = responseEntity.getError();
        int messageType = responseEntity.getMsg_type();
        if(messageType == -100){
            error=-100;
        }
        ResponseEntity.ERROR code = ResponseEntity.ERROR.integerToEnum(error);
        switch (code){
            case FAILED:
                RxBus.get().post(ActionEvent.ERROR,responseEntity.getMsg());
                break;
            case SUCCESS:
                subscription = Observable
                        .just(gson.fromJson(responseEntity.getData().toString(),type))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action1,throwable -> {
                            Log.i("api",url);},this::onComplete);
                break;
            case NOT_LOGIN:
                RxBus.get().post(ActionEvent.NO_LOGIN,responseEntity.getMsg());
                break;
        }
    }
    public void onComplete(){
        if(!subscription.isUnsubscribed()&& null !=subscription){
            subscription.unsubscribe();
        }
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

    public Action1<Object> getAction1() {
        return action1;
    }

    public void setAction1(Action1<Object> action1) {
        this.action1 = action1;
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

//        public Builder setPresenter(BasePresenter basePresenter){
//            this.volleyApi.setPresenter(basePresenter);
//            return this;
//        }

        public Builder setAction(Action1<Object> action1){
            this.volleyApi.setAction1(action1);
            return this;
        }

        //返回VolleyApi对象
        public VolleyApi getVolley(){
            return this.volleyApi;
        }
    }
}