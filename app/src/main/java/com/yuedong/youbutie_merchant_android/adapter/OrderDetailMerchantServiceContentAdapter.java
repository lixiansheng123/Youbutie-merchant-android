package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/12/19.
 */
public class OrderDetailMerchantServiceContentAdapter extends BaseAdapter<ServiceInfoDetailBean> {
    public OrderDetailMerchantServiceContentAdapter(Context con, List<ServiceInfoDetailBean> data) {
        super(con, data, R.layout.item_order_detail_service_content);
    }

    @Override
    public void convert(ViewHolder viewHolder, ServiceInfoDetailBean serviceInfoDetailBean, int position, View convertView) {
        viewHolder.setText(R.id.id_service_name, serviceInfoDetailBean.name);
    }
}
