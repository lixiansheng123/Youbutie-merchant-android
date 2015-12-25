package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Button;
import android.widget.RatingBar;


import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yuedong.youbutie_merchant_android.app.Config;

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

}
