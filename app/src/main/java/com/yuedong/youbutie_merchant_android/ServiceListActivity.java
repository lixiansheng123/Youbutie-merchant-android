package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.adapter.ServiceListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.mouble.db.ServiceInfoDao;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

public class ServiceListActivity extends BaseActivity implements View.OnClickListener {
    // 已经上架的服务
    private List<ServiceInfoDetailBean> alreadyAddServiceInfoDetailBeans;
    // 根据服务器上的serviceInfo的所有服务内容（构建一遍）
    private List<ServiceInfoDetailBean> serviceInfoDetailBeans = new ArrayList<ServiceInfoDetailBean>();
    private Merchant meMechant;
    private ListView listView;
    private ServiceListAdapter adapter;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meMechant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        alreadyAddServiceInfoDetailBeans = meMechant.getServiceInfo();
        initTitleView(new TitleViewHelper().createDefaultTitleView4("服务列表", "添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        setShowContentView(R.layout.activity_service_list);
    }

    @Override
    protected void initViews() {
        listView = fvById(R.id.id_service_list);
        btnConfirm = fvById(R.id.id_btn_confirm);
    }

    @Override
    protected void initEvents() {
        btnConfirm.setOnClickListener(this);
    }

    @Override
    protected void ui() {
        initData();
        adapter = new ServiceListAdapter(context, serviceInfoDetailBeans);
        adapter.setAlreadyAddServices(alreadyAddServiceInfoDetailBeans);
        listView.setAdapter(adapter);
    }

    private void initData() {
        List<ServiceInfo> serviceInfos = ServiceInfoDao.getInstance().findAll();
        for (ServiceInfo serviceInfo : serviceInfos) {
            ServiceInfoDetailBean bean = new ServiceInfoDetailBean();
            bean.name = serviceInfo.getName();
            serviceInfoDetailBeans.add(bean);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_confirm:
                final List<ServiceInfoDetailBean> confirmPutawayServices = adapter.getConfirmPutawayServices();
                if (CommonUtils.listIsNotNull(confirmPutawayServices)) {
                    dialogStatus(true);
                    Merchant updateMerchant = new Merchant();
                    updateMerchant.setServiceInfo(confirmPutawayServices);
                    updateMerchant.update(context, meMechant.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            App.getInstance().meMerchantInfoChange = true;
                            dialogStatus(false);
                            Intent intent = new Intent();
                            intent.putExtra(Constants.KEY_LIST, (Serializable) confirmPutawayServices);
                            setResult(Constants.RESULT_EDIT_SERVICE_LIST, intent);
                            defaultFinished();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            error(s);
                            dialogStatus(false);
                        }
                    });

                } else {
                    T.showShort(context, "请上架一类门店服务!");
                }
                break;
        }
    }
}