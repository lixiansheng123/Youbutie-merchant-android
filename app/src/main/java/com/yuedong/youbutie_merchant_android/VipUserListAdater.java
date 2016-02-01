package com.yuedong.youbutie_merchant_android;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DimenUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.List;

public class VipUserListAdater extends BaseAdapter<Order> {
    public VipUserListAdater(Context con, List<Order> data) {
        super(con, data, R.layout.item_vip_user);
    }

    @Override
    public void convert(ViewHolder viewHolder, Order order, int position, View convertView) {
        User user = order.getUser();
        RoundImageView userPic = viewHolder.getIdByView(R.id.id_user_pic);
        TextView userName = viewHolder.getIdByView(R.id.id_user_name);
        TextView buyNum = viewHolder.getIdByView(R.id.id_buy_num);
        DisplayImageByVolleyUtils.loadUserHead(user.getPhoto(), userPic);
        userName.setText(user.getNickname());
        if (order.buyNum != null) {
            if (order.buyNum == 1) {
                buyNum.setText("首次来店");
            } else {
                // 设置不textView不同的文字大小
                String str = String.format(mCon.getString(R.string.str_buy_num), order.buyNum);
                Spannable textSizesPannable = new SpannableString(str);
                textSizesPannable.setSpan(new AbsoluteSizeSpan(DimenUtils.sp2px(mCon, 18)), 2, (2 + (order.buyNum + "").length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                buyNum.setText(textSizesPannable);
            }
        }
    }
}
