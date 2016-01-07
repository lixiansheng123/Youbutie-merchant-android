package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DimenUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.List;

public class UserListAdapter extends BaseAdapter<Order> {
    private List<Vips> merchantVipsList;

    public void setMerchantVipUser(List<Vips> vipsList) {
        this.merchantVipsList = vipsList;
    }

    public UserListAdapter(Context con, List<Order> data) {
        super(con, data, R.layout.item_user_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, Order o, int position, View convertView) {
        User user = o.getUser();
        RoundImageView userPic = viewHolder.getIdByView(R.id.id_user_pic);
        TextView userName = viewHolder.getIdByView(R.id.id_user_name);
        ImageView iconVip = viewHolder.getIdByView(R.id.id_icon_vip);
        TextView service = viewHolder.getIdByView(R.id.id_service);
        TextView money = viewHolder.getIdByView(R.id.id_pay_money);
        TextView buyNum = viewHolder.getIdByView(R.id.id_buy_num);
        TextView downOrderTime = viewHolder.getIdByView(R.id.id_down_order_time);
        //-----------------------设值------------------------
        userName.setText(user.getNickname());
        DisplayImageByVolleyUtils.loadImage(user.getPhoto(), userPic);
        money.setText("￥" + StringUtil.setDoubleValueCastE(o.getPrice()));
        if (o.buyNum == 1) {
            buyNum.setText("首次来店");
        } else {
            // 设置不textView不同的文字大小
            String str = String.format(mCon.getString(R.string.str_buy_num), o.buyNum);
            Spannable textSizesPannable = new SpannableString(str);
            textSizesPannable.setSpan(new AbsoluteSizeSpan(DimenUtils.sp2px(mCon, 18)), 2, (2 + (o.buyNum + "").length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            buyNum.setText(textSizesPannable);
        }
        List<ServiceInfoDetailBean> serviceInfoDetailBeans = o.getServices();
        StringBuilder sb = new StringBuilder();
        for (ServiceInfoDetailBean bean : serviceInfoDetailBeans) {
            sb.append(bean.name + " ");
        }
        sb.deleteCharAt(sb.length() - 1);
        service.setText(sb.toString());
        downOrderTime.setText(o.getOrderTime().getDate());
        if (curUserIsVip(user)) {
            ViewUtils.showLayout(iconVip);
        } else {
            ViewUtils.hideLayout(iconVip);
        }

    }

    /**
     * 当前用户是否是vip身份
     *
     * @return
     */
    private boolean curUserIsVip(User user) {
        return AppUtils.curUserIsVip(user, merchantVipsList);
    }
}
