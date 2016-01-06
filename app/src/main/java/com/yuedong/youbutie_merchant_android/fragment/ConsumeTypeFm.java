package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.CountConsumeAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class ConsumeTypeFm extends BaseFragment {
    private ListView listView;
    private CountConsumeAdapter adapter;
    private Merchant meMerchant;

    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.item_vp_count_consume, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        adapter = new CountConsumeAdapter(getActivity());
        listView.setAdapter(adapter);


        getListInfo();
    }

    private void getListInfo() {
        // TODO 测试user
        MerchantEvent.getInstance().findMeMetchant(Constants.TEST_USER_ID, new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                meMerchant = list.get(0);
                List<ServiceInfoDetailBean> serviceInfos = meMerchant.getServiceInfo();
                if (CommonUtils.listIsNotNull(serviceInfos)) {

                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    @Override
    public void initEvents() {

    }


}
