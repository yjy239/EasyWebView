package com.yjy.mulitwebviewproject.UI;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;

import com.google.zxing.Result;
import com.yjy.mulitwebviewproject.R;
import com.yjy.mulitwebviewproject.UI.Login.LoginActivity;
import com.yjy.mulitwebviewproject.Utils.LoginUtils;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewFactory;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewActivity;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewFactory.OrginWebViewFactory;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeHandler;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.CallBackFunction;
import com.yjy.mulitwebviewproject.Widget.NetWorkCheckReceiver;
import com.yjy.mulitwebviewproject.Widget.WelComeView.WelComeViewBuilder;
import com.szcatic.scanlibrary.activitys.ActivityScanerCode;
import com.szcatic.scanlibrary.interfaces.OnRxScanerListener;


import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends WebViewActivity<JsResult> implements WebViewActivity.handlerRecycle,
        WebViewActivity.JsActionListener<JsResult,JsPromptResult>,WebViewActivity.SslListener<android.webkit.ClientCertRequest,SslErrorHandler>{

    private static final String TAG = "MainActivity";
    private IWebView mwebView;
    private IWebViewFactory mFactory;
    private BridgeProxy proxy;
    private XWalkView mwalkView;

    private ValueCallback<Uri> mUploadMessage;
    private CallBackFunction scanfountion;

    private static final int RESULT_CODE = 1;
    private static final int LOGIN_CODE = 2;
    private List<View> views = new ArrayList<>();
    private View view;
    private View errorview;

    @Override
    protected void init() {
        view = LayoutInflater.from(this).inflate(R.layout.firstviewpage,null,false);
        errorview = LayoutInflater.from(this).inflate(R.layout.error_network_layout,null,false);
        views.add(view);
        Log.e(TAG,"INIT");

        //初始化
        initBroadcast(errorview);
    }

    private void initBroadcast(View v){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetWorkCheckReceiver(v),filter);
    }

    @Override
    public BridgeProxy createBridge(IWebView webView){
        proxy = new BridgeProxy(webView);
        return proxy;
    }

    @Override
    public void load(View web) {
        setOnhandlerRecycleListener(this);
        setOnJsActionListener(this);
        setOnSslListener(this);
//        mwebView.load("file:///android_asset/test.html",null,null);

        mwebView.load("http://192.168.0.100:8080/sdpsbs/user/login",null,null);
        WelComeViewBuilder builder = new WelComeViewBuilder().addOn(this)
                .setAdapter(views)
//                .setErrorView(errorview)
                .exitWithTopClick()
                .setOnExitListener(new WelComeViewBuilder.exitWelComeListener() {
                    @Override
                    public void AfterExit() {
                        if(!LoginUtils.isIsLogined()){
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(i,LOGIN_CODE);
                        }
                    }
                })
                .build();

    }

    @Override
    public IWebView exchange(View v) {
        mFactory = new OrginWebViewFactory();
        mwebView = mFactory.createWebView(v);
        return mwebView;
    }


    @Override
    public void onProgressChanged(View view, int newProgress) {

    }

    @Override
    public void shouldOverrideUrlLoading(View view, String url) {

    }

    @Override
    public void onPageFinished(View view, String url, XWalkUIClient.LoadStatus status) {
        proxy.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data from Java");
            }
        });
        proxy.registerHandler("openScan", new BridgeHandler() {
            @Override
            public void handler(String data, final CallBackFunction function) {
                Log.e(TAG, "114");
                Intent i = new Intent(MainActivity.this,ActivityScanerCode.class);
                startActivityForResult(i,RESULT_CODE);
                ActivityScanerCode.setScanerListener(new OnRxScanerListener() {
                    @Override
                    public void onSuccess(String type, Result result) {
                        Log.e(TAG,result.toString());
                        function.onCallBack(result.toString());
                    }

                    @Override
                    public void onFail(String type, String message) {

                    }
                });
//                QrCodeActivity.launch(MainActivity.this);
//                scanfountion = function;
            }
        });


    }

    @Override
    public void onPageStarted(View view, String url, Bitmap favicon) {

    }

    @Override
    public void onReceivedError(View view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void doUpdateVisitedHistory(View view, String url, boolean isReload) {

    }

    @Override
    public void shouldInterceptLoadRequest(View view, XWalkWebResourceRequest request) {

    }

    @Override
    public boolean onJsAlert(View view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsConfirm(View view, String url, String message, JsResult result) {
        return false;
    }

    @Override
    public boolean onJsPrompt(View view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    @Override
    public void onReceivedTitle(View view, String title) {

    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
        mUploadMessage = uploadMsg;
//        pickFile();
    }

    private void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }


    @Override
    public void onReceivedSslError(View view, SslErrorHandler handler, SslError error) {

    }

    @Override
    public void onReceivedClientCertRequest(View view, android.webkit.ClientCertRequest handler) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE){
            if (null == mUploadMessage){
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
//        if(requestCode == LOGIN_CODE){
//            Log.e(TAG,"HERE");
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("json")){
            Log.e(TAG,intent.getStringExtra("json"));

            mwebView.load("http://192.168.0.100:8080/sdpsbs/user/login?json="+intent.getStringExtra("json"),null,null);
        }
    }


}
