package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.MoneyContributionBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

public class MoneyContributionAdapter extends BaseAdapter<MoneyContributionBean> {
    public MoneyContributionAdapter(Context con, List<MoneyContributionBean> data) {
        super(con, data, R.layout.item_money_contribution_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, MoneyContributionBean moneyContributionBean, int position, View convertView) {
        NetworkImageView userPic = viewHolder.getIdByView(R.id.id_user_pic);
        TextView rankingText = viewHolder.getIdByView(R.id.id_ranking_text);
        ImageView rankingIcon = viewHolder.getIdByView(R.id.id_ranking_icon);
        TextView userName = viewHolder.getIdByView(R.id.id_user_name);
        TextView totalOli = viewHolder.getIdByView(R.id.id_total_oli);
        User user = moneyContributionBean.getUser();
        DisplayImageByVolleyUtils.loadImage(userPic, user.getPhoto());
        userName.setText(user.getNickname());
        totalOli.setText(moneyContributionBean.getTotalContributionMoney() + "油点");
        position++;
        ViewUtils.showLayout(rankingIcon);
        ViewUtils.hideLayout(rankingText);
        if (position == 1) {
            rankingIcon.setImageResource(R.drawable.icon_number_1);
        } else if (position == 2) {
            rankingIcon.setImageResource(R.drawable.icon_number_2);
        } else if (position == 3) {
            rankingIcon.setImageResource(R.drawable.icon_number_3);
        } else {
            rankingText.setText(position);
            ViewUtils.showLayout(rankingText);
            ViewUtils.hideLayout(rankingIcon);
        }

    }
}
