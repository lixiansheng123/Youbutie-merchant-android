package com.yuedong.youbutie_merchant_android.framework;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
 * 封装刷新和加载
 *
 * @param <T>
 */
public abstract class AboutRefreshFm<T> extends Fragment {
    protected int currentPager;
    private List<T> datas = new ArrayList<T>();
    private BaseAdapter<T> adapter;
    protected PullToRefreshListView refreshListView;

    public void setUp(/*SwipeRefreshLayout refreshLayout, ListView listView*/PullToRefreshListView refreshListView) {
        this.refreshListView = refreshListView;
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        adapter = getAdapter(datas);
        refreshListView.setAdapter(adapter);
        refreshListView.setRefreshing(true);
        executeNetworkTask(1);
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                datas.clear();
                adapter.notifyDataSetChanged();
                executeNetworkTask(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                executeNetworkTask(2);
            }
        });
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
//                dialogStatus(false);
                refreshListView.onRefreshComplete();
            }

            @Override
            public void onSuccess(List<T> list) {
                networkSucceed(list);
                L.i("executeTask-> succeed:" + list.toString());
//                if (mode == 2)
//                    ViewUtils.hideLayout(listBottomView);
//                else
//                    refreshLayout.setRefreshing(false);
                if (CommonUtils.listIsNotNull(list)) {
                    updateData(list, mode);
                } else {
//                    canLoadMore = false;
                    refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    com.yuedong.youbutie_merchant_android.utils.T.showShort(getActivity(), "没有更多数据了");
                }
            }

            @Override
            public void onError(int i, String s) {
//                refreshLayout.setRefreshing(false);
//                refreshListView.onRefreshComplete();
//                if (mode == 2)
//                    ViewUtils.hideLayout(listBottomView);
                com.yuedong.youbutie_merchant_android.utils.T.showShort(getActivity(), "error:" + s);
            }
        });
    }

    private void updateData(List<T> list, int mode) {
        if (list.size() < Config.PAGER_SIZE)
//            canLoadMore = false;
            refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        else
            refreshListView.setMode(PullToRefreshBase.Mode.BOTH);
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

    protected void networkSucceed(List<T> datas) {

    }

    protected void setEmpty() {
        datas.clear();
        adapter.notifyDataSetChanged();
    }

    protected abstract void executeTask(int skip, int limit, FindListener<T> listener);

    public abstract BaseAdapter<T> getAdapter(List<T> datas);
}
