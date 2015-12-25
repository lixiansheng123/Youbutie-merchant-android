package com.yuedong.youbutie_merchant_android.utils;

import android.widget.AbsListView;

/**
 * Created by Administrator on 2015/12/2.
 *
 * @author 俊鹏
 */
public class ScrollListenerImpl implements AbsListView.OnScrollListener {
    private INeedLoadListener listener;

    public ScrollListenerImpl(INeedLoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                if (listener != null) {
                    listener.onLoad();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface INeedLoadListener {
        void onLoad();
    }
}
