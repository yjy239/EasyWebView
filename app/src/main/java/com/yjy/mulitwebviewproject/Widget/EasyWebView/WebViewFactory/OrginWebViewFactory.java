package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewFactory;

import android.content.Context;
import android.view.View;

import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebView;
import com.yjy.mulitwebviewproject.Widget.EasyWebView.IWebViewFactory;

/**
 * Created by software1 on 2017/11/7.
 */

public class OrginWebViewFactory implements IWebViewFactory {
    @Override
    public IWebView createWebView(Context context, int resId) {
        return new OrginWebView(context,resId);
    }

    @Override
    public IWebView createWebView(View v) {
        return new OrginWebView(v);
    }
}
