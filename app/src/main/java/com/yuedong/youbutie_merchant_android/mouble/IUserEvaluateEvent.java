package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.BaseEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.listener.BmobSaveResultObjListener;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/3.
 */
public interface IUserEvaluateEvent<T> extends BaseEvent {
    /**
     * 获取店铺评价
     *
     * @param skip          忽略的条数
     * @param limit         取多少条
     * @param merchantObjId 店铺主键
     * @param listener      回调
     */
    void findUserEvaluateByMerchant(int skip, int limit, String merchantObjId, FindListener<T> listener);

    /**
     * 获取订单评价
     *
     * @param orderObjectId
     * @param listener
     */
    void findUserEvaluateByOrder(String orderObjectId, FindListener<T> listener);

    /**
     * 增加订单评价
     *
     * @param order
     * @param user
     * @param content
     * @param star
     */
    void addOrderEvaluate(Order order, User user, String content, float star, BmobSaveResultObjListener<T> listener);
}
