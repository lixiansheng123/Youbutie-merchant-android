package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.BaseEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class VipEvent implements BaseEvent {
    public static VipEvent vipEvent;

    private VipEvent() {
    }


    public static VipEvent getInstance() {
        if (vipEvent == null) {
            synchronized (VipEvent.context) {
                if (vipEvent == null)
                    vipEvent = new VipEvent();
            }
        }
        return vipEvent;
    }

    /**
     * 通过车型来查找vip门店用户
     */
    public void findVipByCar(String merchantObjectId, List<Car> cars, final FindListener<Vips> listener) {
        listener.onStart();
        BmobQuery<Vips> mainBmobQuery = new BmobQuery<Vips>();
        List<BmobQuery<Vips>> ors = new ArrayList<BmobQuery<Vips>>();
        List<BmobQuery<Vips>> ands = new ArrayList<BmobQuery<Vips>>();
        for (Car car : cars) {
            BmobQuery<Vips> vipsBmobQuery = new BmobQuery<Vips>();
            BmobQuery<Car> carBmobQuery = new BmobQuery<Car>();
            carBmobQuery.addWhereEqualTo(OBJECT_ID, car.getObjectId());
            vipsBmobQuery.addWhereMatchesQuery("car", "Car", carBmobQuery);
            ors.add(vipsBmobQuery);
        }
        BmobQuery<Vips> vipsBmobQuery = new BmobQuery<Vips>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        vipsBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        ands.add(vipsBmobQuery);
        mainBmobQuery.and(ands);
        mainBmobQuery.or(ors);
        mainBmobQuery.include("user");
        mainBmobQuery.findObjects(context, new FindListener<Vips>() {
            @Override
            public void onSuccess(List<Vips> list) {
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
