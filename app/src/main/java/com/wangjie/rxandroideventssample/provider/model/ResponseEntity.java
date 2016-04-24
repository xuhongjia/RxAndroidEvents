package com.wangjie.rxandroideventssample.provider.model;

import com.kymjs.rxvolley.rx.Result;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhanghongbo on 16-1-13.
 */
public class ResponseEntity {

    public enum ERROR
    {
        SUCCESS(0),
        FAILED(1),
        NOT_LOGIN(-100);
        public final int fId;
        ERROR(int value) {
            this.fId = value;
        }

        public static ERROR integerToEnum(int integer) {
            switch (integer)
            {
                case 0:
                    return SUCCESS;
                case 1:
                    return FAILED;
                case -100:
                    return NOT_LOGIN;
            }

            return SUCCESS;
        }
    }
    private Object data;
    private int msg_type;
    private String msg;
    private int error;
    private Integer notify;
    private Integer index_notify;
    private Integer order_notyfy;
    private String url;
    public void fromJson(JSONObject jsonObject)
    {
        this.error = jsonObject.optInt("error");
        this.msg_type = jsonObject.optInt("msg_type");
        this.msg = jsonObject.optString("msg");
        this.data = jsonObject.opt("data");
        this.notify = jsonObject.optInt("notify");
        this.index_notify = jsonObject.optInt("index_notify");
        this.order_notyfy = jsonObject.optInt("order_notify");
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setError(int error) {
        this.error = error;
    }


    public Object getData() {
        if(data instanceof String && data.equals(""))
        {
            return new JSONArray();
        }
        return data;
    }

    public Object getData(Result result){
        if(data instanceof String && data.equals(""))
        {
            return new JSONArray();
        }
        return data;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public String getMsg() {
        return msg;
    }

    public int getError() {
        return error;
    }

    public Integer getNotify() {
        return notify;
    }

    public void setNotify(Integer notify) {
        this.notify = notify;
    }

    public Integer getIndex_notify() {
        return index_notify;
    }

    public void setIndex_notify(Integer index_notify) {
        this.index_notify = index_notify;
    }

    public Integer getOrder_notyfy() {
        return order_notyfy;
    }

    public void setOrder_notyfy(Integer order_notyfy) {
        this.order_notyfy = order_notyfy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
