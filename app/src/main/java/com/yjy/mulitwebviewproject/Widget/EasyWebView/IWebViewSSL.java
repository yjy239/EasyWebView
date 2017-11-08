package com.yjy.mulitwebviewproject.Widget.EasyWebView;

import android.net.http.SslError;
import android.view.View;

/**
 * Created by yjy on 2017/10/9.
 * ssl接口
 */

public interface IWebViewSSL<T,S>{

        void onReceivedSslError(View view, S handler, SslError error);

        void onReceivedClientCertRequest(View view, T handler);
}
