package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.T;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO login
        login();
    }

    private void login() {
        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername("android");
        bmobUser.setPassword("000000");
        bmobUser.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                LaunchWithExitUtils.startActivity(activity,MainActivity.class);
                defaultFinished();
            }

            @Override
            public void onFailure(int i, String s) {
                error(s);
            }
        });
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {

    }
}
