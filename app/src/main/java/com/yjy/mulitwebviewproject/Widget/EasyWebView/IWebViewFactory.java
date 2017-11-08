package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.content.Context;
import android.view.View;

/**
 * Created by software1 on 2017/9/27.
 */

public interface IWebViewFactory {
    IWebView createWebView(Context context, int resId);

    IWebView createWebView(View v);
}
