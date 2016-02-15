package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.yuedong.youbutie_merchant_android.framework.BaseActivity;

/**
 */
public class AboutMeActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("关于我们")//
                , false, false, false, R.layout.activity_about_me);
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
