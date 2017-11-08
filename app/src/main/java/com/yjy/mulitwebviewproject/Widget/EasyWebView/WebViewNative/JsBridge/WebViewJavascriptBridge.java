package com.yjy.mulitwebviewproject.Widget.EasyWebView.WebViewNative.JsBridge;


public interface WebViewJavascriptBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
	

}
