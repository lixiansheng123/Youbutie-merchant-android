package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;

import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

public class UserListAdapter extends BaseAdapter<Object>{
    public UserListAdapter(Context con, List<Object> data, int layouid) {
        super(con, data, layouid);
    }

    @Override
    public void convert(ViewHolder viewHolder, Object o, int position, View convertView) {

    }
}
