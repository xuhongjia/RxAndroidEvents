package com.wangjie.rxandroideventssample.api;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.rx.Result;
import com.wangjie.rxandroideventssample.global.GsonManager;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;


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
    public VolleyApi(String url) {
        this.url = url;
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

    public Observable<ResponseEntity> get(MyHttpParams params, int page) {
        int offset = page * 8;
        params.put("pagesize", "8");
        params.put("offset", offset + "");
        return get(url, params);
    }

    public Observable<ResponseEntity> get(String url) {
        this.url = url;
        return get(url, null);
    }

    public Observable<ResponseEntity> get(MyHttpParams params) {
        return get(this.url, params);
    }

    public Observable<ResponseEntity> get(String url, MyHttpParams params) {
         return rxVolleyBuilder.url(url)
                 .httpMethod(RxVolley.Method.GET)
                 .cacheTime(2)
                 .params(params)
                 .contentType(RxVolley.ContentType.FORM)
                 .shouldCache(true)
                 .getResult()
                 .map(new Func1<Result, ResponseEntity>() {
                     @Override
                     public ResponseEntity call(Result result) {
                         return gson.fromJson(new String(result.data), ResponseEntity.class);
                     }
                 })
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResponseEntity> post(MyHttpParams params) {
         return this.post(this.url, params);
    }

    public Observable<ResponseEntity> post(String url,MyHttpParams params) {
        return rxVolleyBuilder.url(url)
                .httpMethod(RxVolley.Method.POST)
                .cacheTime(2)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .shouldCache(false)
                .getResult()
                .map(new Func1<Result, ResponseEntity>() {
                    @Override
                    public ResponseEntity call(Result result) {
                        return gson.fromJson(new String(result.data),ResponseEntity.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}