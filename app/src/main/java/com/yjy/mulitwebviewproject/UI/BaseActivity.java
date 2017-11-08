package com.yjy.mulitwebviewproject.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by software1 on 2017/9/27.
 */

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(findView());
        initView();
        loadData();
        initEvent();
    }

    /***
     *装载视图*/
    public abstract int findView();

    /**
     * 初始化视图**/
    public abstract void initView();


    /**
     *录入数据 **/
    public abstract void loadData();


    /***
     * 初始化事件*/
    public abstract void initEvent();



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
