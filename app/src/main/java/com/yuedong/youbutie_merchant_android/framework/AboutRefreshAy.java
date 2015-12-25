package com.yuedong.youbutie_merchant_android.framework;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ScrollListenerImpl;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 封装了下拉刷新和上拉加载
 *
 * @param <T>
 */
public abstract class AboutRefreshAy<T> extends BaseActivity {
    protected SwipeRefreshLayout refreshLayout;
    protected ListView listView;
    protected View listBottomView;
    protected int currentPager;
    private List<T> datas = new ArrayList<T>();
    private boolean canLoadMore;
    private BaseAdapter<T> adapter;

    public void setUp(SwipeRefreshLayout refreshLayout, ListView listView) {
        this.refreshLayout = refreshLayout;
        this.listView = listView;
        listBottomView = ViewUtils.inflaterView(context, R.layout.bottomview_list, listView);
        listView.addFooterView(listBottomView, null, false);
        adapter = getAdapter(datas);
        listView.setAdapter(adapter);
        ViewUtils.hideLayout(listBottomView);
        dialogStatus(true);
        executeNetworkTask(1);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas.clear();
                adapter.notifyDataSetChanged();
                executeNetworkTask(1);
            }
        });
        listView.setOnScrollListener(new ScrollListenerImpl(new ScrollListenerImpl.INeedLoadListener() {
            @Override
            public void onLoad() {
                if (!canLoadMore) return;
                executeNetworkTask(2);
                ViewUtils.showLayout(listBottomView);

            }
        }));
    }

    /**
     * @param mode 1正常的刷新和下拉刷新 2上拉更多
     */
    protected void executeNetworkTask(final int mode) {
        int skip = 0;
        final int limit = Config.PAGER_SIZE;
        if (mode == 2) {
            currentPager++;
            skip = (currentPager - 1) * limit;
        } else if (mode == 1) {
            refreshStatus();
        }
        executeTask(skip, limit, new FindListener<T>() {
            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<T> list) {
                networkSucceed(list);
                L.i("executeTask-> succeed:" + list.toString());
                if (mode == 2)
                    ViewUtils.hideLayout(listBottomView);
                else
                    refreshLayout.setRefreshing(false);
                if (CommonUtils.listIsNotNull(list)) {
                    updateData(list, mode);
                } else {
                    canLoadMore = false;
                    com.yuedong.youbutie_merchant_android.utils.T.showShort(context, "没有更多数据了");
                }
            }

            @Override
            public void onError(int i, String s) {
                refreshLayout.setRefreshing(false);
                if (mode == 2)
                    ViewUtils.hideLayout(listBottomView);
                com.yuedong.youbutie_merchant_android.utils.T.showShort(context, "error:" + s);
            }
        });
    }

    private void updateData(List<T> list, int mode) {
        if (list.size() < Config.PAGER_SIZE)
            canLoadMore = false;
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
        canLoadMore = true;
    }

    protected void networkSucceed(List<T> datas) {

    }

    protected void setEmpty() {
        datas.clear();
        adapter.notifyDataSetChanged();
    }

    protected abstract void executeTask(int skip, int limit, FindListener<T> listener);

    public abstract BaseAdapter<T> getAdapter(List<T> datas);
}
