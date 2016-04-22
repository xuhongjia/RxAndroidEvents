package com.wangjie.rxandroideventssample.api;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.rx.Result;


import rx.Observable;

/**
 * Created by xuhon on 2016/4/22.
 */
public class VolleyApi {
    private String url;
    private RxVolley.Builder rxVolleyBuilder=new RxVolley.Builder();
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
        HttpParams params = new HttpParams();
        params.put("pagesize", "8");
        params.put("offset", offset + "");
        get(url, params);
    }

    public Observable<Result> get(HttpParams params, int page) {
        int offset = page * 8;
        params.put("pagesize", "8");
        params.put("offset", offset + "");
        return get(url, params);
    }

    public Observable<Result> get(String url) {
        this.url = url;
        return get(url, null);
    }

    public Observable<Result> get(HttpParams params) {
        return get(this.url, params);
    }

    public Observable<Result> get(String url, HttpParams params) {
         return rxVolleyBuilder.url(url)
                 .httpMethod(RxVolley.Method.GET)
                 .cacheTime(2)
                 .params(params)
                 .contentType(RxVolley.ContentType.FORM)
                 .shouldCache(true)
                 .getResult();
    }

    public Observable<Result> post(HttpParams params) {
         return this.post(this.url, params);
    }

    public Observable<Result> post(String url,HttpParams params) {
        return rxVolleyBuilder.url(url)
                .httpMethod(RxVolley.Method.GET)
                .cacheTime(2)
                .params(params)
                .contentType(RxVolley.ContentType.FORM)
                .shouldCache(false)
                .getResult();
    }
}