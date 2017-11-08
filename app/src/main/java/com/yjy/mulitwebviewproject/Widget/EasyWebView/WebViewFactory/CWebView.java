package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewFactory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;


import com.yjy.mulitwebviewproject.R;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewBehavorListener;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewLifeRecycle;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewSSL;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.JavaToJsCallBack;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeUtil;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.Message;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import org.xwalk.core.ClientCertRequest;
import org.xwalk.core.XWalkHttpAuthHandler;
import org.xwalk.core.XWalkJavascriptResult;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * Created by software1 on 2017/9/27.
 */

public class CWebView implements IWebView<XWalkView> {

    private Context mContext;
    private LayoutInflater inflater;
    private View parent;
    private XWalkView mXWalkView;
    private IWebViewBehavorListener<XWalkJavascriptResult,XWalkJavascriptResult> behavorListener;
    private IWebViewLifeRecycle<XWalkWebResourceRequest> lifeListener;
    private IWebViewSSL<ClientCertRequest,ValueCallback<Boolean>> mSslListener;
    private BaseSettingAdapter adapter;
    private static String TAG = "CWEB";
    private BridgeProxy proxy;
    private IWebView iweb;
    private View errorview;
    private FrameLayout rootView;

    public CWebView(Context context,int resid){
        mContext = context;
        iweb = this;
        parent = LayoutInflater.from(mContext).inflate(resid,null,false);
        if(parent instanceof ViewGroup){
            mXWalkView = new XWalkView(mContext);
            XWalkView.LayoutParams params =  new XWalkView.LayoutParams(XWalkView.LayoutParams.MATCH_PARENT, XWalkView.LayoutParams.MATCH_PARENT);
            ((ViewGroup) parent).addView(mXWalkView,params);
        }else {
            throw new IllegalArgumentException("请添加父布局");
        }
        initListener();
        rootView = (FrameLayout)parent.getRootView();

    }

    public CWebView(View view){
        iweb = this;
        mContext = view.getContext();
        rootView = (FrameLayout) view.getRootView();
        if(view instanceof XWalkView){
            mXWalkView = (XWalkView)view;
        }else {
            throw new IllegalArgumentException("请添加XWalkView");
        }
        initListener();

    }

    public void initListener(){
        errorview = LayoutInflater.from(mContext).inflate(R.layout.error_network_layout,null,false);

        mXWalkView.setUIClient(new XWalkUIClient(mXWalkView){
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                if(lifeListener != null){
                    lifeListener.onPageStarted(view,url,null);
                }
                Log.e(TAG,"START");
                super.onPageLoadStarted(view, url);
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                if(lifeListener != null){
                    lifeListener.onPageFinished(view,url,status);
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
                super.onPageLoadStopped(view, url, status);
            }

            @Override
            public boolean onJsAlert(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                if(behavorListener != null){
                    behavorListener.onJsAlert(view,url,message,result);
                }
                return super.onJsAlert(view, url, message, result);

            }

            @Override
            public boolean onJsConfirm(XWalkView view, String url, String message, XWalkJavascriptResult result) {
                if(behavorListener != null){
                    behavorListener.onJsConfirm(view,url,message,result);
                }
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(XWalkView view, String url, String message, String defaultValue, XWalkJavascriptResult result) {
                if(behavorListener != null){
                    behavorListener.onJsPrompt(view,url,message,defaultValue,result);
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public void onReceivedTitle(XWalkView view, String title) {
                if(behavorListener != null){
                    behavorListener.onReceivedTitle(view,title);
                }
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onUnhandledKeyEvent(XWalkView view, KeyEvent event) {
                if(behavorListener != null){
                    behavorListener.onUnhandledKeyEvent(view,event);
                }
                super.onUnhandledKeyEvent(view, event);
            }

            @Override
            public void openFileChooser(XWalkView view, ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                //之后解决
                super.openFileChooser(view, uploadFile, acceptType, capture);
                if(behavorListener != null){
                    behavorListener.openFileChooser(uploadFile,acceptType,capture);
                }
            }

        });

        mXWalkView.setResourceClient(new XWalkResourceClient(mXWalkView){
            @Override
            public void onLoadStarted(XWalkView view, String url) {
                super.onLoadStarted(view, url);
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
            }

            @Override
            public void onReceivedClientCertRequest(XWalkView view, ClientCertRequest handler) {
                if(mSslListener != null){
                    mSslListener.onReceivedClientCertRequest(view,handler);
                }
                super.onReceivedClientCertRequest(view, handler);
            }

            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);
                if(lifeListener != null){
                    lifeListener.onProgressChanged(view,progressInPercent);
                }

            }

            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                if(lifeListener != null){
                    lifeListener.onReceivedError(view,errorCode,description,failingUrl);
                }

                super.onReceivedLoadError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(XWalkView view, ValueCallback<Boolean> callback, SslError error) {
//                Log.e(TAG,error.getPrimaryError()+"");
                if(mSslListener != null){
                    mSslListener.onReceivedSslError(view,callback,error);
                }
                super.onReceivedSslError(view, callback, error);
            }

            @Override
            public void onReceivedHttpAuthRequest(XWalkView view, XWalkHttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
            }

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(lifeListener != null){
                    lifeListener.shouldOverrideUrlLoading(view,url);
                }
                if(proxy != null){
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
                return true;


            }

            @Override
            public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
                return super.shouldInterceptLoadRequest(view, request);
            }

            @Override
            public void doUpdateVisitedHistory(XWalkView view, String url, boolean isReload) {
                if(lifeListener != null){
                    lifeListener.doUpdateVisitedHistory(view,url,isReload);
                }
                super.doUpdateVisitedHistory(view, url, isReload);
            }
        });
    }

    @Override
    public void init() {

    }

    public void setAdapter(BaseSettingAdapter adapter){
        this.adapter = adapter;
        if(adapter != null&&mXWalkView != null){
            adapter.getSetting(mXWalkView);
        }
    }


    @Override
    public void addJsInterface(Object o, String s) {
        if(mXWalkView != null){
            mXWalkView.addJavascriptInterface(o,s);
        }
    }

    @Override
    public void post(final JavaToJsCallBack callBack) {
        mXWalkView.post(new Runnable() {
            @Override
            public void run() {
                callBack.callback();
            }
        });
    }

    @Override
    public void load(String url, String content, Map<String,String>map) {
        mXWalkView.load(url,content,map);
    }

    @Override
    public void evaluateJavascript(String sctript, ValueCallback<String> back) {
        mXWalkView.evaluateJavascript(sctript,back);
    }

    @Override
    public void setJsBridge(BridgeProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void setOnBehavorListener(IWebViewBehavorListener listener) {
        behavorListener = listener;
    }

    @Override
    public void setOnLifeListner(IWebViewLifeRecycle lifeListener) {
        this.lifeListener = lifeListener;
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
        if (mXWalkView != null) {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }
    }

    @Override
    public void onWebResume() {
        if (mXWalkView != null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();

        }
    }

    @Override
    public void onWebnActivityResult(int requestCode, int resultCode, Intent data) {
        if (mXWalkView != null) {

            mXWalkView.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (mXWalkView != null) {
            mXWalkView.onNewIntent(intent);

        }
    }


    @Override
    public void onWebDestory(){
        if(mXWalkView != null){
            mXWalkView.onDestroy();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
