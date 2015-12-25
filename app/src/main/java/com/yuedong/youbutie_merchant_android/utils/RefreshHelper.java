package com.yuedong.youbutie_merchant_android.utils;

import android.content.Context;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;

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

    public BaseAdapter<T> getAdapter() {
        return adapter;
    }

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
                datas.clear();
                adapter.notifyDataSetChanged();
                executeNetworkTask(activity, 1, proxyRefreshListener);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                L.d("proxyRefreshListener-onPullUpToRefresh");
                executeNetworkTask(activity, 2, proxyRefreshListener);

            }
        });
        activity.dialogStatus(true);
        executeNetworkTask(activity, 1, proxyRefreshListener);

    }


    /**
     * @param mode                 1正常的刷新和下拉刷新 2上拉更多
     * @param proxyRefreshListener
     */
    protected void executeNetworkTask(final BaseActivity context, final int mode, ProxyRefreshListener<T> proxyRefreshListener) {
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
                L.d("proxyRefreshListener-onFinish");
                context.dialogStatus(false);
                refreshListView.onRefreshComplete();
            }

            @Override
            public void onSuccess(List<T> list) {
                L.d("executeTask-> succeed:" + list.toString());
                if (CommonUtils.listIsNotNull(list)) {
                    updateData(list, mode);
                } else {
                    refreshListView.onRefreshComplete();
                    refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    com.yuedong.youbutie_merchant_android.utils.T.showShort(context, "暂无数据");
                }
            }

            @Override
            public void onError(int i, String s) {
                com.yuedong.youbutie_merchant_android.utils.T.showShort(context, "error:" + s);
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


    protected void refreshStatus() {
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
        BaseAdapter<T> getAdapter(List<T> data);

        void executeTask(int skip, int limit, FindListener<T> listener);
    }
}
