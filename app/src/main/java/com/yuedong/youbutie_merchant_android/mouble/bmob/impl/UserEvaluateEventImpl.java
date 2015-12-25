package com.yuedong.youbutie_merchant_android.mouble.bmob.impl;


import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.mouble.IUserEvaluateEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Appraise;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.listener.BmobSaveResultObjListener;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/12/3.
 */
public class UserEvaluateEventImpl implements IUserEvaluateEvent<Appraise> {
    @Override
    public void findUserEvaluateByMerchant(int skip, int limit, String merchantObjId, FindListener<Appraise> listener) {
        BmobQuery<Appraise> bmobQuery = new BmobQuery<Appraise>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo("objectId", merchantObjId);
        bmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        bmobQuery.include("user");
        bmobQuery.setLimit(limit);
        bmobQuery.setSkip(skip);
        bmobQuery.order("-createdAt");
        bmobQuery.findObjects(App.getInstance().getAppContext(), listener);
    }

    @Override
    public void findUserEvaluateByOrder(String orderObjectId, final FindListener<Appraise> listener) {
        listener.onStart();
        BmobQuery<Appraise> bmobQuery = new BmobQuery<Appraise>();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        orderBmobQuery.addWhereEqualTo("objectId", orderObjectId);
        bmobQuery.addWhereMatchesQuery("order", "Order", orderBmobQuery);
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

    @Override
    public void addOrderEvaluate(Order order, User user, String content, float star, final BmobSaveResultObjListener<Appraise> listener) {
        listener.onStart();
        final Appraise appraise = new Appraise();
        appraise.setMerchant(order.getMerchant());
        appraise.setUser(user);
        appraise.setAppraiseTime(new BmobDate(new Date()));
        appraise.setContent(content);
        appraise.setStar(star);
        appraise.setOrder(order);
        appraise.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onResult(appraise);
                listener.onSuccess();
                listener.onFinish();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });
    }


}
