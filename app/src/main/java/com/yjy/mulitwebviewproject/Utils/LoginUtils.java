package com.yjy.mulitwebviewproject.Utils;

/**
 * Created by software1 on 2017/11/2.
 */

public class LoginUtils {
    private static String token;
    private static String userId;
    private static String username;
    private static boolean isLogined = false;

    public static String getUsername() {
        String s = SystemUtils.getSharedString("username");
        if(s != null){
            return MD5.JM(s);
        }else {
            return null;
        }

    }

    public static void setUsername(String username) {
        SystemUtils.setSharedString("username",MD5.KL(username));
    }

    public static String getPassword() {
        String s = SystemUtils.getSharedString("password");
        if(s != null){
            return MD5.JM(s);
        }else {
            return null;
        }
    }

    public static void setPassword(String password) {
        SystemUtils.setSharedString("password",MD5.KL(password));
    }

    private static String password;

    public static String getToken() {
        return SystemUtils.getSharedString("token");
    }

    public static void setToken(String token) {
        SystemUtils.setSharedString("token",token);
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        LoginUtils.userId = userId;
    }

    public static void setLogined(boolean isLogin){
        SystemUtils.setSharedBoolean("isLogin",isLogin);
    }

    public static boolean isIsLogined(){
        return SystemUtils.getSharedBoolean("isLogin",false);
    }



}
