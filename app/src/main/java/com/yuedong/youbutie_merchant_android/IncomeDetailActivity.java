package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.adapter.IncomeDetailListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.IncomeDetailListBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.UserEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.UpdateListener;

public class IncomeDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "IncomeDetailActivity";
    private PulltoRefreshListView pullToRefreshListView;
    private IncomeDetailListAdapter adapter;
    private List<IncomeDetailListBean> datas = new ArrayList<IncomeDetailListBean>();
    private RefreshProxy<IncomeDetailListBean> refreshHelper = new RefreshProxy<IncomeDetailListBean>();
    private Merchant merchant;
    private int totalMoney;
    private TextView curMonthTotalMoney, canWithdrawMoney, alreadyWithdrawNum, alreadyWithdrawMoney;
    private Button requestWithdrawBtn;
    private View withDrawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        merchant = (Merchant) extras.getSerializable(Constants.KEY_BEAN);
        totalMoney = extras.getInt(Constants.KEY_INT, 0);
        buildUi(new TitleViewHelper().createDefaultTitleView3("收入详情"), false, false, false, R.layout.activity_income_detail);
        refreshHelper.showEmptyView = false;
    }

    private void userInfoUpdate() {
        UserEvent.getInstance().pullUserById(App.getInstance().getUser().getObjectId(), new UpdateListener() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess() {
                App.getInstance().userInfoChange = true;
                User user = App.getInstance().getUser();
                App.getInstance().userInfoChange = false;
                curMonthTotalMoney.setText("￥" + totalMoney);
                if (user.getCash() != null)
                    canWithdrawMoney.setText("￥" + StringUtil.setDoubleValue(user.getCash()));
                if (user.getDrawCount() != null)
                    alreadyWithdrawNum.setText(user.getDrawCount() + "次");
                if (user.getDrawTotalCash() != null)
                    alreadyWithdrawMoney.setText("￥" + StringUtil.setDoubleValue(user.getDrawTotalCash()));
            }

            @Override
            public void onFailure(int i, String s) {
                dialogStatus(false);
                error(i);
            }
        });
    }

    private void listProxy() {
        refreshHelper.setPulltoRefreshRefreshProxy(IncomeDetailActivity.this, pullToRefreshListView, new RefreshProxy.ProxyRefreshListener<IncomeDetailListBean>() {
            @Override
            public BaseAdapter<IncomeDetailListBean> getAdapter(List<IncomeDetailListBean> data) {
                return adapter = new IncomeDetailListAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, final FindListener<IncomeDetailListBean> listener) {
                OrderEvent.getInstance().getMonthOrder(skip, limit, merchant.getObjectId(), new FindStatisticsListener() {
                    @Override
                    public void onSuccess(Object o) {
                        List<IncomeDetailListBean> datas = new ArrayList<IncomeDetailListBean>();
                        try {
                            JSONArray jsonArray = (JSONArray) o;
                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                String[] times = new String[len];
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
                            }
                            listener.onSuccess(datas);
                            listener.onFinish();
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

            @Override
            public void networkSucceed(List<IncomeDetailListBean> datas) {

            }
        });
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
        View headView = ViewUtils.inflaterView(context, R.layout.head_income_detail_list, pullToRefreshListView);
        withDrawLayout = headView.findViewById(R.id.id_withdraw_layout);
        curMonthTotalMoney = (TextView) headView.findViewById(R.id.id_cur_month_sales);
        alreadyWithdrawMoney = (TextView) headView.findViewById(R.id.id_already_withdraw_money);
        alreadyWithdrawNum = (TextView) headView.findViewById(R.id.id_already_withdraw_num);
        canWithdrawMoney = (TextView) headView.findViewById(R.id.id_can_withdraw_money);
        requestWithdrawBtn = (Button) headView.findViewById(R.id.id_btn_request_withdraw);
        pullToRefreshListView.addHeaderView(headView, null, false);
        listProxy();

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
        withDrawLayout.setOnClickListener(this);
        requestWithdrawBtn.setOnClickListener(this);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (merchant != null) {
                    String dayDesc = ((IncomeDetailListBean) parent.getAdapter().getItem(position)).getDayDes();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, merchant);
                    bundle.putString(Constants.KEY_TEXT, dayDesc);
                    LaunchWithExitUtils.startActivity(activity, MonthOrderDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected void ui() {
        userInfoUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_withdraw_layout:
                LaunchWithExitUtils.startActivity(activity, WithdrawRecordActivity.class);
                break;
            case R.id.id_btn_request_withdraw:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_BEAN, merchant);
                LaunchWithExitUtils.startActivity(activity, ApplyWithdrawActivity.class, bundle);
                break;

        }
    }
}
