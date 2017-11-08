package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.yjy.mulitwebviewproject.R;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewBuilder.WebViewUtils;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;

/**
 * Created by yjy on 2017/9/27.
 */

public abstract class WebViewActivity<T> extends Activity{

    private IWebView mwebView;
    private XWalkView view;
    private handlerRecycle listener;
    private JsActionListener mJsListener;
    private SslListener<android.webkit.ClientCertRequest,SslErrorHandler> mSslListener;

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.webview);

        //工厂模式创建类
        init();
        mwebView = exchange(mWebView);
        mwebView.onWebCreate();

        final BridgeProxy proxy = createBridge(mwebView);
        new WebViewUtils.Builder(mwebView)
                .setJsBridge(proxy)
                .setAdapter(new BaseSettingAdapter<WebView>() {
                    @Override
                    public void getSetting(WebView mWebView) {
                        //添加对javascript支持

//                        XWalkPreferences.setValue("enable-javascript", true);
//                        //开启调式,支持谷歌浏览器调式
//                        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
//                        //置是否允许通过file url加载的Javascript可以访问其他的源,包括其他的文件和http,https等其他的源
//                        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
//                        // VASCRIPT_CAN_OPEN_WINDOW
//                        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
//                        // enable multiple windows.
//                        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);

                        //设置滑动
                        mWebView.setHorizontalScrollBarEnabled(false);
                        mWebView.setVerticalScrollBarEnabled(false);
                        mWebView.setScrollBarStyle(XWalkView.SCROLLBARS_OUTSIDE_INSET);

                        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                        mWebView.getSettings().setDomStorageEnabled(true);
                        mWebView.getSettings().setJavaScriptEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            WebView.setWebContentsDebuggingEnabled(true);
                        }


                    }
                })
                .setOnBehaveListenter(new IWebViewBehavorListener<T,T>() {
                    @Override
                    public boolean onJsAlert(View view, String url, String message, T result) {
                        if(mJsListener != null){
                            mJsListener.onJsAlert(view,  url, message, result);
                        }
                        return false;
                    }

                    @Override
                    public boolean onJsConfirm(View view, String url, String message, T result) {
                        if(mJsListener != null){
                            mJsListener.onJsConfirm(view, url, message, result);
                        }
//
                        return false;
                    }

                    @Override
                    public boolean onJsPrompt(View view, String url, String message, String defaultValue, T result) {
                        if(mJsListener != null){
                            mJsListener.onJsConfirm(view, url, message,result);
                        }
                        return false;
                    }

                    @Override
                    public void onReceivedTitle(View view, String title) {

                    }

                    @Override
                    public void onUnhandledKeyEvent(View view, KeyEvent event) {
//                onUnhandledKeyEvent(view, event);
                    }

                    @Override
                    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                        if(mJsListener != null){
                            mJsListener.openFileChooser(uploadMsg,AcceptType,capture);
                        }
                    }

                })
                .setOnRecycleListener(new IWebViewLifeRecycle<XWalkWebResourceRequest>() {
                    @Override
                    public void onProgressChanged(View view, int newProgress) {
                        if(listener != null){
                            listener.onProgressChanged(view, newProgress);
                        }

                    }

                    @Override
                    public void shouldOverrideUrlLoading(View view, String url) {
                        if(listener != null){
                            listener.shouldOverrideUrlLoading(view, url);
                        }
                    }

                    @Override
                    public void onPageFinished(View view, String url, Object status) {
                        if(listener != null){
                            listener.onPageFinished(view,  url, (XWalkUIClient.LoadStatus) status);
                        }
//
                    }

                    @Override
                    public void onPageStarted(View view, String url, Bitmap favicon) {
                        if(listener != null){
                            listener.onPageStarted(view, url, favicon);
                        }
//
                    }

                    @Override
                    public void onReceivedError(View view, int errorCode, String description, String failingUrl) {
//                        Log.e("process",errorCode+"");
                        if(listener != null){
                            listener.onReceivedError(view,  errorCode, description, failingUrl);
                        }
//
                    }

                    @Override
                    public void shouldInterceptLoadRequest(View view, XWalkWebResourceRequest request) {
                        if(listener != null){
                            listener.shouldInterceptLoadRequest(view, request);
                        }
//
                    }

                    @Override
                    public void doUpdateVisitedHistory(View view, String url, boolean isReload) {
                        if(listener != null){
                            listener.doUpdateVisitedHistory(view,url,isReload);
                        }
                    }
                })
                .setOnSslListener(new IWebViewSSL<android.webkit.ClientCertRequest,SslErrorHandler>() {
                    @Override
                    public void onReceivedSslError(View view, SslErrorHandler handler, SslError error) {
                        if(mSslListener != null){
                            mSslListener.onReceivedSslError(view,handler,error);
                        }
                    }

                    @Override
                    public void onReceivedClientCertRequest(View view, android.webkit.ClientCertRequest handler) {
                        if(mSslListener != null){
                            mSslListener.onReceivedClientCertRequest(view,handler);
                        }
                    }
                }).build();

        load(view);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String s = json.replace("\n","");
//                String s1 = s.replace("\t","");
//                Log.e(TAG+"0",s1);
//                mwebView.load("http://192.168.0.100:8080/sdpsbs/user/login?json=","json="+s1,null);
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG+"2","1");
//                mwebView.load("http://192.168.0.100:8080/sdpsbs/user/login",null,null);
//            }
//        });
    }



    protected abstract void init();

    public abstract BridgeProxy createBridge(IWebView webView);

    @Override
    protected void onResume() {
        mwebView.onWebResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mwebView.onWebDestory();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mwebView.onWebPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public abstract void load(View wiew);

    /**
     * 切换方案**/
    public  abstract  IWebView exchange(View v);

    /**
     * 初始化方法**/


    public void setOnhandlerRecycleListener(handlerRecycle listener){
        this.listener = listener;
    }

    public void setOnJsActionListener(JsActionListener  jsActionListener){
        this.mJsListener = jsActionListener;
    }

    public void setOnSslListener(SslListener<android.webkit.ClientCertRequest,SslErrorHandler> sslListener){
        this.mSslListener = sslListener;
    }

    /**
     * webview 生命周期*/
    public interface handlerRecycle{
        /**
         * 进度条管理**/
       void onProgressChanged(View view, int newProgress);

        /**
         * 页面重载*/
     void shouldOverrideUrlLoading(View view, String url);

        /**
         * 页面加载结束*/
        void onPageFinished(View view, String url, XWalkUIClient.LoadStatus status);

        /**
         * 页面加载开始*/
         void onPageStarted(View view, String url, Bitmap favicon);

        /**接受错误*/
        void onReceivedError(View view, int errorCode, String description, String failingUrl);


//    void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

        /**
         * 更新浏览历史记录，获取更新的url地址
         */
        void doUpdateVisitedHistory(View view, String url, boolean isReload);

        void shouldInterceptLoadRequest(View view, XWalkWebResourceRequest request);

//
//
//        /**
//         * 回调该方法，处理未被WebView处理的事件*/
//        abstract void onUnhandledKeyEvent(View view, KeyEvent event);
    }


    /***
     * Js的动作监听*/
    public interface JsActionListener<T,S>{

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

        void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture);

    }

    /**
     * ssl监听**/
    public interface SslListener<T,S>{
        void onReceivedSslError(View view, S handler, SslError error);

        void onReceivedClientCertRequest(View view, T handler);
    }

    public <W extends View> W getWebView(View view){
        return (W) view;
    }



    //设置返回按键
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something what you want
//            Log.e("BACK","3");
            if ((System.currentTimeMillis() - exitTime) > 1500) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                if(!view.getNavigationHistory().canGoBack()){
//                    view.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);//返回上一页面
                    finish();
                    System.exit(0);
                }

            }
            return true;//返回true，把事件消费掉，不会继续调用onBackPressed

        }
        return super.onKeyDown(keyCode, event);
    }
}
