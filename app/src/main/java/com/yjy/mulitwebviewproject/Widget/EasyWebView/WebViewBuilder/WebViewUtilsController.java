package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewBuilder;



import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewBehavorListener;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewLifeRecycle;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewSSL;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge.BridgeProxy;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewSettingAdapter.BaseSettingAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by software1 on 2017/10/11.
 */

public class WebViewUtilsController {




    public WebViewUtilsController(){

    }

    public static class Params{

        public IWebViewBehavorListener behaveListenter;

        public IWebViewLifeRecycle lifeListener;

        public IWebViewSSL sslListener;

        public  String mFistUrl;

        public BridgeProxy mProxy;

        public BaseSettingAdapter mAdapter;

        public HashMap<Object,String> JsInterfaceMap;

        public IWebView mWebView;

        public Params(IWebView web){
            mWebView = web;
        }

        public void apply(){
            if(mWebView != null){


                if(mAdapter != null){
                    mWebView.setAdapter(mAdapter);
                }

                if(mProxy != null){
                    mWebView.setJsBridge(mProxy);
                }else if(JsInterfaceMap != null){
                    for(Map.Entry<Object,String> item : JsInterfaceMap.entrySet()){
                        mWebView.addJsInterface(item.getKey(),item.getValue());

                    }
                }

                if(behaveListenter != null){
                    mWebView.setOnBehavorListener(behaveListenter);
                }

                if(lifeListener != null){
                    mWebView.setOnLifeListner(lifeListener);
                }

                if(sslListener != null){
                    mWebView.setOnSslLisenter(sslListener);
                }

                if(mFistUrl != null&&!mFistUrl.equals("")){
                    mWebView.load(mFistUrl,null,null);
                }



            }
        }

    }

}
