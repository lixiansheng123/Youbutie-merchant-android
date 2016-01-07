package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.OrderManagerAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/23.
 */
public class OrderFm extends Fragment {
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
        L.d(TAG + flag + "-------------------oncreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.d(TAG + flag + "-------------------onCreateView");
        View contentView = inflater.inflate(R.layout.fragment_order_fm, container, false);
        initViews(contentView);

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        inintFinished = true;
    }

    private void initViews(View contentView) {
        refreshView = (PullToRefreshListView) contentView.findViewById(R.id.id_refresh_view);
        updateData();
        refreshView.getRefreshableView().setDivider(getResources().getDrawable(R.color.greyd6d1ca));
    }

    private void proxy() {
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
                    orderState = new Integer[]{2, 3};
                } else if ("已完结".equals(flag)) {
                    orderState = new Integer[]{4};
                }
                OrderEvent.getInstance().findMyMerchantOrderInfo(skip, limit, orderState, Constants.TEST_USER_ID, listener);
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
        L.d(TAG + flag + "-------------------onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d(TAG + flag + "-------------------onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d(TAG + flag + "-------------------onDestroy");
    }

}
