package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.UserEvent;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

public class ForgetPsdActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobileEt, testCodeEt, psdEt;
    private Button btnObtainTestCode, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("找回密码"),false,false,false,R.layout.activity_forget_psd);
    }

    @Override
    protected void initViews() {
        btnConfirm = fvById(R.id.id_btn_confirm);
        mobileEt = fvById(R.id.id_account_input);
        testCodeEt = fvById(R.id.id_input_test_code);
        psdEt = fvById(R.id.id_password_input);
        btnObtainTestCode = fvById(R.id.id_btn_get_test_code);

    }

    @Override
    protected void initEvents() {
        btnObtainTestCode.setOnClickListener(this);
        mobileEt.addTextChangedListener(textWatcher);
        testCodeEt.addTextChangedListener(textWatcher);
        psdEt.addTextChangedListener(textWatcher);
    }

    @Override
    protected void ui() {

    }

    private void btnStatus() {
        String inputA = mobileEt.getText().toString();
        String inputP = psdEt.getText().toString();
        String inputT = testCodeEt.getText().toString();
        if (StringUtil.isNotEmpty(inputA) && StringUtil.isNotEmpty(inputP) && StringUtil.isNotEmpty(inputT)) {
            btnConfirm.setOnClickListener(this);
            btnConfirm.setBackgroundResource(R.drawable.bg_round_yellow);
        } else {
            btnConfirm.setOnClickListener(null);
            btnConfirm.setBackgroundResource(R.drawable.bg_round_grey);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_confirm:
                mobileTestCodeResetPsd(mobileEt.getText().toString(), testCodeEt.getText().toString(), psdEt.getText().toString());
                break;

            case R.id.id_btn_get_test_code:
                UserEvent.getInstance().requestSms(ForgetPsdActivity.this, btnObtainTestCode, mobileEt.getText().toString());
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            btnStatus();
        }
    };

    /**
     * 通过手机号验证码重置密码
     *
     * @param mobileNum
     * @param testCode
     * @param newPsd
     */
    private void mobileTestCodeResetPsd(String mobileNum, String testCode, String newPsd) {
        dialogStatus(true);
        BmobUser.resetPasswordBySMSCode(context, testCode, newPsd, new ResetPasswordByCodeListener() {
            @Override
            public void done(BmobException e) {
                dialogStatus(false);
                if (e == null) {
                    T.showLong(context, "密码重置成功");
                    defaultFinished();
                } else {
                    T.showLong(context, "错误码：" + e.getErrorCode() + ",错误原因：" + e.getLocalizedMessage());
                }
            }
        });
    }

}
