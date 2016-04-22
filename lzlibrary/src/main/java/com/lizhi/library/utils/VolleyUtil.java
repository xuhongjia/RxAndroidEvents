package com.lizhi.library.utils;

import android.content.Context;

import com.external.volley.RequestQueue;
import com.external.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

public class VolleyUtil {

	private volatile static RequestQueue requestQueue;

	/** 返回RequestQueue单例 **/
	public static RequestQueue getQueue(Context context) {
		if (requestQueue == null) {
			synchronized (VolleyUtil.class) {
				if (requestQueue == null) {
					requestQueue = Volley.newRequestQueue(context.getApplicationContext());
					CookieManager cookieManager = new CookieManager();
					CookieHandler.setDefault(cookieManager);
				}
			}
		}
		return requestQueue;
	}
}
