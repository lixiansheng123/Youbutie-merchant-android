package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

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

    /**
     * 获取对应用户的门店订单
     */
    public void getUserOrderByMerchant(int skip, int limit, String userId, String merchantId, final FindListener<Order> listener) {
        listener.onStart();
        BmobQuery<Order> mainQurey = new BmobQuery<Order>();
        List<BmobQuery<Order>> ands = new ArrayList<BmobQuery<Order>>();
        BmobQuery<Order> eq1 = new BmobQuery<Order>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userId);
        eq1.addWhereMatchesQuery("user", "_User", userBmobQuery);
        BmobQuery<Order> eq2 = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantId);
        eq2.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        ands.add(eq1);
        ands.add(eq2);
        mainQurey.and(ands);
        mainQurey.order("-createdAt");
        mainQurey.addWhereEqualTo("state", 4);
        mainQurey.setSkip(skip);
        mainQurey.setLimit(limit);
        mainQurey.findObjects(context, new FindListener<Order>() {
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
     * 获取门店订单数
     */
    public void getMerchantOrderNum(String merchantObjectId, final CountListener listener) {
        listener.onStart();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        orderBmobQuery.count(context, Order.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                listener.onSuccess(i);
                listener.onFinish();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });

    }

    /**
     * 获取门店对应服务订单个数
     */
    public void getOrderTypeNumberByMerchant(String merchantObjectId, String serviceObjectId, final CountListener listener) {
        listener.onStart();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        orderBmobQuery.addWhereContainsAll("serviceIds", Arrays.asList(new String[]{serviceObjectId}));
        orderBmobQuery.count(context, Order.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                listener.onSuccess(i);
                listener.onFinish();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });


    }

    private int succeedCount = 0;

    /**
     * 获取门店完成交易的订单并统计购买次数
     *
     * @param merchantObjectId 门店id
     * @param serviceId        服务id 作为筛选条件
     * @param carObjectId      车型id 作为筛选条件
     */
    public void getMemberFinishedOrderAndCountBuyNum(int skip, int limit, final String merchantObjectId, String serviceId, String carObjectId, final FindListener<Order> listener) {
        succeedCount = 0;
        L.d("getMemberFinishedOrderAndCountBuyNum->skip;" + skip + "-limit:" + limit + "-merchantObjectId:" + merchantObjectId + "-serviceId:" + serviceId + "-carObjectId:" + carObjectId);
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
                        countQuery.count(context, Order.class, new CountListener() {
                            @Override
                            public void onSuccess(int count) {
                                order.buyNum = count;
                                succeedCount++;
                                if (succeedCount == list.size()) {
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

    /**
     * 获取当月销售额
     */
    public void getCurMonthSale(String merchantObjectId, final FindStatisticsListener listener) {
        // 使用时间查询查到当月订单求再和
        BmobQuery<Order> mainQurey = new BmobQuery<Order>();
        List<BmobQuery<Order>> and = new ArrayList<BmobQuery<Order>>();
        BmobQuery<Order> q1 = new BmobQuery<Order>();
        Date startDate = new Date(DateUtils.getCurMonthStartTime());
        // 查询指定日期之后的数据(包括当天)
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(startDate));
        and.add(q1);
        // 小于结束时间
        BmobQuery<Order> q2 = new BmobQuery<Order>();
        Date endDate = new Date(DateUtils.getCurMonthEndTime());
        // 想查询指定日期之前的数据(包括当天)
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(endDate));
        and.add(q2);
        mainQurey.and(and);
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        mainQurey.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        mainQurey.sum(new String[]{"price"});
        mainQurey.addWhereEqualTo("state", 4);
        mainQurey.findStatistics(context, Order.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                listener.onSuccess(o);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
            }
        });
    }

    /**
     * 获取当月会员
     *
     * @param merchantObjectId
     * @param listener
     */
    public void getCurMonthUser(String merchantObjectId, final FindListener<Order> listener) {
        listener.onStart();
        BmobQuery<Order> mainQurey = new BmobQuery<Order>();
        List<BmobQuery<Order>> and = new ArrayList<BmobQuery<Order>>();
        BmobQuery<Order> q1 = new BmobQuery<Order>();
        Date startDate = new Date(DateUtils.getCurMonthStartTime());
        // 查询指定日期之后的数据(包括当天)
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(startDate));
        and.add(q1);
        // 小于结束时间
        BmobQuery<Order> q2 = new BmobQuery<Order>();
        Date endDate = new Date(DateUtils.getCurMonthEndTime());
        // 想查询指定日期之前的数据(包括当天)
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(endDate));
        and.add(q2);
        mainQurey.and(and);
        // 查询制定列
        mainQurey.addQueryKeys("user");
        mainQurey.addWhereEqualTo("state", 4);
        mainQurey.findObjects(context, new FindListener<Order>() {
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
     * 获取自己门店月份订单
     */
    public void getOrderByMerchantAndTime(String merchantObjectId, String timeDesc, final FindListener<Order> listener) {
        listener.onStart();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        orderBmobQuery.addWhereEqualTo("sumMonth", timeDesc);
        // 只需要价格返回
        orderBmobQuery.addQueryKeys("price");
        orderBmobQuery.findObjects(context, new FindListener<Order>() {
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
     * 获取月份订单 统计单数和计算总收入
     */
    public void getMonthOrder(int skip, int limit, String merchantObjectId, final FindStatisticsListener listener) {
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        orderBmobQuery.sum(new String[]{"price"}); // 统计订单钱
        orderBmobQuery.groupby(new String[]{"sumMonth"}); // 按照sumMonth进行分组
        orderBmobQuery.setHasGroupCount(true);// 返回分组记录条数
        orderBmobQuery.setSkip(skip);
        orderBmobQuery.setLimit(limit);
        orderBmobQuery.addWhereEqualTo("state", 4);
        orderBmobQuery.findStatistics(context, Order.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                listener.onSuccess(o);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
            }
        });
    }

    /**
     * 获取月份订单订单详情
     *
     * @param skip
     * @param limit
     * @param merchantId
     * @param timeDesc
     * @param listener
     */
    public void getMonthOrderDetail(int skip, int limit, String merchantId, String timeDesc, final FindListener<Order> listener) {
        listener.onStart();
        BmobQuery<Order> orderBmobQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantId);
        orderBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        List<BmobQuery<Order>> ands = new ArrayList<BmobQuery<Order>>();
        BmobQuery<Order> q1 = new BmobQuery<Order>();
        q1.addWhereEqualTo("state", 4);
        ands.add(q1);
        BmobQuery<Order> q2 = new BmobQuery<Order>();
        q2.addWhereEqualTo("sumMonth", timeDesc);
        ands.add(q2);
        orderBmobQuery.and(ands);
        orderBmobQuery.setSkip(skip);
        orderBmobQuery.setLimit(limit);
        orderBmobQuery.order("createdAt");
        orderBmobQuery.include("user");
        orderBmobQuery.findObjects(context, new FindListener<Order>() {
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
     * 统计指定当年指定月份的客户下单情况
     */
    public void countAssignMonthClientDownOrderCase(String merchantObjectId, int month, final FindStatisticsListener listener) {
        BmobQuery<Order> mainQuery = new BmobQuery<Order>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        mainQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        List<BmobQuery<Order>> ands = new ArrayList<BmobQuery<Order>>();
        // 时间查询
        long startTime = DateUtils.getAssignMonthStartTime(month);
        BmobQuery<Order> q1 = new BmobQuery<Order>();
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(new Date(startTime)));
        ands.add(q1);
        long endTime = DateUtils.getAssignMonthEndTime(month);
        BmobQuery<Order> q2 = new BmobQuery<Order>();
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(new Date(endTime)));
        ands.add(q2);
        mainQuery.and(ands);
//        mainQuery.order("-createdAt");//降序排列
        mainQuery.addWhereEqualTo("state", 4);// 统计已经完成交易的订单
        mainQuery.groupby(new String[]{"user"});// 以用户进行分组;
        mainQuery.setHasGroupCount(true);// 返回分组个数
        mainQuery.findStatistics(context, Order.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                listener.onSuccess(o);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
            }
        });

    }
}
