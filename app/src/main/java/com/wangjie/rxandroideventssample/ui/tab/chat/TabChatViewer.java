package com.wangjie.rxandroideventssample.ui.tab.chat;

import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.rxandroideventssample.base.BaseViewer;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;

import java.util.List;

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
