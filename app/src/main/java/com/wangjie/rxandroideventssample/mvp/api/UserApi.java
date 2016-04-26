package com.wangjie.rxandroideventssample.mvp.api;

import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;

import rx.functions.Action1;

/**
 * Created by xuhon on 2016/4/26.
 */
public class UserApi extends BaseApi{
    public static void sendValidate(String phone, Action1<Object> action1){
        MyHttpParams params = new MyHttpParams("mobile",phone);
        builder.setUrl(APIInterface.SEND_FORGET_VALIDATE_CODE_API)
                .setType(PhoneValidate.class)
                .setSubscriber(action1)
                .getVolley()
                .post(params);
    }
}
