package com.wangjie.rxandroideventssample.mvp.interactor;

import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.utils.MyHttpParams;

import rx.functions.Action1;

/**
 * Created by xuhon on 2016/4/27.
 */
public class UserInteractor extends BaseInteractor {
    public  void sendValidate(String phone, Action1<Object> action1){
        MyHttpParams params = new MyHttpParams("mobile",phone);
        builder.setUrl(APIInterface.SEND_FORGET_VALIDATE_CODE_API)
                .setType(PhoneValidate.class)
                .setAction(action1)
                .getVolley()
                .post(params);
    }
}
