package com.yjy.mulitwebviewproject.Http;


import com.yjy.mulitwebviewproject.Utils.MD5;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/7/5.
 */
public class HttpApi {


//    "http://api.global.football.gamenew100.com"
    //主服务器地址
    private static final String mainURL = "http://139.199.229.78/znwg/";
    /**图片地址*/
    private static final String imageURL = "http://football9.b0.upaiyun.com";
    //签名用到的randomKey
    private static final String randomKey = "123456";

    public static final String imageLoadURL = "http://v0.api.upyun.com/football9";

    //返回值为{code:0,content:响应数据,message:错误详情}，code为0时表示成功，其它均为失败

    //region
    /**
     * 登录*/
    public static final String Login  ="api/login";



    //end region

    //获取post请求URL
    public static String getRouterURL(String router) {
        return mainURL + router;
    }


    //获取post form
    public static FormBody getFormBody(String requestStr) {
        String signData = HttpApi.getSignString(requestStr);
//        Log.e("FormBody", requestStr);
//        Log.e("FormBody", signData);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("code", requestStr)
                .add("sign", signData);
        return builder.build();
    }

    //签名
    public static String getSignString(String sign) {
        String signData = MD5.getMD5(sign) + randomKey;
//        signData = signData.toUpperCase();
        signData = MD5.getMD5(signData);

        return signData;
    }

    //获取图片完整url
    public static String getFullImageUrl(String imageUrl) {
        if (imageUrl == null)
            return "";
        if (imageUrl.contains("http://")) {
            return imageUrl;
        } else {
            return imageURL + "/" + imageUrl;
        }
    }

    public static String getFullImageUrl(String imageURL, boolean small) {
        if (imageURL == null) return "";
        String fullImageUrl = getFullImageUrl(imageURL);
        if (small) {
            fullImageUrl = fullImageUrl + "!/fwfh/300x300";
        }
        return fullImageUrl;
    }

}
