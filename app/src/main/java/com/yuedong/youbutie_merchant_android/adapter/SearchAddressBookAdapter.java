package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.PhoneAddressBookBean;
import com.yuedong.youbutie_merchant_android.bean.SearchAddressBookBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.SystemUtils;
import com.yuedong.youbutie_merchant_android.utils.TextUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/1.
 */
public class SearchAddressBookAdapter extends BaseAdapter<SearchAddressBookBean> {
    public SearchAddressBookAdapter(Context con) {
        super(con, R.layout.item_invite_member_child);
    }

    @Override
    public void convert(ViewHolder viewHolder, final SearchAddressBookBean phoneAddressBookBean, int position, View convertView) {
        RoundImageView userHead = viewHolder.getIdByView(R.id.id_user_pic);
        TextView name = viewHolder.getIdByView(R.id.id_user_name);
        TextView registTime = viewHolder.getIdByView(R.id.id_time);
        TextView userHeadText = viewHolder.getIdByView(R.id.id_user_pic_text);
        Button btnInvite = viewHolder.getIdByView(R.id.id_btn_invite);
        ViewUtils.hideLayout(registTime);
        String contactName = phoneAddressBookBean.getContactName();
        userHead.setBackgroundResource(phoneAddressBookBean.getBg());
        name.setText(contactName);
        if (StringUtil.isNotEmpty(contactName)) {
            userHeadText.setText(contactName.substring(0, 1));
        }
        final String unRegistInviteNum = (String) SPUtils.get(mCon, Constants.SP_INVITE_REGIST, "");
        L.d("验证过的号码" + unRegistInviteNum);
        String[] split = unRegistInviteNum.split(",");
        boolean has = false;
        for (String inviteNum : split) {
            if (StringUtil.isNotEmpty(inviteNum) && inviteNum.equals(phoneAddressBookBean.getPhoneNumber())) {
                has = true;
            }
        }
        if (has) {
            btnInvite.setBackgroundResource(R.drawable.bg_round_grey);
            btnInvite.setText(mCon.getString(R.string.str_already_invite));
            btnInvite.setOnClickListener(null);
        } else {
            btnInvite.setBackgroundResource(R.drawable.bg_round_yellow);
            btnInvite.setText(mCon.getString(R.string.str_invite));
            btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 发送机短信 内容模版暂定
                    SystemUtils.sms(mCon, phoneAddressBookBean.getPhoneNumber(), String.format(mCon.getString(R.string.str_sms_moudle), App.getInstance().getUser().getObjectId()));
                    SPUtils.put(mCon, Constants.SP_INVITE_REGIST, unRegistInviteNum + phoneAddressBookBean.getPhoneNumber() + ",");
                    notifyDataSetChanged();
                }
            });
        }


    }
}
