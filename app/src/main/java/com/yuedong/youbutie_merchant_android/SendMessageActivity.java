package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.adapter.AlreadySelectCarAdapter;
import com.yuedong.youbutie_merchant_android.adapter.CollectionMerchantServiceAdapter;
import com.yuedong.youbutie_merchant_android.adapter.SendAdSmsTemplateAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.Callback;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DataUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.DaySelectPop;

import java.util.ArrayList;
import java.util.List;

/**
 * 发广告
 */
public class SendMessageActivity extends BaseActivity implements View.OnClickListener {
    private DaySelectPop startDaySelectPop;
    private DaySelectPop endDaySelectPop;
    private TextView startTime, endTime;
    private GridView gvSelectCar, gvService;
    private AlreadySelectCarAdapter alreadySelectCarAdapter;
    private ArrayList<Car> selectCars;
    private SendAdSmsTemplateAdapter adapter;
    private List<ServiceInfoDetailBean> smsTemplateDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("发广告"));
        setShowContentView(R.layout.activity_send_message);
    }

    @Override
    protected void initViews() {
        gvService = fvById(R.id.id_gv_service);
        gvSelectCar = fvById(R.id.id_gv_already_select_car);
        startDaySelectPop = new DaySelectPop(context);
        endDaySelectPop = new DaySelectPop(context);
        startTime = fvById(R.id.id_select_start_time);
        endTime = fvById(R.id.id_select_end_time);
        smsTemplateDatas = DataUtils.getSmsSelectItem();
        adapter = new SendAdSmsTemplateAdapter(context, smsTemplateDatas);
        gvService.setAdapter(adapter);
    }

    @Override
    protected void initEvents() {
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        startDaySelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackYMR(String year, String month, String day) {
                startTime.setTextColor(Color.parseColor("#81726f"));
                startTime.setText(year + "." + month + "." + day + "  ");
            }
        });
        endDaySelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackYMR(String year, String month, String day) {
                String startTimeStr = startTime.getText().toString();
                String[] times = startTimeStr.split("\\.");
                L.d("startTimeStr" + startTimeStr + "--length:" + times.length);
                if (times.length != 3) return;
                String startYear = times[0];
                String startMonth = times[1];
                String startDay = times[2];
                String startFull = startYear + "-" + startMonth + "-" + startDay + " 00:00:00";
                String endFull = year + "-" + month + "-" + day + " 00:00:00";
                L.d("startTimeFull" + startFull + "---endTimeFull:" + endFull);
                if (DateUtils.isBefore(startFull, endFull)) {
                    endTime.setTextColor(Color.parseColor("#81726f"));
                    endTime.setText(year + "." + month + "." + day + "  ");
                } else {
                    T.showShort(context, "结束时间必须要大于开始时间");
                }
            }
        });
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        fvById(R.id.id_select_car_layout).setOnClickListener(this);
    }

    @Override
    protected void ui() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_select_start_time:
                startDaySelectPop.show();
                break;
            case R.id.id_select_end_time:
                String startTimeStr = startTime.getText().toString();
                if (startTimeStr.indexOf(".") != -1)
                    endDaySelectPop.show();
                else
                    T.showShort(context, "请先选择开始时间");
                break;

            case R.id.id_select_car_layout:
                LaunchWithExitUtils.startActivityForResult(activity, SelectCarActivity.class, Constants.REQUESTCODE_SELECT_CAR);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_SELECT_CAR && resultCode == Constants.RESULT_SELECT_CAR && data != null) {
            selectCars = (ArrayList<Car>) data.getSerializableExtra(Constants.KEY_LIST);
            if (CommonUtils.listIsNotNull(selectCars)) {
                ViewUtils.hideLayout(fvById(R.id.id_select_car_default));
                ViewUtils.showLayout(gvSelectCar);
                alreadySelectCarAdapter = new AlreadySelectCarAdapter(context, selectCars);
                alreadySelectCarAdapter.setAction(AlreadySelectCarAdapter.ACTION_SHOW);
                gvSelectCar.setAdapter(alreadySelectCarAdapter);
            }

        }

    }
}
