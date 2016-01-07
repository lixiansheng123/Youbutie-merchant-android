package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CarEvent implements BaseEvent {
    private static CarEvent carEvent;

    public static CarEvent getInstance() {
        if (carEvent == null) {
            synchronized (CarEvent.class) {
                if (carEvent == null) {
                    carEvent = new CarEvent();
                }
            }
        }
        return carEvent;
    }

    /**
     * 获取所有车型
     *
     * @param listener
     */
    public void getAllCar(final FindListener<Car> listener) {
        listener.onStart();
        BmobQuery<Car> bmobQuery = new BmobQuery<Car>();
        bmobQuery.findObjects(context, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> list) {
                listener.onSuccess(list);
                listener.onFinish();

            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
                listener.onFinish();
            }
        });
    }

}
