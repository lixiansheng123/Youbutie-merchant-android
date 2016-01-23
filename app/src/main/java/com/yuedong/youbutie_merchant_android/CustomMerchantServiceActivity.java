package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;

public class CustomMerchantServiceActivity extends BaseActivity {
    private EditText inputServiceContent, inputServicePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView4("添加服务", "完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = inputServiceContent.getText().toString();
                String servicePrice = inputServicePrice.getText().toString();
                if (StringUtil.isNotEmpty(serviceName) && StringUtil.isNotEmpty(servicePrice)) {
                    ServiceInfoDetailBean newBean = new ServiceInfoDetailBean();
                    newBean.name = serviceName;
                    newBean.icon = "service_default";
                    newBean.objectId = "";
                    newBean.state = 0;
                    newBean.price = Double.parseDouble(servicePrice);
                    Intent it = new Intent();
                    it.putExtra(Constants.KEY_BEAN, newBean);
                    setResult(Constants.RESULT_CUSTOM_MERCHANT_SERVICE, it);
                    defaultFinished();
                } else {
                    T.showShort(context, "请填写完整信息");
                }
            }
        }), false, false, false, R.layout.activity_custom_merchant_service);
    }

    @Override
    protected void initViews() {
        inputServiceContent = fvById(R.id.id_input_service_content);
        inputServicePrice = fvById(R.id.id_input_service_price);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {

    }
}
