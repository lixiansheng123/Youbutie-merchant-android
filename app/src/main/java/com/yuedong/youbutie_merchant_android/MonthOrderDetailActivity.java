package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.MonthOrderDetailAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class MonthOrderDetailActivity extends BaseActivity {
    private RefreshHelper<Order> refreshHelper = new RefreshHelper<Order>();
    private Merchant merchant;
    private String dayDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        dayDesc = extras.getString(Constants.KEY_TEXT);
        merchant = (Merchant) extras.getSerializable(Constants.KEY_BEAN);
        String title = null;
        if (StringUtil.isNotEmpty(dayDesc))
            title = dayDesc.substring(dayDesc.length() - 2);
        buildUi(new TitleViewHelper().createDefaultTitleView3(title + "清单明细"), false, false, false, R.layout.activity_month_order_detail);
    }

    @Override
    protected void initViews() {
        refreshHelper.setPulltoRefreshRefreshProxy(this, (PullToRefreshListView) fvById(R.id.id_refresh_view), new RefreshHelper.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return new MonthOrderDetailAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<Order> listener) {
                OrderEvent.getInstance().getMonthOrderDetail(skip, limit, merchant.getObjectId(), dayDesc, listener);
            }
        });
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {

    }
}
