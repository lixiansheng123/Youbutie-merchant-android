package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.mouble.bmob.BaseEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;

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
                    instance.setMeMerchant(list);
                    listener.onSuccess(instance.getMeMerchant());
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

}
