package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.OrderManagerAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/23.
 */
public class OrderFm extends BaseFragment {
    private static final String TAG = "OrderFm--";
    String flag;
    private RefreshHelper<Order> refreshHelper;
    private PullToRefreshListView refreshView;
    public boolean inintFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = getArguments().getString(Constants.KEY_ACTION);
        refreshHelper = new RefreshHelper<Order>();
        refreshHelper.showEmptyView = false;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        buildUi(null, true, false, false, R.layout.fragment_order_fm);
        refreshView = fvById(R.id.id_refresh_view);
        updateData();
    }


    @Override
    public void initEvents() {

    }

    @Override
    public void onResume() {
        super.onResume();
        inintFinished = true;
    }

    private void proxy() {
        if (mMultiStateView.getViewState() != MultiStateView.VIEW_STATE_CONTENT)
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        refreshHelper.setPulltoRefreshRefreshProxy((BaseActivity) getActivity(), refreshView, new RefreshHelper.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return new OrderManagerAdapter(getActivity(), data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<Order> listener) {
                Integer[] orderState = null;
                if ("新订单".equals(flag)) {
                    orderState = new Integer[]{1};
                } else if ("已接单".equals(flag)) {
                    orderState = new Integer[]{2, 3, 6};
                } else if ("已完结".equals(flag)) {
                    orderState = new Integer[]{4};
                }
                OrderEvent.getInstance().findMyMerchantOrderInfo(skip, limit, orderState, App.getInstance().getUser().getObjectId(), listener);
            }

            @Override
            public void networkSucceed(List<Order> datas) {
                if (!refreshHelper.refresh) {
                    if (!CommonUtils.listIsNotNull(datas))
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        });
    }

    public void updateData() {
        refreshHelper.setEmptyUi();
        refreshHelper.setEmpty();
        proxy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
