package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.RatingBar;


import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 为了方便而把一些操作封装(针对的是项目)
 */
public class AppUtils {
    public static int count = 60;

    public static void startCountDown(final Activity activity, final Button btn, final int changeTextColor, final int changeBgColor, final int finalTextColor, final int finalBgColor) {
        count = 60;
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count > 0) {
                            if (count == 60) {
                                btn.setClickable(false);
                                btn.setTextColor(changeTextColor);
                                btn.setBackgroundResource(changeBgColor);
                            }
                            count--;
                            btn.setText(count + "秒");
                        } else {
                            btn.setClickable(true);
                            btn.setText("验证码");
                            btn.setTextColor(finalTextColor);
                            btn.setBackgroundResource(finalBgColor);
                            timer.cancel();
                        }

                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 联系商家
     *
     * @param context
     * @param merchantPhoto
     */
    public static void merchantCall(Context context, String merchantPhoto) {
        if (StringUtil.isNotEmpty(merchantPhoto)) {
            if (merchantPhoto.matches(Config.REGEX_NUM)) {
                SystemUtils.call(context, merchantPhoto);
            } else {
                T.showShort(context, "商家预留电话不合法");
            }
        } else {
            T.showShort(context, "商家暂无电话");
        }
    }

    /**
     * 设置评价
     *
     * @param grade
     * @param ratingBar
     */
    public static void setGrade(float grade, RatingBar ratingBar) {
        if (ratingBar != null) {
            ratingBar.setRating(Math.round(grade * 5));
        }
    }

    /**
     * 设置刷新提示文字
     *
     * @param refreshView
     */
    public static void setRefreshBaseParams(PullToRefreshBase refreshView) {
        ILoadingLayout loadingLayoutProxy = refreshView.getLoadingLayoutProxy(true, false);
        loadingLayoutProxy.setPullLabel("刷新最新信息...");
        loadingLayoutProxy.setRefreshingLabel("正在刷新最新信息...");
        loadingLayoutProxy.setReleaseLabel("松开刷新...");
    }

    /**
     * 统计门店客户人数
     *
     * @param orders
     * @return
     */
    public static int countMerchantUser(List<Order> orders) {
        int merchantUser = 0;
        if (CommonUtils.listIsNotNull(orders)) {
            List<String> objects = new ArrayList<String>();
            for (Order order : orders) {
                String objectId = order.getUser().getObjectId();
                if (!objects.contains(objectId)) {
                    merchantUser++;
                    objects.add(objectId);
                }
            }
            return merchantUser;
        }

        return 0;
    }

    /**
     * 当前用户是否是vip身份
     *
     * @return
     */
    public static boolean curUserIsVip(User user, List<Vips> merchantVipsList) {
        if (CommonUtils.listIsNotNull(merchantVipsList)) {
            for (Vips vips : merchantVipsList) {
                if (user.getObjectId().equals(vips.getUser().getObjectId()))
                    return true;
            }
        }
        return false;
    }

    /**
     * 设置商家服务(文字的情况下)
     *
     * @param list
     * @return
     */
    public static String setMerchantService(List<ServiceInfoDetailBean> list) {
        if (CommonUtils.listIsNotNull(list)) {
            StringBuilder sb = new StringBuilder();
            for (ServiceInfoDetailBean bean : list) {
                sb.append(bean.name + " ");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return "";
    }


}
