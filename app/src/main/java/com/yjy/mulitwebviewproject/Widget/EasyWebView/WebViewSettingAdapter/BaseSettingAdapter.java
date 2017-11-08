package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter;

/**
 * Created by yjy on 2017/9/29.
 */

public abstract class BaseSettingAdapter<T> {

    private T mWebView;

    /***
     * 初始化设置接口*/
    public BaseSettingAdapter(){

    }


    /**
     * 用来获取setting的适配器*/
    public <T>T getView(){
        return (T)mWebView;
    }

    /***
     * 用来适配各种不同的webview适配*/
    public abstract void getSetting(T mWebView);
}
