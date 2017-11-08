package com.yjy.mulitwebviewproject.Widget.WelComeView;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.yjy.mulitwebviewproject.R;

import java.util.List;

/**
 * Created by software1 on 2017/10/30.
 */

public class WelComeViewBuilder {

    private ViewPager mWelComeView;
    private FrameLayout rootView;
    private WelComeViewPagerAdapter mAdapter;
    private long times;
    private LayoutInflater inflater;
    private View view;
    private ImageView mTopButton;
    private ImageView mBottomButton;
    private Handler mHandler;
    private View errorView;
    private Activity mContext;
    private exitWelComeListener listener;

    public WelComeViewBuilder(){

    }

    public WelComeViewBuilder addOn(Activity context){
        rootView = (FrameLayout) context.getWindow().getDecorView();
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.activity_welcome,null,false);
        mWelComeView = (ViewPager)view.findViewById(R.id.viewpage);
        mTopButton = (ImageView)view.findViewById(R.id.exitTop);
        mBottomButton = (ImageView)view.findViewById(R.id.exitButton);
        mHandler = new Handler();
        mContext = context;
        return this;
    }

    //viewpager的适配器
    public WelComeViewBuilder setAdapter(List<View> views){
        if(mAdapter == null){
            mAdapter = new WelComeViewPagerAdapter(views);
        }

        return this;
    }

    //设置网络错误页面
    public WelComeViewBuilder setErrorView(View v){
        errorView = v;
        return this;
    }


    //按照规定时间推出当前view
    public WelComeViewBuilder exitWithTime(long times){
        this.times = times;
        return this;
    }

    //点击下方按钮退出当前界面
    public WelComeViewBuilder exitWithBottomClick(){
        if(mBottomButton !=null){
            mBottomButton.setVisibility(View.VISIBLE);
            mBottomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.GONE);
                    mHandler.removeCallbacks(runnable);
                    if(errorView !=null){
                        if(isNetworkConnected(mContext)){
                            errorView.setVisibility(View.GONE);
                        }else {
                            errorView.setVisibility(View.VISIBLE);
                        }
                    }
                    if(listener != null){
                        listener.AfterExit();
                    }
                }
            });
        }

        return this;
    }

    //点击右上方退出退出当前界面1
    public WelComeViewBuilder exitWithTopClick(){
        if(mTopButton != null){
            mTopButton.setVisibility(View.VISIBLE);
            mTopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.GONE);
                    mHandler.removeCallbacks(runnable);
                    if(errorView !=null){
                        if(isNetworkConnected(mContext)){
                            errorView.setVisibility(View.GONE);
                        }else {
                            errorView.setVisibility(View.VISIBLE);
                        }
                    }
                    if(listener != null){
                        listener.AfterExit();
                    }
                }
            });
        }

        return this;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(view !=null){
                view.setVisibility(View.GONE);
                if(errorView !=null){
                    if(isNetworkConnected(mContext)){
                        errorView.setVisibility(View.GONE);
                    }else {
                        errorView.setVisibility(View.VISIBLE);
                    }
                }
                mHandler.removeCallbacks(this);
                if(listener != null){
                    listener.AfterExit();
                }
            }
        }
    };

    public WelComeViewBuilder setOnExitListener(exitWelComeListener listener){
        this.listener = listener;
        return this;
    }


    //构建
    public WelComeViewBuilder build(){
        if(mAdapter !=null){
            mWelComeView.setAdapter(mAdapter);
        }
        if(errorView !=null){
            rootView.addView(errorView);
        }
        if(view != null){
            rootView.addView(view);
        }

        if(times == 0){
            times = 3000;
        }
        mHandler.postDelayed(runnable,5000);

        return this;
    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }



    public interface exitWelComeListener{

        void AfterExit();

    }








}
