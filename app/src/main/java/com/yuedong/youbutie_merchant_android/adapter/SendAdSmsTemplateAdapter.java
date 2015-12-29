package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

public class SendAdSmsTemplateAdapter extends BaseAdapter<ServiceInfoDetailBean> {
    private int selectPosition = -1;
    private AdapterView.OnItemClickListener itemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SendAdSmsTemplateAdapter(Context con, List<ServiceInfoDetailBean> data) {
        super(con, data, R.layout.item_collection_merchant_service);
    }

    @Override
    public void convert(ViewHolder viewHolder, ServiceInfoDetailBean serviceInfoDetailBean, final int position, View convertView) {
        TextView name = viewHolder.getIdByView(R.id.id_name);
        name.setText(serviceInfoDetailBean.name);
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(null, v, position, -1);
                selectPosition = position;
                notifyDataSetChanged();
            }
        });

        if (selectPosition != -1) {
            if (selectPosition == position) {
                name.setBackgroundResource(R.drawable.bg_merchant_service_click);
                name.setTextColor(Color.parseColor("#ffffff"));
            } else {
                name.setBackgroundResource(R.drawable.bg_merchant_service);
                name.setTextColor(Color.parseColor("#938381"));
            }
        }
    }
}
