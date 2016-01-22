package com.yuedong.youbutie_merchant_android;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.UmengFeedbackAgent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;

import cn.bmob.v3.BmobUser;

/**
 * 门店设置
 */
public class MerchantSettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView nickNameTv;
    private Button quitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3(getString(R.string.str_setting)), false, false, false, R.layout.activity_merchant_setting);
    }

    @Override
    protected void initViews() {
        nickNameTv = fvById(R.id.id_user_name);
        quitBtn = fvById(R.id.id_btn_quit);
    }

    @Override
    protected void initEvents() {
        fvById(R.id.id_idea_feedback_layout).setOnClickListener(this);
        fvById(R.id.id_checkout_app_update_layout).setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }

    @Override
    protected void ui() {
        User user = App.getInstance().getUser();
        nickNameTv.setText(user.getNickname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_quit:
                //清除缓存用户对象
                BmobUser.logOut(context);
                App.getInstance().setUser(null);
                ActivityTaskUtils.getInstance().delAll();
                LaunchWithExitUtils.startActivity(activity, LoginActivity.class);
                break;

            case R.id.id_checkout_app_update_layout:
                UmengUpdateAgent.forceUpdate(context);

                break;

            case R.id.id_idea_feedback_layout:
                LaunchWithExitUtils.startActivity(activity, FeedbackActivity.class);
                break;

        }
    }
}
