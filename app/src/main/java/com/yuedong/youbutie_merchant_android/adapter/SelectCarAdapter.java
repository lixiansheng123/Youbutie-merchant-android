package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.zxing.decoding.Intents;

import java.util.ArrayList;
import java.util.List;

public class SelectCarAdapter extends BaseAdapter<Car> implements SectionIndexer {
    private AdapterView.OnItemClickListener onItemClickListener;
    private List<Car> selectPosition = new ArrayList<Car>();

    /**
     * 去除选择 先根据car找到对应的index 再从数组中移除对应index并刷新适配器
     *
     * @param selectCar
     */
    public void delSelect(Car selectCar) {
//        Integer index = -1;
//        for (int i = 0; i < mDatas.size(); i++) {
//            if (mDatas.get(i).getObjectId().equals(selectCar.getObjectId()))
//                index = i;
//        }
//        if (index != -1) {
        if (selectPosition.contains(selectCar)) {
            selectPosition.remove(selectCar);
            notifyDataSetChanged();
        }
//        }
    }

    public void addSelect(Car car) {
        if (!selectPosition.contains(car))
//            selectPosition.remove(position);
//        else
            selectPosition.add(car);

    }

    public List<Car> getSelect() {
        return selectPosition;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SelectCarAdapter(Context con, List<Car> data) {
        super(con, data, R.layout.item_select_car);
    }

    public int getSectionForPosition(int position) {
        return mDatas.get(position).getLetter().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    public int getPositionFromData(String key) {
        for (int i = 0; i < mDatas.size(); i++) {
            Car car = mDatas.get(i);
            if (car.getLetter().equals(key))
                return i;
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }


    @Override
    public void convert(final ViewHolder viewHolder, final Car t, final int position,
                        final View convertView) {
        TextView letter = viewHolder.getIdByView(R.id.id_letter);
        TextView carName = viewHolder.getIdByView(R.id.id_car_name);
        ImageView gougou = viewHolder.getIdByView(R.id.id_gougou);
        View line = viewHolder.getIdByView(R.id.id_line);
        int section = getSectionForPosition(position);
        // 一个拥有相同字母的item第一个显示指示字母 其他不显示
        if (position == getPositionForSection(section)) {
            // 显示字母条
            ViewUtils.showLayout(letter);
            letter.setText(t.getLetter());
        } else {
            // 隐藏字母条
            ViewUtils.hideLayout(letter);
        }

        carName.setText(t.getName());
        viewHolder.getIdByView(R.id.id_click_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(null, viewHolder.getConvertView(), position, -1);
                addSelect(t);
                notifyDataSetChanged();
            }
        });

        if (selectPosition.contains(t))
            ViewUtils.showLayout(gougou);
        else
            ViewUtils.hideLayout(gougou);

        // ------------------------------控制恶心的线----------------------------------
        int nextPosition = position + 1;
        if (nextPosition < getCount()) {
            int nextSection = getSectionForPosition(nextPosition);
            if (nextPosition == getPositionForSection(nextSection)) {
                ViewUtils.hideLayout(line);
            } else {
                ViewUtils.showLayout(line);
            }
        }

        if (position == getCount() - 1) {
            ViewUtils.hideLayout(line);
        }
    }
}