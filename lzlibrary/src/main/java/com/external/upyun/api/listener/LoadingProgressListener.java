package com.external.upyun.api.listener;

public interface LoadingProgressListener {
	void onProgress(long bytesWritten, long totalSize);
}
