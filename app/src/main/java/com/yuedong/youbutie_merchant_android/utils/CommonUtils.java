package com.yuedong.youbutie_merchant_android.utils;

import java.util.Date;
import java.util.List;

public class CommonUtils {

    public static boolean listIsNotNull(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean objIsNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 获取订单号
     *
     * @param userObjectId
     * @return
     */
    public static String getOrderNumber(String userObjectId) {
        if (StringUtil.isNotEmpty(userObjectId)) {
            String o1 = DateUtils.formatDate(new Date(), DateUtils.DATETIMEyyyyMMddHHmmss);
            return userObjectId + o1;
        }
        return "";
    }
}
