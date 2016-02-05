package com.yuedong.youbutie_merchant_android.utils;

import android.view.View;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class RefreshProxy<T> {
    public BaseAdapter<T> adapter;
    public List<T> datas = new ArrayList<T>();
    public PulltoRefreshListView listView;
    public int currentPager;
    // 是否代理了emptyview
    public boolean showEmptyView = true;
    private android.os.Handler handler = new android.os.Handler();

    public BaseAdapter<T> getAdapter() {
        return adapter;
    }

    public boolean refresh = false;
    private boolean one = true;

    public PulltoRefreshListView getListView() {
        return listView;
    }

    /**
     * 设置pulltorefresh代理刷新和加载更多
     *
     * @param listView
     * @param proxyRefreshListener
     */
    public void setPulltoRefreshRefreshProxy(final BaseActivity activity, PulltoRefreshListView listView, final ProxyRefreshListener<T> proxyRefreshListener) {
        this.listView = listView;
        if (listView == null || proxyRefreshListener == null) return;
        adapter = proxyRefreshListener.getAdapter(datas);
        if (adapter != null)
            listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PulltoRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!refresh) refresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeNetworkTask(activity, 1, proxyRefreshListener);
                    }
                }, 500);
                if (one) {
                    one = false;
                    refresh = false;
                }
            }

            @Override
            public void onLoad() {
                if (!refresh) refresh = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeNetworkTask(activity, 2, proxyRefreshListener);
                    }
                }, 500);
            }
        });
        // 进入自动刷新
        listView.autoRefresh();
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
                L.d("executeTask- onFinish");
                listView.onRefreshComplete();
            }

            @Override
            public void onSuccess(List<T> list) {
                listView.onRefreshComplete();
                proxyRefreshListener.networkSucceed(list);
                if (CommonUtils.listIsNotNull(list)) {
                    updateData(list, mode);
                } else {
                    listView.onRefreshComplete();
                    listView.setLoadFull();
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
                L.d("executeTask-onError");
                context.error(i);
            }
        });
    }

    private void updateData(List<T> list, int mode) {
        if (list.size() < Config.PAGER_SIZE) {
            listView.setLoadFull();
        }
        if (mode == 1) {
            datas.clear();
            datas.addAll(list);
        } else
            datas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    public void refreshStatus() {
//        datas.clear();
        currentPager = 1;
    }

    public void setEmptyUi() {
        L.d("setEmptyUi---------------->");
        if (adapter == null) return;
        datas.clear();
        adapter.notifyDataSetChanged();
    }

    public void setEmpty() {
        L.d("setEmpty---------------->");
        adapter = null;
        listView = null;
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
