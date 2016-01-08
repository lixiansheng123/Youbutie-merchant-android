package com.yuedong.youbutie_merchant_android.utils;

import com.yuedong.youbutie_merchant_android.bean.MoneyContributionBean;

import java.util.Comparator;

public class RankingComparator implements Comparator<MoneyContributionBean> {
    @Override
    public int compare(MoneyContributionBean lhs, MoneyContributionBean rhs) {
        int flag = lhs.getTotalContributionMoney() - rhs.getTotalContributionMoney();
        if (flag > 0)
            // 排前面
            return -1;
        else if (flag < 0)
            // 排后面
            return 1;
        else
            return 0;
    }
}
