package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

public class ClientMerchantConsumeRecordAdapter extends BaseAdapter<Order> {
    public ClientMerchantConsumeRecordAdapter(Context con, List<Order> data) {
        super(con, data, R.layout.item_list_client_merchant_consume_record);
    }

    @Override
    public void convert(ViewHolder viewHolder, Order order, int position, View convertView) {
        TextView time = viewHolder.getIdByView(R.id.id_time);
        TextView service = viewHolder.getIdByView(R.id.id_service);
        TextView money = viewHolder.getIdByView(R.id.id_pay_money);
        View line = viewHolder.getIdByView(R.id.id_line);

        List<ServiceInfoDetailBean> list = order.getServices();
        service.setText(AppUtils.setMerchantService(list));
        time.setText(order.getOrderTime().getDate());
        money.setText("￥" + StringUtil.setDoubleValue(order.getPrice()));
        if (position == getCount() - 1) {
            ViewUtils.hideLayout(line);
        } else {
            ViewUtils.showLayout(line);
        }

    }
}
