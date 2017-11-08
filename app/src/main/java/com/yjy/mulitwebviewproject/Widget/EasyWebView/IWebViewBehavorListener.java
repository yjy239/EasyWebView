package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;

/**
 * Created by software1 on 2017/9/28.
 */

public interface IWebViewBehavorListener<T,S> {
    /**
     * JS警告框
     * @param view 浏览器
     * @param url 链接
     * @param message 消息
     * @param result 结果*/
    boolean onJsAlert(View view, String url, String message, T result);

    /**
     * JS确认框
     * @param view
     * @param url
     * @param message
     * @param result*/

    boolean onJsConfirm(View view, String url, String message, T result);


    /**
     * JS拦截
     * @param view
     * @param url
     * @param message
     * @param result*/
    boolean onJsPrompt(View view, String url, String message, String defaultValue, S result);

    /***
     * @param title 获取网页标题
     * @param view 浏览器**/
    void onReceivedTitle(View view, String title);


    /**
     * 回调该方法，处理未被WebView处理的事件*/
    void onUnhandledKeyEvent(View view, KeyEvent event);

    /**
     * 回调该方法，处理未被WebView处理的文件选择事件*/
    void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture);

}
