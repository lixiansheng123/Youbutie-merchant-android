package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.IncomeDetailListAdapter;
import com.yuedong.youbutie_merchant_android.bean.IncomeDetailListBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IncomeDetailActivity extends BaseActivity {
    private PullToRefreshListView pullToRefreshListView;
    private IncomeDetailListAdapter adapter;
    private List<IncomeDetailListBean> datas = new ArrayList<IncomeDetailListBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowContentView(R.layout.activity_income_detail);
    }

    @Override
    protected void initViews() {
        pullToRefreshListView = fvById(R.id.id_refresh_view);
        adapter = new IncomeDetailListAdapter(context);
        ListView listView = pullToRefreshListView.getRefreshableView();
        View headView = ViewUtils.inflaterView(context, R.layout.head_income_detail_list, listView);
        listView.addHeaderView(headView, null, false);
        listView.setAdapter(adapter);
        initDatas();
    }

    private void initDatas() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = 12;
        while (month > 0) {
            IncomeDetailListBean bean = new IncomeDetailListBean();
            bean.setMonth(month);
            bean.setDayDes(year + "年" + month + "月");
            datas.add(bean);
            month--;
        }

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {

    }
}
