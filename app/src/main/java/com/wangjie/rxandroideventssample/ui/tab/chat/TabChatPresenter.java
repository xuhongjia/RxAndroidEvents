package com.wangjie.rxandroideventssample.ui.tab.chat;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABNoneInteractorImpl;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.api.RestApi;
import com.wangjie.rxandroideventssample.base.BasePresenter;
import com.wangjie.rxandroideventssample.global.APIInterface;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.provider.model.PhoneValidate;
import com.wangjie.rxandroideventssample.provider.model.ResponseEntity;
import com.wangjie.rxandroideventssample.ui.tab.feed.TabFeedViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class TabChatPresenter extends BasePresenter<TabChatViewer, ABNoneInteractorImpl> {
    private static final String TAG = TabChatPresenter.class.getSimpleName();
    private static int feedCount = 0;
    private Random random = new Random();
    private static final int ONE_HOUR = 1000 * 60 * 60;

    //发送请求
    void getValidate(String phone){
        RestApi api=new RestApi(APIInterface.SEND_VALIDATE_CODE_API);
        Map<String,String> param= new HashMap<>();
        param.put("mobile",phone);
        api.post(param);
    }

//    void loadFeeds(int size) {
//        goSubscription(
//                Observable.from(testLoadFeedsFromNet(size))
//                        .subscribeOn(Schedulers.newThread())
//                        .map(feed -> {
//                            feed.setTitle(feed.getTitle() + "_ob");
//                            return feed;
//                        })
//                        .toSortedList((feed, feed2) -> (int) (feed.getCreated() - feed2.getCreated()))
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(viewer::onLoadReeds, throwable -> Logger.w(TAG, "error: " + throwable.getMessage()))
//        );
//    }
//
//    /**
//     * 模拟从网络获取数据
//     *
//     * @param size
//     * @return
//     */
//    private List<Feed> testLoadFeedsFromNet(int size) {
//        size = size < 1 ? 1 : size;
//        List<Feed> feeds = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            Feed feed = new Feed();
//            feed.setTitle("title_" + feedCount);
//            feed.setContent("content_" + feedCount);
//            feed.setCreated(System.currentTimeMillis() - (random.nextInt(ONE_HOUR + 10) + ONE_HOUR));
//            feeds.add(feed);
//            feedCount++;
//        }
//        return feeds;
//    }


    void sendValidate(PhoneValidate phoneValidate){
        goSubscription(
                Observable.just(phoneValidate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(viewer::validateReturn, throwable -> Logger.w(TAG, "error: " + throwable.getMessage()))
        );
    }

}
