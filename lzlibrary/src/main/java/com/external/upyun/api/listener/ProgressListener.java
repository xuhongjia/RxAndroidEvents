package com.external.upyun.api.listener;

public interface ProgressListener {
    void transferred(long transferedBytes, long totalBytes);
}
