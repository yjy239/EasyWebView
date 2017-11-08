package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewBuilder;


import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewBehavorListener;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewLifeRecycle;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewSSL;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import java.util.HashMap;

/**
 * Created by yjy on 2017/9/30.
 * 浏览器构建类
 */

public class WebViewUtils<W extends IWebView> {


    private  W  mWebView;

    private static WebViewUtilsController mController;

    private static IWebViewBehavorListener behaveListenter;

    private  static IWebViewLifeRecycle lifeListener;

    private static IWebViewSSL sslListener;

    private static String mUrl;

    private static BridgeProxy mProxy;

    private static BaseSettingAdapter mAdapter;

    public WebViewUtils(W webView){
        mController = new WebViewUtilsController();
    }

    /**
     * 设置浏览器类型**/
    public void  init(W webView){
        this.mWebView = webView;
    }

    /**
     * 设置设置类型**/
    public void setAdapter(IWebView iWebView, BaseSettingAdapter<W> adapter){
        if(mWebView !=null){
            iWebView.setAdapter(adapter);
        }

    }

    /**绑定native方法**/
    public void addJsInterface(Object obj,String s){
        if(mWebView != null){
            mWebView.addJsInterface(obj,s);
        }

    }

    /**
     * 设置行为监听**/
    public void setOnBehaveListenter(IWebViewBehavorListener behaveListenter){
        this.behaveListenter = behaveListenter;
        mWebView.setOnBehavorListener(behaveListenter);

    }

    /**
     * 设置周期监听**/
    public void setOnRecycleListener(IWebViewLifeRecycle lifeListener){
        this.lifeListener = lifeListener;
        mWebView.setOnLifeListner(lifeListener);
    }

    /**设置ssl监听**/
    public void setOnSslListener(IWebViewSSL listener){
        sslListener = listener;
        mWebView.setOnSslLisenter(sslListener);
    }

    /**设置jsbridge**/
    public void setJsBridge(BridgeProxy proxy){
        mProxy = proxy;
        mWebView.setJsBridge(proxy);
    }


    public static class Builder<W extends IWebView>{

        private  W  mWebView;

        private WebViewUtilsController.Params mParams;
//
//        private IWebViewBehavorListener behaveListenter;
//
//        private IWebViewLifeRecycle lifeListener;
//
//        private IWebViewSSL sslListener;
//
//        private String url;


        public Builder(IWebView mWebView){
            mParams = new WebViewUtilsController.Params(mWebView);
            mParams.mWebView = mWebView;
        }
        /**
         * 设置浏览器类型**/
//        public Builder init(W  webView){
//            mParams.mWebView = webView;
//            return this;
//        }

        /**
         * 设置设置类型**/
        public Builder setAdapter(BaseSettingAdapter adapter){
//            if(mWebView !=null){
//                mWebView.setAdapter(adapter);
//            }
            mParams.mAdapter = adapter;
            return this;
        }

        /**绑定native方法**/
        public Builder addJsInterface(Object obj,String s){
           HashMap<Object,String> map = new HashMap<>();
            mParams.JsInterfaceMap = map;
            return this;
        }

        /**
         * 设置行为监听**/
        public Builder setOnBehaveListenter(IWebViewBehavorListener Listenter){
            mParams.behaveListenter = Listenter;
            return this;
        }

        /**
         * 设置周期监听**/
        public Builder setOnRecycleListener(IWebViewLifeRecycle Listener){
            mParams.lifeListener = Listener;
            return this;
        }

        /**设置ssl监听**/
        public Builder setOnSslListener(IWebViewSSL listener){
            mParams.sslListener = listener;
            return this;
        }

        public Builder load(String url){
            mParams.mFistUrl = url;
            return this;
        }

        public Builder setJsBridge(BridgeProxy proxy){
            mParams.mProxy = proxy;
            return this;
        }

        public Builder build(){
            mParams.apply();
            return this;
        }


    }
}
