package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.DrawMoneyRecordEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.DrawMoneyRecord;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;

import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 申请提现
 */
public class ApplyWithdrawActivity extends BaseActivity implements View.OnClickListener {
    private TextView canWithdrawMoneyTv, bankcardInfoTv;
    private EditText inputMoneyEt;
    private User user;
    private Merchant merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        buildUi(new TitleViewHelper().createDefaultTitleView3("申请提现"), false, false, false, R.layout.activity_apply_withdraw);
    }

    @Override
    protected void initViews() {
        canWithdrawMoneyTv = fvById(R.id.id_can_withdraw_money);
        bankcardInfoTv = fvById(R.id.id_bankcard_info);
        inputMoneyEt = fvById(R.id.id_input_box);
    }

    @Override
    protected void initEvents() {
        fvById(R.id.id_btn_submit_apply).setOnClickListener(this);
    }

    @Override
    protected void ui() {
        user = App.getInstance().getUser();
        Double cash = user.getCash();
        String bankCardNumber = user.getBankCard();
        String carUserName = user.getCardName();
        if (cash != null)
            canWithdrawMoneyTv.setText(String.format(getString(R.string.str_￥), cash + ""));
        if (bankCardNumber != null && carUserName != null && bankCardNumber.length() > 10) {
            int carNumberLength = bankCardNumber.length();
            L.d("carNumberLength:--------------------------->" + carNumberLength);
            String startCardNum = bankCardNumber.substring(0, 6);
            String endCarNum = bankCardNumber.substring(carNumberLength - 4, carNumberLength);
            int signCount = carNumberLength - 10;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < signCount; i++) {
                sb.append("*");
            }
            String fullSignCarNumber = startCardNum + sb.toString() + endCarNum;
            L.d("fullSignCarNumber:---------------------->>" + fullSignCarNumber);
            if (fullSignCarNumber.length() == carNumberLength) {
                bankcardInfoTv.setText(fullSignCarNumber + "(" + carUserName + ")");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_submit_apply:
                String inputContent = inputMoneyEt.getText().toString();
                if (StringUtil.isNotEmpty(inputContent)) {
                    final double inputMoneyD = Double.parseDouble(inputContent);
                    Double cash = user.getCash();
                    if (cash == null)
                        cash = 0.0;
                    if (inputMoneyD <= cash) {
                        if (inputMoneyD <= 1000) {
                            // 查询当天提现次数
                            DrawMoneyRecordEvent.getInstance().queryCurDayWithdrawCount(user.getObjectId(), new CountListener() {

                                @Override
                                public void onStart() {
                                    dialogStatus(true);
                                }

                                @Override
                                public void onSuccess(int i) {
                                    if (i < 3) {
                                        DrawMoneyRecord drawMoneyRecord = new DrawMoneyRecord();
                                        drawMoneyRecord.setState(0);
                                        drawMoneyRecord.setMoney(inputMoneyD);
                                        drawMoneyRecord.setMerchant(merchant);
                                        drawMoneyRecord.setUser(user);
                                        drawMoneyRecord.save(context, new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                User updateUser = new User();
                                                Double userCash = user.getCash();
                                                Double drawTotalCash = user.getDrawTotalCash();
                                                Integer drawCount = user.getDrawCount();
                                                if (drawTotalCash == null) {
                                                    drawTotalCash = 0.0;
                                                }
                                                if (drawCount == null)
                                                    drawCount = 0;
                                                userCash = userCash - inputMoneyD;
                                                drawTotalCash = drawTotalCash + inputMoneyD;
                                                drawCount++;
                                                // 更新用户可以提现金额
                                                updateUser.setCash(userCash);
                                                // 更新用户提总金额
                                                updateUser.setDrawTotalCash(drawTotalCash);
                                                // 更新用户提现次数
                                                updateUser.setDrawCount(drawCount);
                                                updateUser.update(context, user.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        App.getInstance().userInfoChange = true;
                                                        dialogStatus(false);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putDouble(Constants.KEY_INT, inputMoneyD);
                                                        bundle.putString(Constants.KEY_TEXT, bankcardInfoTv.getText().toString());
                                                        LaunchWithExitUtils.startActivity(activity, ApplySucceedActivity.class, bundle);
                                                    }

                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                        dialogStatus(false);
                                                        error(s);
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                dialogStatus(false);
                                                error(s);
                                            }
                                        });
                                    } else {
                                        dialogStatus(false);
                                        T.showShort(context, "一天申请提现不能超过3次");
                                    }
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    dialogStatus(false);
                                    error(s);
                                }
                            });

                        } else {
                            T.showShort(context, "单次提现不能大于1000");
                        }
                    } else {
                        T.showShort(context, "提现金额不能不大可体现金额");
                    }
                } else {
                    T.showShort(context, "请输入提现金额");
                }
                break;
        }
    }
}
