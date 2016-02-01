package com.yuedong.youbutie_merchant_android.utils;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 把刷新和加载更多代理
 *
 * @param <T>
 */
public class RefreshHelper<T> {
    public BaseAdapter<T> adapter;
    public List<T> datas = new ArrayList<T>();
    public PullToRefreshListView refreshListView;
    public int currentPager;
    // 是否代理了emptyview
    public boolean showEmptyView = true;
    private android.os.Handler handler = new android.os.Handler();

    public BaseAdapter<T> getAdapter() {
        return adapter;
    }

    public boolean refresh = false;

    public PullToRefreshListView getRefreshListView() {
        return refreshListView;
    }

    /**
     * 设置pulltorefresh代理刷新和加载更多
     *
     * @param refreshListView
     * @param proxyRefreshListener
     */
    public void setPulltoRefreshRefreshProxy(final BaseActivity activity, PullToRefreshListView refreshListView, final ProxyRefreshListener<T> proxyRefreshListener) {
        this.refreshListView = refreshListView;
        if (refreshListView == null || proxyRefreshListener == null) return;
        AppUtils.setRefreshBaseParams(refreshListView);
        adapter = proxyRefreshListener.getAdapter(datas);
        if (adapter != null)
            refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!refresh) refresh = true;
                datas.clear();
                adapter.notifyDataSetChanged();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeNetworkTask(activity, 1, proxyRefreshListener);
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!refresh) refresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeNetworkTask(activity, 2, proxyRefreshListener);
                    }
                }, 500);
            }
        });
        activity.dialogStatus(true);
        executeNetworkTask(activity, 1, proxyRefreshListener);

    }


    /**
     * @param mode                 1正常的刷新和下拉刷新 2上拉更多
     * @param proxyRefreshListener
     */
    protected void executeNetworkTask(final BaseActivity context, final int mode, final ProxyRefreshListener<T> proxyRefreshListener) {
        context.mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        L.d("executeTask-onStart");
        int skip = 0;
        final int limit = Config.PAGER_SIZE;
        if (mode == 2) {
            currentPager++;
            skip = (currentPager - 1) * limit;
        } else if (mode == 1) {
            refreshStatus();
        }
        proxyRefreshListener.executeTask(skip, limit, new FindListener<T>() {
            @Override
            public void onFinish() {
                L.d("executeTask-onFinish");
                context.dialogStatus(false);
                refreshListView.onRefreshComplete();
            }

            @Override
            public void onSuccess(List<T> list) {
                proxyRefreshListener.networkSucceed(list);
                L.d("executeTask-> succeed:" + list.toString());
                if (CommonUtils.listIsNotNull(list)) {
                    updateData(list, mode);
                } else {
                    refreshListView.onRefreshComplete();
                    refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    if (!refresh) {
                        if (!showEmptyView) return;
                        try {
                            View emptyView = context.mMultiStateView.getView(MultiStateView.VIEW_STATE_EMPTY);
                            if (emptyView == null)
                                context.mMultiStateView.setViewForState(R.layout.empty_view, MultiStateView.VIEW_STATE_EMPTY);
                            context.mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                L.i("executeTask-onError");
                context.error(i);
            }
        });
    }

    private void updateData(List<T> list, int mode) {
        if (list.size() < Config.PAGER_SIZE) {
            refreshListView.onRefreshComplete();
            refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            refreshListView.onRefreshComplete();
            refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        if (mode == 1) {
            datas.clear();
            datas.addAll(list);
        } else
            datas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    public void refreshStatus() {
        datas.clear();
        currentPager = 1;
//        canLoadMore = true;
    }

    public void setEmptyUi() {
        if (adapter == null) return;
        datas.clear();
        adapter.notifyDataSetChanged();
    }

    public void setEmpty() {
        adapter = null;
        refreshListView = null;
    }


    public interface ProxyRefreshListener<T> {
        /**
         * 获取适配器
         */
        BaseAdapter<T> getAdapter(List<T> data);

        /**
         * 执行网络方法
         */
        void executeTask(int skip, int limit, FindListener<T> listener);

        /**
         * 成功回调
         */
        void networkSucceed(List<T> datas);
    }
}
