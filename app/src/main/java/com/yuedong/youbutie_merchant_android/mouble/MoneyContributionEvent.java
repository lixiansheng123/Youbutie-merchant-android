package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.MoneyContribute;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * Created by Administrator on 2016/1/7.
 */
public class MoneyContributionEvent implements BaseEvent {
    private static MoneyContributionEvent INSTANCE;

    public static MoneyContributionEvent getInstance() {
        if (INSTANCE == null)
            synchronized (MoneyContributionEvent.class) {
                if (INSTANCE == null)
                    INSTANCE = new MoneyContributionEvent();
            }
        return INSTANCE;
    }

    /**
     * 获取门店油点贡献
     */
    public void getMerchantMoneyContribution(String merchantObjectId, FindStatisticsListener listener) {
        BmobQuery<MoneyContribute> moneyContributionEventBmobQuery = new BmobQuery<MoneyContribute>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        moneyContributionEventBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        moneyContributionEventBmobQuery.sum(new String[]{"money"});
        moneyContributionEventBmobQuery.findStatistics(context, MoneyContribute.class, listener);
    }

    /**
     * 获取油点排行
     */
    public void getMoneyContributionRanking(int skip, int limit, String merchantObjectId, final FindStatisticsListener listener) {
        BmobQuery<MoneyContribute> moneyContributeBmobQuery = new BmobQuery<MoneyContribute>();
        BmobQuery<Merchant> merchantBmobQuery = new BmobQuery<Merchant>();
        merchantBmobQuery.addWhereEqualTo(OBJECT_ID, merchantObjectId);
        moneyContributeBmobQuery.addWhereMatchesQuery("merchant", "Merchant", merchantBmobQuery);
        moneyContributeBmobQuery.sum(new String[]{"money"});
        moneyContributeBmobQuery.groupby(new String[]{"user"});
        moneyContributeBmobQuery.order("-createdAt");//降序排列
        moneyContributeBmobQuery.include("user");
        moneyContributeBmobQuery.setSkip(skip);
        moneyContributeBmobQuery.setLimit(limit);
        moneyContributeBmobQuery.findStatistics(context, MoneyContribute.class, new FindStatisticsListener() {
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
