package com.app.aitalkdemo;

import android.support.annotation.VisibleForTesting;

/**
 * Created by 知らないのセカイ on 2017/5/2.
 */

public class Msg {
    public final static int TYPE_RECEIVE=1;
    public final static int TYPE_SEND=2;
    private final static String KEY="135aec50af6e4a5684e59a19ab976ed3";

    private int code;
    private String text;
    private String url;
    private int type;

    private String info;

    public Msg(int code, String text, String url, int type, String info) {
        this.code = code;
        this.text = text;
        this.url = url;
        this.type = type;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
