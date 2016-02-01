package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.adapter.AlreadySelectCarAdapter;
import com.yuedong.youbutie_merchant_android.adapter.SendAdSmsTemplateAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.Callback;
import com.yuedong.youbutie_merchant_android.model.MessageEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.VipEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.model.listener.ObtainSecretKeyListener;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DataUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.DaySelectPop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发广告
 */
public class SendMessageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SendMessageActivity";
    private DaySelectPop startDaySelectPop;
    private DaySelectPop endDaySelectPop;
    private TextView startTime, endTime, adTitle;
    private GridView gvSelectCar, gvService;
    private AlreadySelectCarAdapter alreadySelectCarAdapter;
    private ArrayList<Car> selectCars = new ArrayList<Car>();
    private SendAdSmsTemplateAdapter adapter;
    private List<ServiceInfoDetailBean> smsTemplateDatas;
    private EditText inputBox;
    // 广告标题标题颜色是否已经变化
    private boolean adTitleTextColorChange = false;
    private String startYear, startMonth, startDay, endYear, endMonth, endDay;
    private Merchant myMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMerchant = (Merchant) getIntent().getSerializableExtra(Constants.KEY_BEAN);
        buildUi(new TitleViewHelper().createDefaultTitleView3("发广告"), false, false, false, R.layout.activity_send_message);
    }

    @Override
    protected void initViews() {
        adTitle = fvById(R.id.id_ad_title);
        inputBox = fvById(R.id.id_input_box);
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
        findViewById(R.id.id_confirm_send).setOnClickListener(this);
        findViewById(R.id.id_ad_title_layout).setOnClickListener(this);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inputBox.setText(String.format(smsTemplateDatas.get(position).icon, myMerchant.getName()));
            }
        });
        startDaySelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackYMR(String year, String month, String day) {
                startYear = year;
                startMonth = month;
                startDay = day;
                startTime.setTextColor(Color.parseColor("#81726f"));
                startTime.setText(year + "." + month + "." + day + "  ");
            }
        });
        endDaySelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackYMR(String year, String month, String day) {
                endYear = year;
                endMonth = month;
                endDay = day;
                String startTimeStr = startTime.getText().toString();
                String[] times = startTimeStr.split("\\.");
                if (times.length != 3) return;
                String startYear = times[0];
                String startMonth = times[1];
                String startDay = times[2];
                String startFull = startYear + "-" + startMonth + "-" + startDay + " 00:00:00";
                String endFull = year + "-" + month + "-" + day + " 00:00:00";
                long startL = DateUtils.strTimeToLongTime(startFull);
                long endL = DateUtils.strTimeToLongTime(endFull);
                if (DateUtils.isBefore(startFull, endFull) && startL != endL) {
                    endTime.setTextColor(Color.parseColor("#81726f"));
                    endTime.setText(year + "." + month + "." + day + "  ");
                } else {
                    T.showShort(context, "结束时间必须要大于开始时间");
                    endYear = null;
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
            case R.id.id_confirm_send:
                sendNewAd();
                break;

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
                Intent it = new Intent(activity, SelectCarActivity.class);
                it.putExtra(Constants.KEY_LIST, selectCars);
                LaunchWithExitUtils.startActivityForResult(activity, it, Constants.REQUESTCODE_SELECT_CAR);

                break;

            case R.id.id_ad_title_layout:
                Intent it2 = new Intent(activity, InfoEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_TEXT, "填写广告标题");
                bundle.putInt(Constants.KEY_ACTION, InfoEditActivity.ACTION_INPUT_AD_TITLE);
                it2.putExtras(bundle);
                LaunchWithExitUtils.startActivityForResult(activity, it2, Constants.REQUESTCODE_INPUT_AD_TITLE);
                break;

        }
    }

    /**
     * 发布新的广告
     */
    private void sendNewAd() {
        if (checkoutParams()) {
            dialogStatus(true);
            if (CommonUtils.listIsNotNull(selectCars)) {
                VipEvent.getInstance().findMerchantVipByCar(myMerchant.getObjectId(), selectCars, new FindListener<Vips>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(List<Vips> list) {
                        sendMessageByVips(list);
                    }

                    @Override
                    public void onError(int i, String s) {
                        error(i);
                        dialogStatus(false);
                    }
                });
            } else {
                // 全部车型的话就是推送給他门店的所有vip
                VipEvent.getInstance().findVipByMerchant(myMerchant.getObjectId(), new FindListener<Vips>() {
                    @Override
                    public void onSuccess(List<Vips> list) {
                        sendMessageByVips(list);
                    }

                    @Override
                    public void onError(int i, String s) {
                        error(i);
                        dialogStatus(false);
                    }
                });
            }
        }
    }

    private void sendMessageByVips(List<Vips> list) {
        if (CommonUtils.listIsNotNull(list)) {
            List<String> userObjects = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            for (Vips vips : list) {
                userObjects.add(vips.getUser().getObjectId());
                User user = vips.getUser();
                if (user != null) {
                    sb.append(user.getObjectId() + ",");
                }
            }
            if (sb.length() > 0)
                sb.deleteCharAt(sb.length() - 1);
            L.d("推送的objectids:" + sb.toString());
            Messages messages = new Messages();
            messages.setType(2);
            messages.setContent(inputBox.getText().toString().trim());
            messages.setTitle(adTitle.getText().toString().trim());
            messages.setStartTime(new BmobDate(new Date(DateUtils.strTimeToLongTime(startYear + "-" + startMonth + "-" + startDay + " 00:00:00"))));
            messages.setEndTime(new BmobDate(new Date(DateUtils.strTimeToLongTime(endYear + "-" + endMonth + "-" + endDay + " 00:00:00"))));
            messages.setSender(App.getInstance().getUser());
            messages.setTargets(userObjects);
            messages.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    MessageEvent.getInstance().countCurMonthSendMessageNumber(new CountListener() {
                        @Override
                        public void onSuccess(int i) {
                            dialogStatus(false);
                            L.d("countCurMonthSendMessageNumber-succeed:" + i);
                            if (i < 2) {
                                App.getInstance().getYdApiSecretKey(new ObtainSecretKeyListener() {
                                    @Override
                                    public void start() {

                                    }

                                    @Override
                                    public void end() {

                                    }

                                    @Override
                                    public void succeed(String secretKey) {
                                        RequestYDHelper requestYDHelper = new RequestYDHelper();
                                        requestYDHelper.setAppSecretkey(secretKey);
                                        requestYDHelper.requestPushGroup(getString(R.string.str_push_merchant_ad_title),//
                                                String.format(getString(R.string.str_push_merchant_ad_content), myMerchant.getName()), //
                                                sb.toString() /*"9b09e9af35,009a84e9fb"*/, RequestYDHelper.PUSH_TYPE_MERCHANT_AD, "", "");
                                    }

                                    @Override
                                    public void fail(int code, String error) {

                                    }
                                });
                            } else {
                                T.showShort(context, "发出的广告不会提醒用户，当月只能提醒用户两次");
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            error(i);
                        }
                    });
                    T.showShort(context, "发布成功");
                    setResult(Constants.RESULT_ADD_AD);
                    defaultFinished();
                }

                @Override
                public void onFailure(int i, String s) {
                    error(i);
                    dialogStatus(false);
                }
            });
        } else {
            T.showLong(context, "未能找到本店会员");
            dialogStatus(false);
        }
    }

    private boolean checkoutParams() {
        boolean legal = true;
        String inputStr = inputBox.getText().toString();
        inputStr = inputStr.trim();
        if (!adTitleTextColorChange) {
            legal = false;
            T.showShort(context, "请输入标题!");
            return legal;
        }

        if (startYear == null || endYear == null) {
            legal = false;
            T.showShort(context, "请录入时间!");
            return legal;
        }

        if (StringUtil.isEmpty(inputBox.getText().toString())) {
            legal = false;
            T.showShort(context, "请输入广告内容!");
            return legal;
        }

        return legal;

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

        } else if (requestCode == Constants.REQUESTCODE_INPUT_AD_TITLE && resultCode == Constants.RESULT_INPUT_AD_TITLE && data != null) {
            String inputText = data.getStringExtra(Constants.KEY_TEXT);
            adTitle.setText(inputText + "    ");
            if (!adTitleTextColorChange) {
                adTitle.setTextColor(Color.parseColor("#938381"));
                adTitleTextColorChange = true;
            }
        }

    }
}
