package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by software1 on 2017/9/28.
 */

public interface IWebViewLifeRecycle<R> {

    /**
     * 进度条管理**/
    void onProgressChanged(View view, int newProgress);

    /**
     * 页面重载*/
    void shouldOverrideUrlLoading(View view, String url);

    /**
     * 页面加载结束*/
    void onPageFinished(View view, String url, Object status);

    /**
     * 页面加载开始*/
    void onPageStarted(View view, String url, Bitmap favicon);

    /**接受错误*/
    void onReceivedError(View view, int errorCode, String description, String failingUrl);

//    void onReceivedSslError(View view, T handler, SslError error);
//    void onReceivedClientCertRequest(View view, S handler);

    /**拦截url**/
    void shouldInterceptLoadRequest(View view, R request);

//    void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

    /**
     * 更新浏览历史记录，获取更新的url地址
     */
    void doUpdateVisitedHistory(View view, String url, boolean isReload);
//
//    /**
//     * 表单提交出错*/
//    void onFormResubmission(IWebView view, Message dontResend, Message resend);


}
