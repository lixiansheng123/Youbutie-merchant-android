package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

public class AlreadySelectCarAdapter extends BaseAdapter<Car> {
    private OnDeleteSelectListener<Car> onDeleteSelectListener;
    private int action = ACTION_ALREADY_SEELCT_CAR;
    public static final int ACTION_ALREADY_SEELCT_CAR = 0x001;
    public static final int ACTION_SHOW = 0x002;

    public void setAction(int action) {
        this.action = action;
    }

    public void setOnDeleteSelectListener(OnDeleteSelectListener<Car> onDeleteSelectListener) {
        this.onDeleteSelectListener = onDeleteSelectListener;
    }

    public AlreadySelectCarAdapter(Context con, List<Car> data) {
        super(con, data, R.layout.item_already_select_car);
    }

    public void insert(Car car) {
        L.d("insert:car" + car.toString());
        if (!mDatas.contains(car)) {
            mDatas.add(car);
            notifyDataSetChanged();
        }
    }

    @Override
    public void convert(ViewHolder viewHolder, final Car car, int position, View convertView) {
        TextView text = viewHolder.getIdByView(R.id.id_text);
        ImageView close = viewHolder.getIdByView(R.id.id_del);
        text.setText(car
                .getName());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteSelectListener != null)
                    onDeleteSelectListener.onDelete(car);
                mDatas.remove(car);
                notifyDataSetChanged();
            }
        });
        if (action == ACTION_SHOW) {
            text.setPadding(text.getPaddingLeft(), 0, text.getPaddingRight(), text.getPaddingBottom());
            ViewUtils.hideLayout(close);
        }
    }

    public interface OnDeleteSelectListener<T> {
        void onDelete(T t);
    }
}
