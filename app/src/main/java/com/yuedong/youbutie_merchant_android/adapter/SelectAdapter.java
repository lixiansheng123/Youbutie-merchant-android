package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.ServiceInfo;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/11/30.
 */
public class SelectAdapter extends BaseAdapter<BmobObject> {
    private static String TAG = "SelectAdapter";
    public static final int MODE_SERVICE = 0x002;
    public static final int MODE_CAR = 0x003;
    private int mode = MODE_SERVICE;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public SelectAdapter(Context con) {
        super(con, R.layout.item_select_item);
    }

    @Override
    public void convert(ViewHolder viewHolder, BmobObject bmobObject, int position, View convertView) {
        TextView text = viewHolder.getIdByView(R.id.id_text);
        switch (mode) {
            case MODE_SERVICE:
                ServiceInfo serviceInfo = (ServiceInfo) bmobObject;
                text.setText(serviceInfo.getName());
                break;

            case MODE_CAR:
                Car car = (Car) bmobObject;
                text.setText(car.getName());
                break;

        }
    }

}
