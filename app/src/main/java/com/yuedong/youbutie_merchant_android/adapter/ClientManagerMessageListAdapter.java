package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Messages;

import java.util.List;

public class ClientManagerMessageListAdapter extends BaseAdapter<Messages> {
    public ClientManagerMessageListAdapter(Context con, List<Messages> data) {
        super(con, data, R.layout.item_client_manager_message_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, Messages messages, int position, View convertView) {

    }
}
