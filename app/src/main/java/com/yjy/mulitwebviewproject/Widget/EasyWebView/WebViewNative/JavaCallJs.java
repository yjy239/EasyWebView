package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative;

import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.JSBaseCallBack;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.JavaToJsCallBack;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;


/**
 * Created by software1 on 2017/9/30.
 */

public class JavaCallJs {

    private static IWebView mWebView;

    private static BridgeProxy proxy;

    private static String TAG ="JavaCallJs";

    public JavaCallJs(IWebView mWebView){
        this.mWebView = mWebView;
    }

    public JavaCallJs(BridgeProxy proxy){
        JavaCallJs.proxy = proxy;
    }

    /**
     * 加载js代码**/
    public static void callBackScanResult(final JSBaseCallBack baseCallBack){
        //调用post方法，确定加载页面完成后加载js方法
        if(mWebView != null){
            mWebView.post(new JavaToJsCallBack() {
                @Override
                public void callback() {
                    baseCallBack.baseCallBack();
                }
            });
        }
    }


}
