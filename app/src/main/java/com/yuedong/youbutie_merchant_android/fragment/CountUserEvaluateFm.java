package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.UserEvaluateAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/1/7.
 */
public class CountUserEvaluateFm extends BaseFragment {
    private RefreshHelper<Order> refreshHelper = new RefreshHelper<Order>();
    private PullToRefreshListView refreshListView;
    private Merchant merchant;

    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_user_evaluate);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        refreshListView = fvById(R.id.id_refresh_view);
        refreshHelper.setPulltoRefreshRefreshProxy((BaseActivity) getActivity(), refreshListView, new RefreshHelper.ProxyRefreshListener<Order>() {
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
                            dialogStatus(true);
                        }

                        @Override
                        public void onFinish() {
                            dialogStatus(false);
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
        });
    }

    @Override
    public void initEvents() {

    }
}
