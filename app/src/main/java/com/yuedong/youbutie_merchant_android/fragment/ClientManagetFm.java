package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

/**
 * Created by Administrator on 2015/12/25.
 * 客户管理fm
 */
public class ClientManagetFm extends BaseFragment {
    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("客户管理"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_client_manager, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {

    }

    @Override
    public void initEvents() {

    }
}
