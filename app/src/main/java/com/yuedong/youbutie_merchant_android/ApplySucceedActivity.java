package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;

/**
 * 申请成功
 */
public class ApplySucceedActivity extends BaseActivity {
    private double applyWithdrawMoney;
    private String cardInfo;
    private TextView cardInfoTv, applyWwithdrawMoneyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        applyWithdrawMoney = extras.getDouble(Constants.KEY_INT, 0);
        cardInfo = extras.getString(Constants.KEY_TEXT);
        buildUi(new TitleViewHelper().createDefaultTitleView3("申请成功"), false, false, false, R.layout.activity_appley_succeed);
    }

    @Override
    protected void initViews() {
        cardInfoTv = fvById(R.id.id_card_info);
        applyWwithdrawMoneyTv = fvById(R.id.id_apply_withdraw_money);
    }

    @Override
    protected void initEvents() {
        fvById(R.id.id_btn_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchWithExitUtils.startActivity(activity, IncomeDetailActivity.class);
            }
        });
    }

    @Override
    protected void ui() {
        cardInfoTv.setText(cardInfo);
        applyWwithdrawMoneyTv.setText(String.format(getString(R.string.str_￥), applyWithdrawMoney + ""));
    }
}
