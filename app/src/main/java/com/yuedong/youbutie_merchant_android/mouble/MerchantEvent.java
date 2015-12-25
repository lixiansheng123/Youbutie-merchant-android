package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.BaseEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MerchantEvent implements BaseEvent {

    private static MerchantEvent INSTANCE = new MerchantEvent();

    public static MerchantEvent getInstance() {
        return INSTANCE;
    }

    /**
     * 获取我的门店
     *
     * @param userObjectId
     * @param listener
     */
    public void findMeMetchant(String userObjectId, final FindListener<Merchant> listener) {
        listener.onStart();
        BmobQuery<Merchant> bmobQuery = new BmobQuery<Merchant>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userObjectId);
        bmobQuery.addWhereMatchesQuery("user", "_User", userBmobQuery);
        bmobQuery.findObjects(context, new FindListener<Merchant>() {
            @Override
            public void onSuccess(List<Merchant> list) {
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
