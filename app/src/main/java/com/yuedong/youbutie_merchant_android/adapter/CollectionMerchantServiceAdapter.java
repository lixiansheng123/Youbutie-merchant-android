package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CollectionMerchantServiceAdapter extends BaseAdapter<ServiceInfoDetailBean> {
    private List<ServiceInfoDetailBean> selectService = new ArrayList<ServiceInfoDetailBean>();
    private View.OnClickListener itemClickListener;

    public void setItemClickListener(View.OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<ServiceInfoDetailBean> getSelect() {
        return selectService;
    }

    private void addSelect(ServiceInfoDetailBean position) {
        if (selectService.contains(position))
            selectService.remove(position);
        else
            selectService.add(position);
    }

    public CollectionMerchantServiceAdapter(Context con, List<ServiceInfoDetailBean> data) {
        super(con, data, R.layout.item_collection_merchant_service);
    }

    @Override
    public void convert(ViewHolder viewHolder, final ServiceInfoDetailBean serviceInfoDetailBean, final int position, View convertView) {
        TextView name = viewHolder.getIdByView(R.id.id_name);
        name.setText(serviceInfoDetailBean.name);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelect(serviceInfoDetailBean);
                notifyDataSetChanged();
                if (itemClickListener != null)
                    itemClickListener.onClick(v);
            }
        });

        if (selectService.contains(serviceInfoDetailBean)) {
            name.setBackgroundResource(R.drawable.bg_merchant_service_click);
            name.setTextColor(Color.parseColor("#ffffff"));
        } else {
            name.setBackgroundResource(R.drawable.bg_merchant_service);
            name.setTextColor(Color.parseColor("#938381"));
        }
    }
}
