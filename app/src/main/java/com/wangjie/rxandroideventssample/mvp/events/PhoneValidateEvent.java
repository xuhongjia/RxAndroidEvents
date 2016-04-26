package com.wangjie.rxandroideventssample.mvp.events;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22.
 */
public class PhoneValidateEvent implements Serializable {
    private String tag = "phoneValidate";
    private String phone;
    private String code;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
