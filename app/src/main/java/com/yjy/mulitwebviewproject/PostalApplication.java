package com.yjy.mulitwebviewproject;

import android.app.Application;

import com.yjy.mulitwebviewproject.Utils.SystemUtils;

/**
 * Created by software1 on 2017/11/2.
 */

public class PostalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SystemUtils.setContext(this);
    }
}
