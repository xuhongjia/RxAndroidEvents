package com.wangjie.rxandroideventssample.ui.tab.chat;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.androidinject.annotation.annotations.mvp.AIPresenter;
import com.wangjie.rxandroideventssample.R;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.global.GsonManager;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.ui.adpater.FeedAdapter;
import com.wangjie.rxandroideventssample.ui.tab.TabContainer;
import com.wangjie.rxandroideventssample.ui.tab.feed.TabFeedPresenter;
import com.wangjie.rxandroideventssample.ui.tab.feed.TabFeedViewer;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
@AILayout(R.layout.tab_chat)
public class TabChatContainer extends TabContainer implements TabChatViewer{
    private static final String TAG = TabChatContainer.class.getSimpleName();

    @AIView(R.id.phone)
    private EditText phone;


    @AIPresenter
    private TabChatPresenter presenter;

    public TabChatContainer(Context context) {
        super(context);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @AIClick({R.id.getValidate})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getValidate:
                presenter.getValidate(phone.getText().toString().trim());
                break;
        }
    }
    @Override
    public void getValidate(String phone) {
//        presenter.getValidate(phone);
    }

    @Override
    public void validateReturn(PhoneValidate phoneValidate) {
        Toast.makeText(getContext(),phoneValidate.getCode()+"",Toast.LENGTH_SHORT).show();
    }

//    //网络请求返回的callback
//    @Accept
//    public void onPostAccept(Object tag , Object event){
//        switch (tag.toString()){
//            case APIInterface.SEND_VALIDATE_CODE_API:
//                validateReturn(gson.fromJson(event.toString(), PhoneValidate.class));
//                break;
//        }
//    }
}
