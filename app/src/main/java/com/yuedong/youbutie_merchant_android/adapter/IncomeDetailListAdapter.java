package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.IncomeDetailListBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class IncomeDetailListAdapter extends BaseAdapter<IncomeDetailListBean> {
    public IncomeDetailListAdapter(Context con) {
        super(con, R.layout.item_income_detail_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, IncomeDetailListBean incomeDetailListBean, int position, View convertView) {

    }
}
