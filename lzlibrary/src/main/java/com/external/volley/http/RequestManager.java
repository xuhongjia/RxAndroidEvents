package com.external.volley.http;

import android.content.Context;

import com.external.volley.AuthFailureError;
import com.external.volley.DefaultRetryPolicy;
import com.external.volley.Request;
import com.external.volley.Request.Method;
import com.external.volley.RequestQueue;
import com.external.volley.RetryPolicy;
import com.external.volley.toolbox.Volley;
import com.lizhi.library.utils.MyLog;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * RequestManager
 *
 * @author steven pan
 */
public class RequestManager {

    private static final String CHARSET_UTF_8 = "UTF-8";

    private static final int TIMEOUT_COUNT = 160 * 1000;

    private static final int RETRY_TIMES = 4;

    private volatile static RequestManager instance = null;

    private RequestQueue mRequestQueue = null;

    public interface RequestListener {

        void onRequest();

        void onSuccess(String response, String url, int actionId);

        void onError(String errorMsg, String url, int actionId);
    }

    private RequestManager() {

    }

    public void init(Context context) {
        this.mRequestQueue = Volley.newRequestQueue(context);
    }

    public static RequestManager getInstance() {
        if (null == instance) {
            synchronized (RequestManager.class) {
                if (null == instance) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    /**
     * default get method
     *
     * @param url
     * @param requestListener
     * @param actionId
     * @return
     */
    public LoadControler get(String url, RequestListener requestListener, int actionId) {
        return this.get(url, requestListener, true, actionId);
    }

    public LoadControler get(String url, RequestListener requestListener, boolean shouldCache, int actionId) {
        return this.request(Method.GET, url, null, null, requestListener, shouldCache, TIMEOUT_COUNT, RETRY_TIMES, actionId);
    }

    /**
     * default post method
     *
     * @param url
     * @param data            String, Map<String, String> or RequestMap(with file)
     * @param requestListener
     * @param actionId
     * @return
     */
    public LoadControler post(final String url, Object data, final RequestListener requestListener, int actionId) {
        return this.post(url, data, requestListener, false, TIMEOUT_COUNT, RETRY_TIMES, actionId);
    }

    /**
     * @param url
     * @param data            String, Map<String, String> or RequestMap(with file)
     * @param requestListener
     * @param shouldCache
     * @param timeoutCount
     * @param retryTimes
     * @param actionId
     * @return
     */
    public LoadControler post(final String url, Object data, final RequestListener requestListener, boolean shouldCache,
                              int timeoutCount, int retryTimes, int actionId) {
        return request(Method.POST, url, data, null, requestListener, shouldCache, timeoutCount, retryTimes, actionId);
    }

    /**
     * request
     *
     * @param method          mainly Method.POST and Method.GET
     * @param url             target url
     * @param data            request params
     * @param headers         request headers
     * @param requestListener request callback
     * @param shouldCache     useCache
     * @param timeoutCount    reqeust timeout count
     * @param retryTimes      reqeust retry times
     * @param actionId        request id
     * @return
     */
    public LoadControler request(int method, final String url, Object data, final Map<String, String> headers,
                                 final RequestListener requestListener, boolean shouldCache, int timeoutCount, int retryTimes, int actionId) {
        return this.sendRequest(method, url, data, headers, new LoadListener() {
            @Override
            public void onStart() {
                requestListener.onRequest();
            }

            @Override
            public void onSuccess(byte[] data, String url, int actionId) {
                String parsed = null;
                try {
                    parsed = new String(data, CHARSET_UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                requestListener.onSuccess(parsed, url, actionId);
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                requestListener.onError(errorMsg, url, actionId);
            }
        }, shouldCache, timeoutCount, retryTimes, actionId);
    }

    /**
     * @param method
     * @param url
     * @param data
     * @param headers
     * @param requestListener
     * @param shouldCache
     * @param timeoutCount
     * @param retryTimes
     * @param actionId
     * @return
     */
    public LoadControler sendRequest(int method, final String url, Object data, final Map<String, String> headers,
                                     final LoadListener requestListener, boolean shouldCache, int timeoutCount, int retryTimes, int actionId) {
        if (requestListener == null)
            throw new NullPointerException();

        final ByteArrayLoadControler loadControler = new ByteArrayLoadControler(requestListener, actionId);

        Request<?> request = null;
        if (data != null && data instanceof RequestMap) {// force POST and No  Cache
            request = new ByteArrayRequest(Method.POST, url, data, loadControler, loadControler);
            request.setShouldCache(false);
        } else {
            request = new ByteArrayRequest(method, url, data, loadControler, loadControler);
            request.setShouldCache(shouldCache);
        }

        if (headers != null && !headers.isEmpty()) {// add headers if not empty
            try {
                request.getHeaders().putAll(headers);
            } catch (AuthFailureError e) {
                e.printStackTrace();
            }
        }

        RetryPolicy retryPolicy = new DefaultRetryPolicy(timeoutCount, retryTimes, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        loadControler.bindRequest(request);


        if (this.mRequestQueue == null)
            throw new NullPointerException();
        requestListener.onStart();
        this.mRequestQueue.add(request);

        return loadControler;
    }

}
