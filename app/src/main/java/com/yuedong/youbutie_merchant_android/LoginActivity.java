package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText acctionInputEt, psdInputEt;
    private Button btnLogin;
    private TextView forgetPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView2(getString(R.string.str_login)), false, false, false, R.layout.activity_login);
    }

    private void login() {
        BmobUser.loginByAccount(context, acctionInputEt.getText().toString(), psdInputEt.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                dialogStatus(false);
                if (e == null) {
                    dialogStatus(false);
                    LaunchWithExitUtils.startActivity(activity, MainActivity.class);
                    defaultFinished();
                } else {
                    error(e.getMessage());
                }

            }
        });
    }

    @Override
    protected void initViews() {
        acctionInputEt = fvById(R.id.id_account_input);
        psdInputEt = fvById(R.id.id_password_input);
        btnLogin = fvById(R.id.id_btn_login);
        forgetPasswordTv = fvById(R.id.id_forget_psd);
    }

    @Override
    protected void initEvents() {
        forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchWithExitUtils.startActivity(activity, ForgetPsdActivity.class);
            }
        });
        acctionInputEt.addTextChangedListener(textWatcher);
        psdInputEt.addTextChangedListener(textWatcher);
    }

    private void btnStatus() {
        String inputA = acctionInputEt.getText().toString();
        String inputP = psdInputEt.getText().toString();
        if (StringUtil.isNotEmpty(inputA) && StringUtil.isNotEmpty(inputP)) {
            btnLogin.setOnClickListener(this);
            btnLogin.setBackgroundResource(R.drawable.bg_round_yellow);
        } else {
            btnLogin.setOnClickListener(null);
            btnLogin.setBackgroundResource(R.drawable.bg_round_grey);
        }
    }

    @Override
    protected void ui() {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_login:
                dialogStatus(true);
                // 先查询是否是商家端的帐号
                BmobQuery<User> userBmobQuery = new BmobQuery<User>();
                userBmobQuery.addWhereEqualTo("mobilePhoneNumber", acctionInputEt.getText().toString());
                userBmobQuery.findObjects(context, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {

                        if (CommonUtils.listIsNotNull(list)) {
                            User user = list.get(0);
                            if (user.getType() == 2) {
                                login();
                            } else {
                                dialogStatus(false);
                                T.showShort(context, "该帐号不是商家端帐号");
                            }
                        } else {
                            dialogStatus(false);
                            T.showShort(context, "不存在该帐号或没有对手机号码进行验证 请检查");
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        dialogStatus(false);
                        error(s);
                    }
                });
                break;
        }
    }
}
