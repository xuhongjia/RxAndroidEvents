package com.external.upyun.api.listener;

public interface LoadingCompleteListener {
	void result(boolean isSuccess, String response, String error);
}
