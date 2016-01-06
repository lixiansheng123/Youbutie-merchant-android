package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.CountConsumeAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

/**
 * Created by Administrator on 2016/1/6.
 */
public class ConsumeTypeFm extends BaseFragment {
    private ListView listView;
    private CountConsumeAdapter adapter;

    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.item_vp_count_consume, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        adapter = new CountConsumeAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    public void initEvents() {

    }
}
