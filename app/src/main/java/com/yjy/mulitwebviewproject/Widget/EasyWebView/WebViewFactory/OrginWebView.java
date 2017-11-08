package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewBehavorListener;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewLifeRecycle;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewSSL;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.JavaToJsCallBack;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeUtil;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.Message;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by software1 on 2017/11/7.
 */

public class OrginWebView implements IWebView<WebView> {

    private Context mContext;
    private LayoutInflater inflater;
    private View parent;
    private WebView mWebView;
    private IWebViewBehavorListener<JsResult,JsPromptResult> behavorListener;
    private IWebViewLifeRecycle<XWalkWebResourceRequest> lifeListener;
    private IWebViewSSL<android.webkit.ClientCertRequest,SslErrorHandler> mSslListener;
    private BaseSettingAdapter adapter;
    private static String TAG = "CWEB";
    private BridgeProxy proxy;
    private IWebView iweb;
    private View errorview;
    private FrameLayout rootView;
    private ValueCallback<Uri> mUploadMessage;
    private int RESULT_CODE = 0;

    public OrginWebView(Context context,int resid){
        mContext = context;
        iweb = this;
        parent = LayoutInflater.from(mContext).inflate(resid,null,false);
        if(parent instanceof ViewGroup){
            mWebView = new WebView(mContext);
            XWalkView.LayoutParams params =  new XWalkView.LayoutParams(XWalkView.LayoutParams.MATCH_PARENT, XWalkView.LayoutParams.MATCH_PARENT);
            ((ViewGroup) parent).addView(mWebView,params);
        }else {
            throw new IllegalArgumentException("请添加父布局");
        }
        initListener();
        rootView = (FrameLayout)parent.getRootView();

    }

    public OrginWebView(View view){
        iweb = this;
        mContext = view.getContext();
        rootView = (FrameLayout) view.getRootView();
        if(view instanceof WebView){
            mWebView = (WebView) view;
        }else {
            throw new IllegalArgumentException("请添加XWalkView");
        }
        initListener();

    }

    private void initListener() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(lifeListener != null){
                    lifeListener.shouldOverrideUrlLoading(view,url);
                }

                if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                    proxy.handlerReturnData(url);
                    return true;
                } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                    proxy.flushMessageQueue();
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(lifeListener!=null){
                    lifeListener.onPageStarted(view,url,favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(lifeListener!=null){
                    lifeListener.onPageFinished(view,url,null);
                }
                if(proxy != null){
                    if (proxy.toLoadJs != null) {
                        BridgeUtil.webViewLoadLocalJs(mContext,iweb, proxy.toLoadJs);
                    }

                    //
                    if (proxy.getStartupMessage() != null) {
                        for (Message m : proxy.getStartupMessage()) {
                            proxy.dispatchMessage(m);
                        }
                        proxy.setStartupMessage(null);
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if(lifeListener != null){
                    lifeListener.onReceivedError(view,errorCode,description,failingUrl);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                if(mSslListener != null){
                    mSslListener.onReceivedSslError(view,handler,error);
                }
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, android.webkit.ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                if(mSslListener  != null){
                    mSslListener.onReceivedClientCertRequest(view,request);
                }
            }
        });


        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if(behavorListener != null){

                }
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if(behavorListener != null){
                    behavorListener.onJsAlert(view,url,message,result);
                }
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                if(behavorListener != null){
                    behavorListener.onJsConfirm(view,url,message,result);
                }
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if(behavorListener != null){
                    behavorListener.onJsPrompt(view,url,message,defaultValue,result);
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }


        });

    }


    @Override
    public void init() {

    }

    @Override
    public void setOnBehavorListener(IWebViewBehavorListener listener) {
        this.behavorListener = listener;
    }

    @Override
    public void setOnLifeListner(IWebViewLifeRecycle lifeListner) {
        this.lifeListener = lifeListner;
    }

    @Override
    public void setOnSslLisenter(IWebViewSSL sslLisenter) {
        this.mSslListener = sslLisenter;
    }

    @Override
    public void onWebCreate() {

    }

    @Override
    public void onWebPause() {
        mWebView.onPause();
    }

    @Override
    public void onWebResume() {
        mWebView.onResume();
    }

    @Override
    public void onWebnActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onWebDestory() {

    }

    @Override
    public void setAdapter(BaseSettingAdapter adapter) {
        this.adapter = adapter;
        if(adapter != null&&mWebView != null){
            adapter.getSetting(mWebView);
        }
    }

    @Override
    public void addJsInterface(Object o, String s) {

    }

    @Override
    public void post(JavaToJsCallBack callBack) {

    }

    @Override
    public void load(String url, String content, Map<String, String> map) {
        mWebView.loadUrl(url,null);
    }

    @Override
    public void evaluateJavascript(String sctript, ValueCallback<String> back) {
        try {
            mWebView.evaluateJavascript(sctript,back);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setJsBridge(BridgeProxy proxy) {
        this.proxy = proxy;
    }
}
