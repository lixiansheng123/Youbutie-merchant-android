package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.CountConsumeBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/1/6.
 */
public class CountConsumeAdapter extends BaseAdapter<CountConsumeBean> {
    public CountConsumeAdapter(Context context) {
        super(context, R.layout.item_count_consume);
    }

    @Override
    public void convert(ViewHolder viewHolder, CountConsumeBean countConsumeBean, int position, View convertView) {

    }
}
