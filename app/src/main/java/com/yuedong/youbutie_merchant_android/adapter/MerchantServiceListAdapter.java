package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

/**
 * 商家服务列表
 */
public class MerchantServiceListAdapter extends BaseAdapter<ServiceInfoDetailBean> {
    private OnButtonSwitchListener onButtonSwitchListener;
    private boolean oneselfChangeXicheStatus = false;

    public void setOnButtonSwitchListener(OnButtonSwitchListener onButtonSwitchListener) {
        this.onButtonSwitchListener = onButtonSwitchListener;
    }

    public MerchantServiceListAdapter(Context con) {
        super(con, R.layout.item_merchant_service_list);
    }

    @Override
    public void convert(ViewHolder viewHolder, ServiceInfoDetailBean serviceInfoDetailBean, int position, View convertView) {
        ImageView imageView = viewHolder.getIdByView(R.id.id_service_pic);
        TextView name = viewHolder.getIdByView(R.id.id_service_name);
        final TextView timeDesc = viewHolder.getIdByView(R.id.id_service_time_desc);
        final ImageView toggleButton = viewHolder.getIdByView(R.id.id_togglebtn);
        CardView cardView = viewHolder.getIdByView(R.id.id_carview_pic);

        //----------------------------赋值------------------------
        String serviceName = serviceInfoDetailBean.name;
        Integer[] serviceInfoDisplayStyle = AppUtils.getServiceInfoDisplayStyle(serviceName);
        cardView.setCardBackgroundColor(serviceInfoDisplayStyle[0]);
        imageView.setImageResource(serviceInfoDisplayStyle[1]);
        name.setText(serviceName);
        if (serviceName.equals(mCon.getString(R.string.str_car_wash))) {
            ViewUtils.showLayouts(toggleButton, timeDesc);
            Integer state = serviceInfoDetailBean.state;
            if (state == 0) {
                toggleButton.setImageResource(R.drawable.button_off);
                timeDesc.setText(mCon.getString(R.string.str_idle_time));
            } else {
                toggleButton.setImageResource(R.drawable.button_on);
                timeDesc.setText(mCon.getString(R.string.str_busy_time));
            }
        } else {
            ViewUtils.hideLayouts(toggleButton, timeDesc);
        }

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state = 0;
                String text = timeDesc.getText().toString();
                if (text.equals(mCon.getString(R.string.str_idle_time))) {
                    toggleButton.setImageResource(R.drawable.button_on);
                    timeDesc.setText(mCon.getString(R.string.str_busy_time));
                    state = 1;
                } else {
                    toggleButton.setImageResource(R.drawable.button_off);
                    timeDesc.setText(mCon.getString(R.string.str_idle_time));
                }
                if (onButtonSwitchListener != null)
                    onButtonSwitchListener.bottonSwitch(state);
            }
        });

//
    }

    public interface OnButtonSwitchListener {
        void bottonSwitch(int state);
    }
}
