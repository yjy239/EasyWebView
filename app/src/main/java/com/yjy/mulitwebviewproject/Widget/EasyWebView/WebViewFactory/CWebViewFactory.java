package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewFactory;

import android.content.Context;
import android.view.View;

import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewFactory;


/**
 * Created by software1 on 2017/9/27.
 */

public class CWebViewFactory implements IWebViewFactory {
    @Override
    public IWebView createWebView(Context context, int resid) {
        return new CWebView(context,resid);
    }

    @Override
    public IWebView createWebView(View v) {
        return new CWebView(v);
    }
}
