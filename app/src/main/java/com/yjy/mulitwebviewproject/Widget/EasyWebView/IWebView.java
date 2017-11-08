package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.content.Intent;
import android.webkit.ValueCallback;



import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import java.util.Map;

/**
 * Created by yjy on 2017/9/27.
 */

public interface IWebView<T>{
    /***
     * webview初始化***/
    void init();

    void setOnBehavorListener(IWebViewBehavorListener listener);

    void setOnLifeListner(IWebViewLifeRecycle lifeListner);

    void setOnSslLisenter(IWebViewSSL sslLisenter);

    /**
     * webview 创建状态**/
    void onWebCreate();
    /**
     * webview停止交互**/
    void onWebPause();

    /**
     * webview交互状态*/
    void onWebResume();

    /**
     * webview acttivity 返回*/
    void onWebnActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * intnent 返回**/
    void onNewIntent(Intent intent);

    /****
     * webview销毁**/
    void onWebDestory();

    /**
     * 设置adapter**/
    void setAdapter(BaseSettingAdapter adapter);

    /**
     * 绑定native**/
    void addJsInterface(Object o, String s);

    /**webview启动js方法**/
    void post(JavaToJsCallBack callBack);

    /**
     * 加载url或者js**/
    void load(String url, String content, Map<String, String> map);

    void evaluateJavascript(String sctript, ValueCallback<String> back);

    void setJsBridge(BridgeProxy proxy);






}
