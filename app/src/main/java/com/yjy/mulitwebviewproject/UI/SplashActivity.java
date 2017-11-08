package com.yjy.mulitwebviewproject.UI;

import android.content.Intent;

import com.yjy.mulitwebviewproject.R;


/**
 * Created by software1 on 2017/9/27.
 */

public class SplashActivity extends BaseActivity {


    @Override
    public int findView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void initEvent() {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
