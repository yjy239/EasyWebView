package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative;

import android.app.Activity;

import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;


/**
 * Created by software1 on 2017/9/30.
 */

public class JsCallJava {

    private static final String TAG = "WebViewNative";

    private Activity mContext;
    private IWebView web;
    private JavaCallJs mJavaCallJs;

    public JsCallJava(){

    }


    public JsCallJava(Activity context){
        mContext = context;
    }

    public JsCallJava(Activity context,IWebView web){
        mContext = context;
        mJavaCallJs = new JavaCallJs(web);
    }

//    @JavascriptInterface
//    public void hello(String str){
//        Log.e(TAG,str);
//    }
//
//    @JavascriptInterface
//    public void openScan(){
//        Log.e(TAG,"OPEN");
////        QrCodeActivity.launch(mContext);
//    }

}
