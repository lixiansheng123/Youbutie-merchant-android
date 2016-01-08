package com.yuedong.youbutie_merchant_android;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.IncomeDetailListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.IncomeDetailListBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

public class IncomeDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "IncomeDetailActivity";
    private PullToRefreshListView pullToRefreshListView;
    private IncomeDetailListAdapter adapter;
    private List<IncomeDetailListBean> datas = new ArrayList<IncomeDetailListBean>();
    private RefreshHelper<IncomeDetailListBean> refreshHelper = new RefreshHelper<IncomeDetailListBean>();
    private Merchant merchant;
    private int totalMoney;
    private TextView curMonthTotalMoney, canWithdrawMoney, alreadyWithdrawNum, alreadyWithdrawMoney;
    private Button requestWithdrawBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        merchant = (Merchant) extras.getSerializable(Constants.KEY_BEAN);
        totalMoney = extras.getInt(Constants.KEY_INT, 0);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("收入详情"));
        setShowContentView(R.layout.activity_income_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.getInstance().userInfoChange) {
            ui();
            App.getInstance().userInfoChange = false;
        }
    }

    @Override
    protected void initViews() {
        pullToRefreshListView = fvById(R.id.id_refresh_view);
        ListView listView = pullToRefreshListView.getRefreshableView();
        View headView = ViewUtils.inflaterView(context, R.layout.head_income_detail_list, listView);
        curMonthTotalMoney = (TextView) headView.findViewById(R.id.id_cur_month_sales);
        alreadyWithdrawMoney = (TextView) headView.findViewById(R.id.id_already_withdraw_money);
        alreadyWithdrawNum = (TextView) headView.findViewById(R.id.id_already_withdraw_num);
        canWithdrawMoney = (TextView) headView.findViewById(R.id.id_can_withdraw_money);
        requestWithdrawBtn = (Button) headView.findViewById(R.id.id_btn_request_withdraw);
        listView.addHeaderView(headView, null, false);
        refreshHelper.setPulltoRefreshRefreshProxy(this, pullToRefreshListView, new RefreshHelper.ProxyRefreshListener<IncomeDetailListBean>() {
            @Override
            public BaseAdapter<IncomeDetailListBean> getAdapter(List<IncomeDetailListBean> data) {
                return adapter = new IncomeDetailListAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, final FindListener<IncomeDetailListBean> listener) {
                OrderEvent.getInstance().getMonthOrder(skip, limit, merchant.getObjectId(), new FindStatisticsListener() {
                    @Override
                    public void onSuccess(Object o) {
                        L.d(TAG, o.toString());
                        try {
                            JSONArray jsonArray = (JSONArray) o;
                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                String[] times = new String[len];
                                List<IncomeDetailListBean> datas = new ArrayList<IncomeDetailListBean>();
                                for (int i = 0; i < len; i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    int totalPrice = jsonObject.getInt("_sumPrice");
                                    String dayDes = jsonObject.getString("sumMonth");
                                    int count = jsonObject.getInt("_count");
                                    IncomeDetailListBean bean = new IncomeDetailListBean();
                                    bean.setDayDes(dayDes);
                                    bean.setTotalMoney(totalPrice);
                                    bean.setOrderNumber(count);
                                    datas.add(bean);
                                    times[i] = dayDes;
                                }
                                sort(times, datas);
                                listener.onSuccess(datas);
                                listener.onFinish();
                            } else {
                                listener.onError(-2, "查询成功但无数据");
                                listener.onFinish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            listener.onError(-1, e.getMessage());
                            listener.onFinish();
                        }

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        listener.onError(i, s);
                        listener.onFinish();
                    }
                });
            }
        });

    }

    private void sort(String[] times, List<IncomeDetailListBean> datas) {
        Arrays.sort(times);
        LinkedList<IncomeDetailListBean> list = new LinkedList<IncomeDetailListBean>();
        for (String time : times) {
            IncomeDetailListBean bean = getBeanBySet(time, datas);
            if (bean != null)
                list.addFirst(bean);
        }
        if (CommonUtils.listIsNotNull(list)) {
            datas.clear();
            for (IncomeDetailListBean bean : list) {
                datas.add(bean);
            }
        }
    }

    private IncomeDetailListBean getBeanBySet(String timeDes, List<IncomeDetailListBean> datas) {
        for (IncomeDetailListBean bean : datas) {
            if (timeDes.equals(bean.getDayDes()))
                return bean;
        }
        return null;
    }

    @Override
    protected void initEvents() {
        requestWithdrawBtn.setOnClickListener(this);
    }

    @Override
    protected void ui() {
        User user = App.getInstance().getUser();
        L.d("User:" + user.toString());
        curMonthTotalMoney.setText("￥" + totalMoney);
        if (user.getCash() != null)
            canWithdrawMoney.setText("￥" + user.getCash());
        if (user.getDrawCount() != null)
            alreadyWithdrawNum.setText(user.getDrawCount() + "次");
        if (user.getDrawTotalCash() != null)
            alreadyWithdrawMoney.setText("￥" + user.getDrawTotalCash());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_btn_request_withdraw:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_BEAN, merchant);
                LaunchWithExitUtils.startActivity(activity, ApplyWithdrawActivity.class, bundle);
                break;

        }
    }
}
