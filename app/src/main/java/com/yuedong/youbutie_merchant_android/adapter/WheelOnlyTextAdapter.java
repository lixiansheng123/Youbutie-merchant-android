package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yuedong.youbutie_merchant_android.R;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by Administrator on 2015/12/28.
 */

public class WheelOnlyTextAdapter extends AbstractWheelTextAdapter {
    private List<String> mDatas;

    public WheelOnlyTextAdapter(Context context) {
        super(context, R.layout.item_wheel_text, NO_RESOURCE);
        setItemTextResource(R.id.id_text);
    }

    public void setData(List<String> data) {
        if (data == null)
            return;
        this.mDatas = data;
        // 通知删除无效数据
        notifyDataInvalidatedEvent();
        // 通知数据发生改变
        notifyDataChangedEvent();
    }

    public void setData(String[] strArray) {
        if (strArray == null || strArray.length <= 0)
            return;
        if (mDatas == null) {
            mDatas = new ArrayList<String>();
        }
        for (String str : strArray) {
            mDatas.add(str);
        }

    }

    /**
     * 清除数据源
     *
     * @param isRefreshData 刷新适配器
     */
    public void clearData(boolean isRefreshData) {
        mDatas.clear();
        if (isRefreshData)
            notifyDataChangedEvent();
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (null != mDatas) {
            return mDatas.get(index);
        } else {
            return "";
        }
    }
}