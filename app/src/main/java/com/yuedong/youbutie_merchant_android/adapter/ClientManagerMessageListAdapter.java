package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.SystemUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

public class ClientManagerMessageListAdapter extends BaseAdapter<Messages> {
    public ClientManagerMessageListAdapter(Context con, List<Messages> data) {
        super(con, data, R.layout.item_client_manager_message_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, Messages messages, int position, View convertView) {
        TextView title = viewHolder.getIdByView(R.id.id_message_name);
        TextView content = viewHolder.getIdByView(R.id.id_message_content);
        TextView time = viewHolder.getIdByView(R.id.id_message_time);
        ImageView mask = viewHolder.getIdByView(R.id.id_icon_message_overdue);
        TextView validTime = viewHolder.getIdByView(R.id.id_validity_time);
        title.setText(messages.getTitle());
        content.setText(messages.getContent());
        time.setText(messages.getCreatedAt());
        String startTime = messages.getStartTime().getDate();
        String endTime = messages.getEndTime().getDate();
        long startTimeL = BmobDate.getTimeStamp(startTime);
        long endTimeL = BmobDate.getTimeStamp(endTime);
        startTime = DateUtils.formatDate(new Date(startTimeL), "yyyy.MM.dd");
        endTime = DateUtils.formatDate(new Date(endTimeL), "yyyy.MM.dd");
        validTime.setText("有效期:" + startTime + " 至 " + endTime);
        if (endTimeL < System.currentTimeMillis()) {
            // 过期
            ViewUtils.showLayout(mask);
            title.setTextColor(Color.parseColor("#e0dedb"));
            content.setTextColor(Color.parseColor("#e0dedb"));
            time.setTextColor(Color.parseColor("#e0dedb"));
            validTime.setTextColor(Color.parseColor("#e0dedb"));
        } else {
            ViewUtils.hideLayout(mask);
            title.setTextColor(Color.parseColor("#eeb600"));
            content.setTextColor(Color.parseColor("#938381"));
            time.setTextColor(Color.parseColor("#cbc0bf"));
            validTime.setTextColor(Color.parseColor("#cbc0bf"));
        }


    }
}
