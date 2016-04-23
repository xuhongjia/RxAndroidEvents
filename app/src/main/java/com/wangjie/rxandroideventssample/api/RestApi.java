package com.wangjie.rxandroideventssample.api;

//import com.lizhi.library.utils.LZUtils;
//import com.squareup.okhttp.Request;
//import com.wangjie.rxandroideventssample.events.ActionEvent;
//import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
//import com.wangjie.rxandroideventssample.rxbus.RxBus;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanghongbo on 16-1-13.
 */
public class RestApi {
//    protected String url;
//    //    protected Dispatcher dispatcher;
//    private String extra;
//    //    private YSharePreference ySharePreference;
//    public RestApi(String url) {
//        this.url = url;
////        dispatcher = Dispatcher.get(new Bus());
////        ySharePreference = YSharePreference.getInstance();
//    }
//
//    /**
//     * @param url
//     * @param extra 　附加参数
//     */
//    public RestApi(String url, String extra) {
//        this.url = url;
////        dispatcher = Dispatcher.get(new Bus());
//        this.extra = extra;
//    }
//
//
//    public void get() {
//        get(this.url, null);
//    }
//
//    /**
//     * 获取列表数据
//     *
//     * @param page 　页码
//     */
//    public void get(int page) {
//        int offset = page * 8;
//        Map<String, String> params = new HashMap<>();
//        params.put("pagesize", "8");
//        params.put("offset", offset + "");
//        get(url, params);
//    }
//
//    public void get(Map<String,String> params,int page)
//    {
//        int offset = page * 8;
//        params.put("pagesize", "8");
//        params.put("offset", offset + "");
//        get(url, params);
//    }
//
//    public void get(String url) {
//        this.url = url;
//        get(url, null);
//    }
//
//    public void get(Map<String, String> params) {
//        get(this.url, params);
//    }
//
//    public void get(String url, Map<String, String> params) {
////        USER user = YSharePreference.getInstance().getUser();
////        if (user != null) {
////            url += "&sign=" + user.getSign();
////        }
////        dispatcher.dispatch(BaseActions.LOADING);
//        if (params != null) {
//            String paramsStr = LZUtils.mapToString(params);
//            url = url + paramsStr;
//        }
//        OkHttpUtils
//                .get()
//                .url(url)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
////                        dispatcher.dispatch(LoginActions.NETWORK_ERROR);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        ResponseEntity responseEntity = new ResponseEntity();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            responseEntity.fromJson(jsonObject);
//                            dealNetwork(responseEntity);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//    }
//
//    public void post(Map<String, String> params) {
//        this.post(this.url, params);
//    }
//
//    public void post(String url, Map<String, String> params) {
////        USER user = YSharePreference.getInstance().getUser();
////        if (user != null) {
////            url += "&sign=" + user.getSign();
////        }
//        params = trimParam(params);
////        dispatcher.dispatch(BaseActions.LOADING);
//        OkHttpUtils
//                .post()
//                .url(url)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
////                        dispatcher.dispatch(BaseActions.NETWORK_ERROR);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        ResponseEntity responseEntity = new ResponseEntity();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            responseEntity.fromJson(jsonObject);
//                            dealNetwork(responseEntity);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//    }
//
//    /**
//     * 处理网络请求
//     *
//     * @param responseEntity
//     */
//    private void dealNetwork(ResponseEntity responseEntity) {
//        responseEntity.setUrl(url);
////        RxBus.get().post(url,responseEntity);
//        int error = responseEntity.getError();
//        int messageType = responseEntity.getMsg_type();
//        if (messageType == -100) {
//            error = -100;
//        }
//        ResponseEntity.ERROR code = ResponseEntity.ERROR.integerToEnum(error);
//        switch (code) {
//            case FAILED:
//                RxBus.get().post(ActionEvent.ERROR,responseEntity.getMsg());
////                dispatcher.dispatch(BaseActions.ERROR, BaseActions.DATA, responseEntity.getMsg(), BaseActions.ERROR, error);
//                break;
//            case SUCCESS:
//                RxBus.get().post(url,responseEntity.getData());
////                if (TextUtils.isEmpty(extra))
////                    dispatcher.dispatch(BaseActions.RESPONSE + url, BaseActions.DATA, responseEntity.getData(), BaseActions.ERROR, error);
////                else
////                    dispatcher.dispatch(BaseActions.RESPONSE + url + extra, BaseActions.DATA, responseEntity.getData(),BaseActions.ERROR, error);
//                break;
//            case NOT_LOGIN:
//                RxBus.get().post(ActionEvent.NO_LOGIN, responseEntity);
////                YSharePreference.getInstance().setUser(null);
////                dispatcher.dispatch(BaseActions.NO_LOGIN);
//                break;
//        }
//
//    }
//
//    /**
//     * 删除param中的空值
//     *
//     * @param params
//     * @return
//     */
//    private Map<String, String> trimParam(Map<String, String> params) {
//        Map<String, String> temp = new HashMap<>();
//        Iterator it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            String key = (String) pair.getKey();
//            Object value = pair.getValue();
//            if (value != null) {
//                temp.put(key, value.toString());
//            }
//        }
//
//        return temp;
//    }
}
