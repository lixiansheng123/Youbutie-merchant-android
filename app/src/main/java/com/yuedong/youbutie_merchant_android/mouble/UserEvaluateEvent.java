package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Appraise;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 用户评价module
 */
public class UserEvaluateEvent implements BaseEvent {
    private static UserEvaluateEvent instance;

    private UserEvaluateEvent() {
    }

    public static UserEvaluateEvent getInstance() {
        if (instance == null)
            synchronized (UserEvaluateEvent.context) {
                if (instance == null)
                    instance = new UserEvaluateEvent();
            }
        return instance;
    }

    /**
     * 获取用户评价根据门店
     *
     * @param skip
     * @param limit
     * @param merchantObjId
     * @param listener
     */
    public void findUserEvaluateByMerchant(int skip, int limit, String merchantObjId, final FindListener<Appraise> listener) {
        listener.onStart();
        BmobQuery<Appraise> bmobQuery = new BmobQuery<Appraise>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo("objectId", merchantObjId);
        bmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        bmobQuery.include("user");
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(skip);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(context, new FindListener<Appraise>() {
            @Override
            public void onSuccess(List<Appraise> list) {
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
