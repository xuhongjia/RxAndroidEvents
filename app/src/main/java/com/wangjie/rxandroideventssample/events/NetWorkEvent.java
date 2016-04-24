package com.wangjie.rxandroideventssample.events;

/**
 * Created by xuhon on 2016/4/24.
 */
public class NetWorkEvent {
    private int errorNo;
    private String strMsg;

    public int getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(int errorNo) {
        this.errorNo = errorNo;
    }

    public String getStrMsg() {
        return strMsg;
    }

    public void setStrMsg(String strMsg) {
        this.strMsg = strMsg;
    }
}
