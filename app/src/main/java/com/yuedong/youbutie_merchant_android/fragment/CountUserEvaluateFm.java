package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.UserEvaluateAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/1/7.
 */
public class CountUserEvaluateFm extends BaseFragment {
    private RefreshProxy<Order> refreshHelper = new RefreshProxy<Order>();
    private PulltoRefreshListView refreshListView;
    private Merchant merchant;


    @Override
    public void initViews(Bundle savedInstanceState) {
        refreshHelper.showEmptyView = false;
        buildUi(null, true, false, false, R.layout.fragment_user_evaluate);
        refreshListView = fvById(R.id.id_refresh_view);
        refreshHelper.setPulltoRefreshRefreshProxy((BaseActivity) getActivity(), refreshListView, new RefreshProxy.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return new UserEvaluateAdapter(getActivity(), data);
            }

            @Override
            public void executeTask(final int skip, final int limit, final FindListener<Order> listener) {
                if (merchant == null) {
                    MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {

                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onFinish() {
                        }

                        @Override
                        public void onSuccess(List<Merchant> list) {
                            merchant = list.get(0);
                            OrderEvent.getInstance().getMerchantEvaluate(skip, limit, merchant.getObjectId(), listener);
                        }

                        @Override
                        public void onError(int i, String s) {
                            error(s);
                        }
                    });
                } else {
                    OrderEvent.getInstance().getMerchantEvaluate(skip, limit, merchant.getObjectId(), listener);
                }

            }

            @Override
            public void networkSucceed(List<Order> datas) {
                if (!refreshHelper.refresh)
                    if (!CommonUtils.listIsNotNull(datas))
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        });
    }

    @Override
    public void initEvents() {

    }
}
