package com.szcatic.scanlibrary.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



public class ActivityBase extends Activity {

    public ActivityBase mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
