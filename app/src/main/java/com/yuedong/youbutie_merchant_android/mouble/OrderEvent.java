package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.BaseEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class OrderEvent implements BaseEvent {

    private static OrderEvent INSTANCE;

    public static OrderEvent getInstance() {
        if (INSTANCE == null) {
            synchronized (OrderEvent.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OrderEvent();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取门店完成交易的订单并统计购买次数
     *
     * @param merchantObjectId 门店id
     * @param serviceId        服务id 作为筛选条件
     * @param carObjectId      车型id 作为筛选条件
     */
    public void getMemberFinishedOrderAndCountBuyNum(int skip, int limit, final String merchantObjectId, String serviceId, String carObjectId, final FindListener<Order> listener) {
        L.i("getMemberFinishedOrderAndCountBuyNum->skip;" + skip + "-limit:" + limit + "-merchantObjectId:" + merchantObjectId + "-serviceId:" + serviceId + "-carObjectId:" + carObjectId);
        listener.onStart();
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        bmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        bmobQuery.addWhereEqualTo("state", 4);
        bmobQuery.include("user");
        bmobQuery.order("createdAt");
        bmobQuery.setSkip(skip);
        bmobQuery.setLimit(limit);

        if (serviceId != null) {
            List<String> services = new ArrayList<String>();
            services.add(serviceId);
            bmobQuery.addWhereContainsAll("serviceIds", services);
        }

        if (carObjectId != null) {
            BmobQuery<Car> carBmobQuery = new BmobQuery<Car>();
            carBmobQuery.addWhereEqualTo(OBJECT_ID, carObjectId);
            bmobQuery.addWhereMatchesQuery("car", "Car", carBmobQuery);
        }

        bmobQuery.findObjects(context, new FindListener<Order>() {
            @Override
            public void onSuccess(final List<Order> list) {
                if (CommonUtils.listIsNotNull(list)) {
                    // 统计来店次数
                    for (int i = 0; i < list.size(); i++) {
                        final Order order = list.get(i);
                        User user = order.getUser();
                        if (user == null) continue;
                        BmobQuery<Order> countQuery = new BmobQuery<Order>();
                        // 统计的是对应用户的
                        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
                        userBmobQuery.addWhereEqualTo(OBJECT_ID, user.getObjectId());
                        //  统计还得是自己店的信息
                        BmobQuery<Merchant> merchantBmobQuerCount = new BmobQuery<Merchant>();
                        merchantBmobQuerCount.addWhereEqualTo(OBJECT_ID, merchantObjectId);
                        // 统计的是已经完成交易的订单
                        countQuery.addWhereEqualTo("state", 4);
                        //---使用复合查询同时匹配两个外键(and)
                        List<BmobQuery<Order>> ands = new ArrayList<BmobQuery<Order>>();
                        BmobQuery<Order> addWhereMatchesQuery1 = new BmobQuery<Order>();
                        BmobQuery<Order> addWhereMatchesQuery2 = new BmobQuery<Order>();
                        addWhereMatchesQuery1.addWhereMatchesQuery("user", "_User", userBmobQuery);
                        addWhereMatchesQuery2.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuerCount);
                        ands.add(addWhereMatchesQuery1);
                        ands.add(addWhereMatchesQuery2);
                        countQuery.and(ands);
                        final int index1 = i;
                        countQuery.count(context, Order.class, new CountListener() {
                            @Override
                            public void onSuccess(int count) {
                                order.setBuyNum(count);
                                int index2 = index1;
                                if (index2 == list.size() - 1) {
                                    listener.onSuccess(list);
                                    listener.onFinish();
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                listener.onError(i, s);
                                listener.onFinish();
                                return;
                            }
                        });
                    }
                } else {
                    listener.onFinish();
                }
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
                listener.onFinish();
            }
        });

    }
}
