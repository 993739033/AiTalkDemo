package com.app.aitalkdemo;

/**
 * Created by 知らないのセカイ on 2017/5/2.
 */

public interface HttpCallbackListener {
    void onFinish(String respone);

    void onError(Exception e);
}
