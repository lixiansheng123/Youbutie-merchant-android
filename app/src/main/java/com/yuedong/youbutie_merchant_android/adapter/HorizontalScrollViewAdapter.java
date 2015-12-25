package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yuedong.youbutie_merchant_android.framework.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
public abstract class HorizontalScrollViewAdapter<T> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<T> mDatas;
    private int mLayoutid;

    public HorizontalScrollViewAdapter(Context context, List<T> mDatas, int layoutid) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mLayoutid = layoutid;
    }

    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public T getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, mLayoutid, position);
        convert(viewHolder, getItem(position), position, convertView);
        return viewHolder.getConvertView();
    }

    protected abstract void convert(ViewHolder viewHolder, T t, int position, View convertView);


}
