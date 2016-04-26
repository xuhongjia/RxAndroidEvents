package com.wangjie.rxandroideventssample.mvp.viewer;

import com.wangjie.rxandroideventssample.provider.model.Feed;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public interface TabFeedViewer extends BaseViewer{
    void loadFeeds(int size);
    void onLoadReeds(List<Feed> feedList);

    void deleteFeed();
}
