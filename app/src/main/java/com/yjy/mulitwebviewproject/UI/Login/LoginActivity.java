package com.yjy.mulitwebviewproject.UI.Login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yjy.mulitwebviewproject.Http.HttpDAO;
import com.yjy.mulitwebviewproject.Interfaces.BaseCallBack;
import com.yjy.mulitwebviewproject.R;
import com.yjy.mulitwebviewproject.UI.BaseActivity;
import com.yjy.mulitwebviewproject.UI.MainActivity;
import com.yjy.mulitwebviewproject.Utils.LoginUtils;
import com.yjy.mulitwebviewproject.Utils.MD5;
import com.yjy.mulitwebviewproject.Utils.SystemUtils;


/**
 * Created by software1 on 2017/11/2.
 */

public class LoginActivity extends BaseActivity {

    private TextView title;
    private EditText mUserEdit;
    private EditText mPwEdit;
    private Button mLoginButton;
    private ImageView back;
    private int LOGIN_CODE = 2;

    @Override
    public int findView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        title = (TextView)findViewById(R.id.title_name);
        title.setText("登录");
        mUserEdit = (EditText)findViewById(R.id.telnum);
        mPwEdit = (EditText)findViewById(R.id.pwnum);
        mLoginButton = (Button)findViewById(R.id.login_in);
        back = (ImageView)findViewById(R.id.back_button);
        if(LoginUtils.getUsername() != null && !LoginUtils.getUsername().isEmpty()){
            mUserEdit.setText(LoginUtils.getUsername());
        }
        if(LoginUtils.getPassword() != null && !LoginUtils.getPassword().isEmpty()){
            mPwEdit.setText(LoginUtils.getPassword());
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    public void initEvent() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mUserEdit.getText().toString())) {
                    Toast.makeText(SystemUtils.getContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mPwEdit.getText().toString())) {
                    Toast.makeText(SystemUtils.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = mPwEdit.getText().toString();
                Log.e("MD5加密前",s);
                String s1 = MD5.KL(s);
                Log.e("MD5加密",s1);
                Log.e("MD5解密后",MD5.JM(s1));
                HttpDAO.Login("admin", "admin", new BaseCallBack() {
                    @Override
                    public void success(Object data) {
                        Log.e("json",data.toString());
                        LoginUtils.setUsername(mUserEdit.getText().toString());
                        LoginUtils.setPassword(mPwEdit.getText().toString());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("json",data.toString());
                        startActivity(i);
//                        setResult(LOGIN_CODE,i);
//                        finish();

                    }

                    @Override
                    public void failed(int errorCode, Object data) {

                    }
                });

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
