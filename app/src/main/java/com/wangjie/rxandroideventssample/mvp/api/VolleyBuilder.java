package com.wangjie.rxandroideventssample.mvp.api;

import java.lang.reflect.Type;

import rx.functions.Action1;

/**
 * Created by xuhon on 2016/4/27.
 */
public class VolleyBuilder {
    private static VolleyBuilder _VolleyBuilder;
    private VolleyApi volleyApi;
    private VolleyBuilder(){
        volleyApi=new VolleyApi();
    }

    public static VolleyBuilder getInstance() {
        if(_VolleyBuilder==null){
            _VolleyBuilder=new VolleyBuilder();
        }
        return _VolleyBuilder;
    }

    public VolleyBuilder setUrl(String url){
        this.volleyApi.setUrl(url);
        return this;
    }

    public VolleyBuilder setType(Type type){
        this.volleyApi.setType(type);
        return this;
    }

//        public Builder setPresenter(BasePresenter basePresenter){
//            this.volleyApi.setPresenter(basePresenter);
//            return this;
//        }

    public VolleyBuilder setAction(Action1<Object> action1){
        this.volleyApi.setAction1(action1);
        return this;
    }

    //返回VolleyApi对象
    public VolleyApi getVolley(){
        return this.volleyApi;
    }
}
