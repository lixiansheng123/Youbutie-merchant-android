package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.DrawMoneyRecord;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class WithdrawRecordListAdapter extends BaseAdapter<DrawMoneyRecord> {
    public WithdrawRecordListAdapter(Context con, List<DrawMoneyRecord> data) {
        super(con, data, R.layout.item_withdraw_record);
    }

    @Override
    public void convert(ViewHolder viewHolder, DrawMoneyRecord drawMoneyRecord, int position, View convertView) {
        TextView time = viewHolder.getIdByView(R.id.id_time);
        TextView withdrawMonet = viewHolder.getIdByView(R.id.id_withdraw_money);
        TextView status = viewHolder.getIdByView(R.id.id_status);
        View line = viewHolder.getIdByView(R.id.id_line);

        if (position == getCount() - 1)
            ViewUtils.hideLayout(line);
        else
            ViewUtils.showLayout(line);

        time.setText(drawMoneyRecord.getCreatedAt());
        withdrawMonet.setText("￥" + drawMoneyRecord.getMoney());
        int state = drawMoneyRecord.getState();
        if (state == 0) {
            status.setText(mCon.getString(R.string.str_progress));
            status.setTextColor(Color.parseColor("#ff8b3e"));
            status.setBackgroundResource(R.drawable.red_frame);
        } else {
            status.setText(mCon.getString(R.string.str_withdraw_succeed));
            status.setTextColor(Color.parseColor("#15d29d"));
            status.setBackgroundResource(R.drawable.green_frame);
        }

    }
}
