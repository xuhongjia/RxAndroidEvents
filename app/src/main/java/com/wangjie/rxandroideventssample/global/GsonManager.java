package com.wangjie.rxandroideventssample.global;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/22.
 */
public class GsonManager {
    private static GsonManager _GsonManager;
    private Gson gson;
    private GsonManager(){
        gson=new Gson();
    }
    public static GsonManager getInstance(){
        if(_GsonManager==null){
            _GsonManager=new GsonManager();
        }
        return _GsonManager;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
