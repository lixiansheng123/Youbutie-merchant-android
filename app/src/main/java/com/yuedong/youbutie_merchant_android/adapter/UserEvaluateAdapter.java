package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;

import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

public class UserEvaluateAdapter extends BaseAdapter<Order> {
    public UserEvaluateAdapter(Context con, List<Order> data) {
        super(con, data, R.layout.item_user_evaluate);
    }

    @Override
    public void convert(ViewHolder viewHolder, Order order, int position, View convertView) {
        NetworkImageView networkImageView = viewHolder.getIdByView(R.id.id_user_photo);
        TextView name = viewHolder.getIdByView(R.id.id_user_name);
        TextView time = viewHolder.getIdByView(R.id.id_time);
        TextView content = viewHolder.getIdByView(R.id.id_evaluate_content);
        RatingBar ratingBar = viewHolder.getIdByView(R.id.id_star_evaluate);
        User user = order.getUser();
        BmobDate date = order.getAppraiseTime();
        DisplayImageByVolleyUtils.loadImage(networkImageView, user.getPhoto());
        name.setText(user.getNickname());
        if (date != null) {
            time.setText(date.getDate());
        }
        content.setText(order.getContent());
        ratingBar.setRating(Math.round(order.getStar() * 5));
    }
}
