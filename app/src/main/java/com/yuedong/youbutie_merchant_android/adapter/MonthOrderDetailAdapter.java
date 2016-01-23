package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.List;

public class MonthOrderDetailAdapter extends BaseAdapter<Order> {
    public MonthOrderDetailAdapter(Context con, List<Order> data) {
        super(con, data, R.layout.item_month_order_detail);
    }

    @Override
    public void convert(ViewHolder viewHolder, Order order, int position, View convertView) {
        RoundImageView roundImageView = viewHolder.getIdByView(R.id.id_user_pic);
        TextView userName = viewHolder.getIdByView(R.id.id_user_name);
        TextView services = viewHolder.getIdByView(R.id.id_order_service);
        TextView orderPrice = viewHolder.getIdByView(R.id.id_order_price);
        TextView downOrderTime = viewHolder.getIdByView(R.id.id_down_order_time);
        User orderUser = order.getUser();
        List<ServiceInfoDetailBean> merchantService = order.getServices();
        String s = AppUtils.setMerchantService(merchantService);
        services.setText(s);
        DisplayImageByVolleyUtils.loadImage(orderUser.getPhoto(), roundImageView);
        userName.setText(orderUser.getNickname());
        orderPrice.setText("￥" + order.getPrice());
        downOrderTime.setText(order.getOrderTime().getDate());
    }
}
