package com.yuedong.youbutie_merchant_android.framework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 俊鹏
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected/* 可以給子类使用 */ List<T> mDatas;
    protected Context mCon;
    protected static LayoutInflater mInflater;
    private int layoutId;


    public View inflaterView(int res) {
        return mInflater.inflate(res, null);
    }

    public BaseAdapter(Context con, List<T> data, int layouid) {
        this.mDatas = data;
        this.mCon = con;
        mInflater = LayoutInflater.from(con);
        this.layoutId = layouid;
    }

    public BaseAdapter(Context con, int layoutId) {
        this(con, null, layoutId);
    }

    public void setData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            this.mDatas = data;
        }
    }

    public void setData(T[] ts) {
        if (ts != null && ts.length > 0) {
            mDatas = new ArrayList<T>();
            for (T t : ts) {
                this.mDatas.add(t);
            }
        }
    }

    /**
     * 把适配器设置空
     */
    public void setEmpty() {
        if (CommonUtils.listIsNotNull(mDatas)) {
            mDatas.clear();
            notifyDataSetChanged();
            mDatas = null;
        }
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        L.d("BaseAdapter------------getView");
        ViewHolder viewHolder = ViewHolder.get(mCon, convertView, parent, layoutId, position);
        convert(viewHolder, getItem(position), position, viewHolder.getConvertView());
        return viewHolder.getConvertView();
    }

    abstract public void convert(ViewHolder viewHolder, T t, int position, View convertView);


}
