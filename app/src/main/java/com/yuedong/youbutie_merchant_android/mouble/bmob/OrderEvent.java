package com.yuedong.youbutie_merchant_android.mouble.bmob;

import android.webkit.WebView;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class OrderEvent implements BaseEvent {
    private static OrderEvent INSTANCE = new OrderEvent();

    public static OrderEvent getInstance() {
        return INSTANCE;
    }

    /**
     * 获取我的门店订单（根据订单状态）
     *
     * @param skip
     * @param limit
     * @param orderStatus
     * @param userObjId
     * @param findListener
     */
    public void findMyMerchantOrderInfo(final int skip, final int limit, final Integer[] orderStatus, String userObjId, final FindListener<Order> findListener) {
        findListener.onStart();
        // --先查到我的门店
        final BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userObjId);
        merchantBmobQuery.addWhereMatchesQuery("user", "_User", userBmobQuery);
        merchantBmobQuery.findObjects(context, new FindListener<Merchant>() {
            @Override
            public void onSuccess(List<Merchant> list) {
                if (!CommonUtils.listIsNotNull(list)) {
                    T.showShort(context, "您还没有门店");
                    findListener.onFinish();
                } else {
                    // 获取门店订单
                    Merchant merchant = list.get(0);
                    BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
                    BmobQuery<Merchant> merchantBmobQuery1 = new BmobQuery<Merchant>();
                    merchantBmobQuery1.addWhereEqualTo(OBJECT_ID, merchant.getObjectId());
                    orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery1);
                    orderBmobQuery.include("user,merchant");
                    orderBmobQuery.setLimit(limit);
                    orderBmobQuery.setSkip(skip);
                    orderBmobQuery.addWhereContainedIn("state", Arrays.asList(orderStatus));
                    orderBmobQuery.order("-updatedAt");
                    orderBmobQuery.findObjects(context, new FindListener<Order>() {
                        @Override
                        public void onSuccess(List<Order> list) {
                            findListener.onSuccess(list);
                            findListener.onFinish();
                        }

                        @Override
                        public void onError(int i, String s) {
                            findListener.onError(i, s);
                            findListener.onFinish();
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                findListener.onError(i, s);
                findListener.onFinish();
            }
        });

    }


}
