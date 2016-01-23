package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.CountConsumeBean;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

public class CountConsumeAdapter extends BaseAdapter<CountConsumeBean> {
    private static final int PROGRESS_MIN_WIDTH = 50;
    private int progressLayoutMaxWidth;
    // 已经获取到了进度最大的宽度
    private boolean obtainProgressLayoutMaxWitch = false;

    public CountConsumeAdapter(Context context) {
        super(context, R.layout.item_count_consume);
    }

    @Override
    public void convert(ViewHolder viewHolder, final CountConsumeBean countConsumeBean, int position, View convertView) {
        ServiceInfoDetailBean serviceInfoDetailBean = countConsumeBean.getServiceInfoDetailBean();
        CardView cardView = viewHolder.getIdByView(R.id.id_carview_pic);
        ImageView iconPic = viewHolder.getIdByView(R.id.id_icon_pic);
        TextView serviceDesc = viewHolder.getIdByView(R.id.id_service_desc);
        TextView serviceRatio = viewHolder.getIdByView(R.id.id_ratio);
        final RelativeLayout progressLayout = viewHolder.getIdByView(R.id.id_progress_layout);
        TextView orderNum = viewHolder.getIdByView(R.id.id_order_num);

        //-------------------------------------------------------------
        String serviceName = serviceInfoDetailBean.name;
        Integer[] serviceInfoDisplayStyle = AppUtils.getServiceInfoDisplayStyle(serviceName);
        cardView.setCardBackgroundColor(serviceInfoDisplayStyle[0]);
        iconPic.setImageResource(serviceInfoDisplayStyle[1]);
        if (serviceName.length() > 2)
            serviceName = serviceName.substring(0, 2);
        else if (serviceName.length() <= 1)
            serviceName = serviceName + " ";
        String typeRatio = countConsumeBean.getTypeRatio() + "%";
        serviceDesc.setText(serviceName + "服务");
        serviceRatio.setText(typeRatio);
        orderNum.setText(countConsumeBean.getTypeNumber() + "");
        progressLayout.post(new Runnable() {
            @Override
            public void run() {
                if (!obtainProgressLayoutMaxWitch) {
                    // 进度最大宽度
                    progressLayoutMaxWidth = progressLayout.getWidth();
                    obtainProgressLayoutMaxWitch = true;
                }
                // 增量
                int increment = (int) ((progressLayoutMaxWidth - PROGRESS_MIN_WIDTH) * 1.0f / 100 + 0.5f);
                int shouldWidth = countConsumeBean.getTypeRatio() * increment + PROGRESS_MIN_WIDTH;
                if (shouldWidth < PROGRESS_MIN_WIDTH)
                    shouldWidth = PROGRESS_MIN_WIDTH;
                else if (shouldWidth > progressLayoutMaxWidth)
                    shouldWidth = progressLayoutMaxWidth;
                progressLayout.getLayoutParams().width = shouldWidth;
                progressLayout.requestLayout();
                ViewUtils.showLayout(progressLayout);
            }
        });
    }

}
