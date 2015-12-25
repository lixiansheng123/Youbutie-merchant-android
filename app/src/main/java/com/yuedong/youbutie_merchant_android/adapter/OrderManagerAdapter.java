package com.yuedong.youbutie_merchant_android.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.MerchantCollectionActivity;
import com.yuedong.youbutie_merchant_android.OrderDetailActivity;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.fragment.OrderManagerFm;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 订单管理adapter
 */
public class OrderManagerAdapter extends BaseAdapter<Order> {
    public OrderManagerAdapter(Context con, List<Order> data) {
        super(con, data, R.layout.item_order_manager);
    }

    @Override
    public void convert(ViewHolder viewHolder, final Order order, final int position, View convertView) {
        NetworkImageView networkImageView = viewHolder.getIdByView(R.id.id_user_pic);
        TextView name = viewHolder.getIdByView(R.id.id_user_name);
        TextView time = viewHolder.getIdByView(R.id.id_time);
        TextView service = viewHolder.getIdByView(R.id.id_service);
        Button receiveOrder = viewHolder.getIdByView(R.id.id_btn_receive_order);
        TextView money = viewHolder.getIdByView(R.id.id_pay_money);
        TextView waitPayName = viewHolder.getIdByView(R.id.id_wait_pay_name);
        User orderUser = order.getUser();
        DisplayImageByVolleyUtils.loadImage(networkImageView, orderUser.getPhoto());
        name.setText(orderUser.getNickname());
        time.setText(order.getOrderTime().getDate());


        ViewUtils.hideLayouts(money, waitPayName, receiveOrder);
        service.setTextColor(Color.parseColor("#eeb600"));
        money.setTextColor(Color.parseColor("#ff8b3e"));
        waitPayName.setBackgroundDrawable(mCon.getResources().getDrawable(R.drawable.red_frame));
        waitPayName.setTextColor(Color.parseColor("#ff8b3e"));
        waitPayName.setText(mCon.getString(R.string.str_wait_pay));
        final int orderState = order.getState();
        switch (orderState) {
            case 1:
            case 2:
            case 3:
                List<ServiceInfoDetailBean> services = order.getServices();
                StringBuilder sb = new StringBuilder();
                for (ServiceInfoDetailBean serviceInfoDetailBean : services) {
                    if (serviceInfoDetailBean != null)
                        sb.append(serviceInfoDetailBean.name + " ");
                }
                if (sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);
                service.setText(sb.toString());
                if (orderState == 2) {
                    ViewUtils.showLayout(receiveOrder);
                    receiveOrder.setText(mCon.getString(R.string.str_collection));
                } else if (orderState == 1) {
                    ViewUtils.showLayout(receiveOrder);
                    receiveOrder.setText(mCon.getString(R.string.str_receive_order));
                } else if (orderState == 3) {
                    ViewUtils.showLayouts(money, waitPayName);
                    money.setText("￥" + order.getPrice());
                }
                break;

            case 4:
                // 已完结
                ViewUtils.showLayouts(money, waitPayName);
                money.setTextColor(Color.parseColor("#15d29d"));
                waitPayName.setTextColor(Color.parseColor("#15d29d"));
                waitPayName.setBackgroundDrawable(mCon.getResources().getDrawable(R.drawable.green_frame));
                waitPayName.setText(mCon.getString(R.string.str_already_collection));
                service.setTextColor(Color.parseColor("#938381"));
                service.setText("客户暂未评价");
                money.setText("￥" + order.getPrice());
                break;
        }

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = getItem(position);
                if (orderState == 1) {
                    final BaseActivity attachAct = (BaseActivity) mCon;
                    final OrderManagerFm orderManagerFm = (OrderManagerFm) attachAct.getSupportFragmentManager().findFragmentByTag(Constants.ORDERMANAGER_FM_TAG);
                    orderManagerFm.passFmStartOrderDetail(order, Constants.REQUESTCODE_RECEIVE_ORDER);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, order);
                    LaunchWithExitUtils.startActivity((Activity) mCon, OrderDetailActivity.class, bundle);
                }
            }
        });

        receiveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickBtn = (Button) v;
                try {
                    final BaseActivity attachAct = (BaseActivity) mCon;
                    final OrderManagerFm orderManagerFm = (OrderManagerFm) attachAct.getSupportFragmentManager().findFragmentByTag(Constants.ORDERMANAGER_FM_TAG);
                    if (mCon.getString(R.string.str_receive_order).equals(clickBtn.getText().toString())) {
                        orderManagerFm.receiveOrder(order);

                    } else if (mCon.getString(R.string.str_collection).equals(clickBtn.getText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.KEY_BEAN, order);
                        LaunchWithExitUtils.startActivity(attachAct, MerchantCollectionActivity.class, bundle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    T.showShort(mCon, "接单按钮出现错误...");
                }
            }
        });

    }
}
