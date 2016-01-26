package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;

import cn.bmob.v3.listener.UpdateListener;

public class InfoEditActivity extends BaseActivity {
    private EditText inputbox;
    public static final int ACTION_INPUT_AD_TITLE = 0x001;
    public static final int ACTION_INPUT_MEMBER_AD = 0x002;
    public static final int ACTION_INPUT_MEMBER_NAME = 0x003;
    public static final int ACTION_INPUT_MEMBER_LOCATION = 0x004;
    public static final int ACTION_INPUT_MEMBER_TEL = 0x005;
    public static final int ACTION_INPUT_MILEAGE = 0x006;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(null, false, false, false, R.layout.activity_info_edit);
    }

    @Override
    protected void initViews() {
        inputbox = fvById(R.id.id_input_box);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        final Bundle paramsBundle = getIntent().getExtras();
        String title = paramsBundle.getString(Constants.KEY_TEXT);
        final int action = paramsBundle.getInt(Constants.KEY_ACTION);
        String tips = paramsBundle.getString(Constants.KEY_TEXT2);
        initTitleView(new TitleViewHelper().createDefaultTitleView4(title, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputbox.getText().toString();
                if (StringUtil.isNotEmpty(inputText)) {
                    if (action == ACTION_INPUT_AD_TITLE || action == ACTION_INPUT_MILEAGE) {
                        Intent it = new Intent();
                        it.putExtra(Constants.KEY_TEXT, inputText);
                        int resultCode = 0;
                        if (action == ACTION_INPUT_AD_TITLE)
                            resultCode = Constants.RESULT_INPUT_AD_TITLE;
                        else
                            resultCode = Constants.RESULT_INPUT_MILEAGE;
                        setResult(resultCode, it);
                        defaultFinished();
                    } else if (action == ACTION_INPUT_MEMBER_AD) {
                        Merchant updateMerchant = new Merchant();
                        updateMerchant.setIntroduced(inputText);
                        updateMerchantInfo(updateMerchant, inputText, Constants.RESULT_MERCHANT_AD);
                    } else if (action == ACTION_INPUT_MEMBER_NAME) {
                        Merchant updateMerchant = new Merchant();
                        updateMerchant.setName(inputText);
                        updateMerchantInfo(updateMerchant, inputText, Constants.RESULT_MERCHANT_NAME);
                    } else if (action == ACTION_INPUT_MEMBER_LOCATION) {
                        Merchant updateMerchant = new Merchant();
                        updateMerchant.setAddress(inputText);
                        updateMerchantInfo(updateMerchant, inputText, Constants.RESULT_MERCHANT_LOCATION);
                    } else if (action == ACTION_INPUT_MEMBER_TEL) {
                        Merchant updateMerchant = new Merchant();
                        updateMerchant.setTelephone(inputText);
                        updateMerchantInfo(updateMerchant, inputText, Constants.RESULT_MERCHANT_TEL);
                    }

                } else {
                    T.showShort(context, "请输入内容!");
                }
            }

        }));
        if (StringUtil.isNotEmpty(tips)) {
            inputbox.setHint("提示:" + tips);
        }
        // 增加限制
        int maxLength = 999999999;
        if (action == ACTION_INPUT_MILEAGE) {
            inputbox.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (action == ACTION_INPUT_MEMBER_NAME || action == ACTION_INPUT_AD_TITLE || action == ACTION_INPUT_MEMBER_LOCATION || action == ACTION_INPUT_AD_TITLE) {
            maxLength = 20;
        } else if (action == ACTION_INPUT_MEMBER_TEL)
            maxLength = 15;
        inputbox.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

    }

    private void updateMerchantInfo(Merchant updateMerchant, final String modifyText, final int resultCode) {
        dialogStatus(true);
        Merchant merchant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        updateMerchant.update(context, merchant.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_TEXT, modifyText);
                dialogStatus(false);
                App.getInstance().meMerchantInfoChange = true;
                setResult(resultCode, intent);
                defaultFinished();
            }

            @Override
            public void onFailure(int i, String s) {
                dialogStatus(false);
                error(s);
            }
        });
    }
}
