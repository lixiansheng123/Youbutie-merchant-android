package com.yuedong.youbutie_merchant_android;

import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuedong.youbutie_merchant_android.adapter.UserListAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;

public class UserListActivity extends BaseActivity {
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
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
