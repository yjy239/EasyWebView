package com.yjy.mulitwebviewproject.Http;

import android.util.Log;

import com.yjy.mulitwebviewproject.Interfaces.BaseCallBack;

/**
 * Created by yjy on 2017/11/6.
 * 实现网络请求
 */

public class HttpDAO {

    public static void Login(String username, String password, final BaseCallBack callBack){
        OkHttpClientManager.getInstance().doOkHttpPost(HttpApi.Login)
                .addCode("username",username)
                .addCode("password",password)
                .LogGetURL()
                .setBaseCallBack(new BaseCallBack() {
                    @Override
                    public void success(Object data) {
                        try {
                            String json = data.toString();
//                            String token = json.getString("token");
//                            LoginUtils.setToken(token);
                            Log.e("json",json);
                            callBack.success(json);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void failed(int errorCode, Object data) {
                       Log.e("errorCode",data.toString());
                    }
                });

    }


}
