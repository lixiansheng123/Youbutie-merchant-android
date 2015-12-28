package com.yuedong.youbutie_merchant_android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

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

public class DaySelectPop extends Dialog {
    private WheelView yearWv, monthWv, dayWv;
    private Context mContext;
    private String curYear;
    private String curMonth;
    private View mContentView;
    private boolean selectFinished = true;
    private List<String> daysData;
    private List<String> yearsData;
    private List<String> monthData;
    private WheelOnlyTextAdapter adapter;
    private WheelOnlyTextAdapter adapter2;
    private WheelOnlyTextAdapter adapter3;
    private Callback callback;

    public void setOnCallbcak(Callback callbcak) {
        this.callback = callbcak;
    }

    public DaySelectPop(Context context) {
        super(context, R.style.style_my_dialog);
        View contentView = ViewUtils.inflaterView(context, R.layout.dialog_day_select);
        this.mContext = context;
        this.mContentView = contentView;
        setContentView(contentView);
        initViews();
        initEvents();
    }

    private void initEvents() {
        yearWv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                curYear = yearsData.get(wheel.getCurrentItem());
            }
        });
        monthWv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                selectFinished = false;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                curMonth = monthData.get(wheel.getCurrentItem());
                daysData = DataUtils.getDays(Integer.parseInt(curYear), Integer.parseInt(curMonth));
                adapter3.setData(daysData);
                selectFinished = true;
            }
        });
        dayWv.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

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
                // 获取wheelView3当前选中的值
                String day = daysData.get(dayWv.getCurrentItem());
                if (callback != null)
                    callback.callbackYMR(curYear, curMonth, day);
            }
        });
    }

    private void initViews() {
        yearWv = (WheelView) mContentView.findViewById(R.id.id_wheelview_year);
        monthWv = (WheelView) mContentView.findViewById(R.id.id_wheelview_month);
        dayWv = (WheelView) mContentView.findViewById(R.id.id_wheelview_day);
        setWheelViewParams(yearWv);
        setWheelViewParams(monthWv);
        setWheelViewParams(dayWv);

        adapter = new WheelOnlyTextAdapter(mContext);
        adapter2 = new WheelOnlyTextAdapter(mContext);
        adapter3 = new WheelOnlyTextAdapter(mContext);
        yearWv.setViewAdapter(adapter);
        monthWv.setViewAdapter(adapter2);
        dayWv.setViewAdapter(adapter3);
        yearsData = DataUtils.getYears(Calendar.getInstance().get(Calendar.YEAR));
        monthData = DataUtils.getMonth();
        // 当前年
        curYear = yearsData.get(yearWv.getCurrentItem());
        // 当前月
        curMonth = monthData.get(monthWv.getCurrentItem());
        daysData = DataUtils.getDays(Integer.parseInt(curYear), Integer.parseInt(curMonth));
        int centerYears = (int) Math.round(yearsData.size() * 1.0 / 2);
        int centerMonth = (int) Math.round(monthData.size() * 1.0 / 2);
        int centerDay = (int) Math.round(daysData.size() * 1.0 / 2);
        adapter.setData(yearsData);
        adapter2.setData(monthData);
        adapter3.setData(daysData);
        yearWv.setCurrentItem(centerYears);
        monthWv.setCurrentItem(centerMonth);
        dayWv.setCurrentItem(centerDay);
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
}
