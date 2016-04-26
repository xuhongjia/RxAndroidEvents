package com.wangjie.rxandroideventssample.mvp.viewer;

import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public interface TabChatViewer extends BaseViewer{
    //发送请求
    void getValidate(String phone);


    //接受请求
    void validateReturn(PhoneValidate phoneValidate);

}
