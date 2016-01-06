package com.yuedong.youbutie_merchant_android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.WheelOnlyTextAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.mouble.Callback;
import com.yuedong.youbutie_merchant_android.utils.DataUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;

public class TimeSelectPop extends Dialog {
    private WheelView hourWv, minuteWv;
    private TextView selectTimeDesc;
    private Context mContext;
    private String curHour;
    private String curMinute;
    private View mContentView;
    private boolean selectFinished = true;
    private List<String> hoursData;
    private List<String> minuteData;
    private WheelOnlyTextAdapter adapter2;
    private WheelOnlyTextAdapter adapter3;
    private Callback callback;

    public void setOnCallbcak(Callback callbcak) {
        this.callback = callbcak;
    }

    public TimeSelectPop(Context context) {
        super(context, R.style.style_my_dialog);
        View contentView = ViewUtils.inflaterView(context, R.layout.dialog_select_time);
        this.mContext = context;
        this.mContentView = contentView;
        setContentView(contentView);
        initViews();
        initEvents();
    }

    private void initEvents() {
        hourWv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                selectFinished = false;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                curHour = hoursData.get(wheel.getCurrentItem());
                selectFinished = true;
            }
        });
        minuteWv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                selectFinished = false;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                selectFinished = true;
                curMinute = minuteData.get(wheel.getCurrentItem());
            }
        });

        mContentView.findViewById(R.id.view_gap).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContentView.findViewById(R.id.id_select_cancle).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mContentView.findViewById(R.id.id_select_confirm).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!selectFinished)
                    return;
                dismiss();
                if (callback != null)
                    callback.callbackHM(curHour, curMinute);

            }
        });
    }

    private void initViews() {
        selectTimeDesc = (TextView) mContentView.findViewById(R.id.id_select_time_desc);
        hourWv = (WheelView) mContentView.findViewById(R.id.id_wheelview_month);
        minuteWv = (WheelView) mContentView.findViewById(R.id.id_wheelview_day);
        setWheelViewParams(hourWv);
        setWheelViewParams(minuteWv);
        adapter2 = new WheelOnlyTextAdapter(mContext);
        adapter3 = new WheelOnlyTextAdapter(mContext);
        hourWv.setViewAdapter(adapter2);
        minuteWv.setViewAdapter(adapter3);
        hoursData = DataUtils.getHours();
        minuteData = DataUtils.getMinutes();
        adapter2.setData(hoursData);
        adapter3.setData(minuteData);
        hourWv.setCurrentItem(0);
        minuteWv.setCurrentItem(0);
        curHour = hoursData.get(hourWv.getCurrentItem());
        curMinute = minuteData.get(minuteWv.getCurrentItem());
    }

    @Override
    public void show() {
        if (isShowing()) return;
        super.show();
        Integer[] phoneWh = App.getInstance().getPhoneWh();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (phoneWh[0]); // 设置宽度
        this.getWindow().setAttributes(lp);
    }

    private void setWheelViewParams(WheelView wheelView) {
        wheelView.setShadowColor(0xB2ffffff, 0xB2ffffff, 0xB2ffffff);
        wheelView.setWheelBackground(R.color.transparent);
        wheelView.setWheelForeground(R.drawable.bg_wheel_indicate);
    }

    public void setSelectTimeDesc(CharSequence desc) {
        selectTimeDesc.setText(desc);
    }
}
