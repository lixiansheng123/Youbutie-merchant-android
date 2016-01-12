package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MerchantEvent implements BaseEvent {

    private static MerchantEvent INSTANCE = new MerchantEvent();

    public static MerchantEvent getInstance() {
        return INSTANCE;
    }

    /**
     * 获取我的门店 先判断门店信息是否发生变更（是由自己来控制的）发生变更联网获取门店信息 没有变更而内存存在我的门店信息就从内存中获取就好了
     *
     * @param userObjectId
     * @param listener
     */
    public void findMeMetchant(String userObjectId, final FindListener<Merchant> listener) {
        final App instance = App.getInstance();
        if (instance.meMerchantInfoChange)
            instance.setMeMerchant(null);
        if (!CommonUtils.listIsNotNull(instance.getMeMerchant())) {
            listener.onStart();
            BmobQuery<Merchant> bmobQuery = new BmobQuery<Merchant>();
            BmobQuery<User> userBmobQuery = new BmobQuery<User>();
            userBmobQuery.addWhereEqualTo(OBJECT_ID, userObjectId);
            bmobQuery.addWhereMatchesQuery("user", "_User", userBmobQuery);
            bmobQuery.findObjects(context, new FindListener<Merchant>() {
                @Override
                public void onSuccess(List<Merchant> list) {
                    instance.meMerchantInfoChange = false;
                    if (CommonUtils.listIsNotNull(list)) {
                        instance.setMeMerchant(list);
                        listener.onSuccess(instance.getMeMerchant());
                    } else {
                        listener.onError(-1, "您还没有门店");
                    }
                    listener.onFinish();
                }

                @Override
                public void onError(int i, String s) {
                    listener.onError(i, s);
                    listener.onFinish();
                }
            });

        } else {
            listener.onSuccess(instance.getMeMerchant());
        }
    }

    /**
     * 获取门店订单用户
     *
     * @param merchantObjectId
     */
    public void getMerchantOrderUser(String merchantObjectId, final FindListener<Order> listener) {
        listener.onStart();
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantbmobQuery = new BmobQuery<Merchant>();
        merchantbmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        // 只查询user对象
        bmobQuery.addQueryKeys("user");
        bmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantbmobQuery);
        bmobQuery.findObjects(context, new FindListener<Order>() {
            @Override
            public void onSuccess(List<Order> list) {
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

    /**
     * 获取门店vip用户
     *
     * @param merchantObjectId
     * @param listener
     */
    public void getMerchantVipUser(String merchantObjectId, final FindListener<Vips> listener) {
        listener.onStart();
        BmobQuery<Vips> vipsBmobQuery = new BmobQuery<Vips>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        vipsBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        vipsBmobQuery.include("user");
        vipsBmobQuery.findObjects(context, new FindListener<Vips>() {
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
