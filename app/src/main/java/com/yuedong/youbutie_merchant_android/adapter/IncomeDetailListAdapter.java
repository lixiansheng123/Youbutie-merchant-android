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
    public IncomeDetailListAdapter(Context con, List<IncomeDetailListBean> data) {
        super(con, data, R.layout.item_income_detail_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, IncomeDetailListBean incomeDetailListBean, int position, View convertView) {
        viewHolder.setText(R.id.id_time_des, incomeDetailListBean.getDayDes())//
                .setText(R.id.id_order_num, incomeDetailListBean.getOrderNumber() + "单")//
                .setText(R.id.id_order_total_money, "￥" + incomeDetailListBean.getTotalMoney());
    }
}
