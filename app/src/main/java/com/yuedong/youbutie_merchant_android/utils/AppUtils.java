package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;


import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        L.d("比较的user:" + user.toString());
        if (CommonUtils.listIsNotNull(merchantVipsList)) {
            for (Vips vips : merchantVipsList) {
                User vipUser = vips.getUser();
                L.d("被比较的vipuser:" + vipUser.toString());
                if (user.getObjectId().equals(vipUser.getObjectId()))
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

    // 未注册用户头像随机色
    private static int[] userBgs = new int[]{R.drawable.bg_circle_yellow, R.drawable.bg_circle_green, R.drawable.bg_circle_blue, R.drawable.bg_circle_red};
    private static Random random = new Random();

    /**
     * 随机获取未注册通讯录用户的头像
     *
     * @return
     */
    public static int randomGetAddressBookUnRegistFriendHead() {
        return userBgs[random.nextInt(4)];
    }

    /**
     * 获取serviceContent应该显示的样式
     *
     * @param serviceName
     * @return
     */
    public static Integer[] getServiceInfoDisplayStyle(String serviceName) {
        // 第一个是背景 第二个icon
        Integer[] displayStyle = new Integer[2];
        serviceName = serviceName.trim();
        if (_equals(R.string.str_car_wash, serviceName)) {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_xiche;
        } else if (_equals(R.string.str_wax, serviceName)) {
            displayStyle[0] = getColor(R.color.purplece67f4);
            displayStyle[1] = R.drawable.icon_service_dala;
        } else if (_equals(R.string.str_pad_pasting, serviceName)) {
            displayStyle[0] = getColor(R.color.green89c9ae);
            displayStyle[1] = R.drawable.icon_service_tiemo;
        } else if (_equals(R.string.str_tyre, serviceName)) {
            displayStyle[0] = getColor(R.color.blue8781f0);
            displayStyle[1] = R.drawable.icon_service_luntai;
        } else if (_equals(R.string.str_clean_trim, serviceName)) {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_neishiqingxi;
        } else if (_equals(R.string.str_pan_bottom, serviceName)) {
            displayStyle[0] = getColor(R.color.purplece67f4);
            displayStyle[1] = R.drawable.icon_service_dipanzhuangjia;

        } else if (_equals(R.string.str_dujing, serviceName)) {
            displayStyle[0] = getColor(R.color.green45cdfd);
            displayStyle[1] = R.drawable.icon_service_dujing;
        } else if (_equals(R.string.str_clean_air_condition, serviceName)) {
            displayStyle[0] = getColor(R.color.green89c9ae);
            displayStyle[1] = R.drawable.icon_service_kongtiaoqingxi;

        } else if (_equals(R.string.str_lacquer, serviceName)) {
            displayStyle[0] = getColor(R.color.blue8781f0);
            displayStyle[1] = R.drawable.icon_service_banjinpenqi;

        } else if (_equals(R.string.str_wheel_location, serviceName)) {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_silundingwei;

        } else if (_equals(R.string.str_refuel, serviceName)) {
            displayStyle[0] = getColor(R.color.green45cdfd);
            displayStyle[1] = R.drawable.icon_service_jiayou;

        } else if (_equals(R.string.str_brake_disc, serviceName)) {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_shachepapan;

        } else if (_equals(R.string.str_windshield_wiper, serviceName)) {
            displayStyle[0] = getColor(R.color.blue8781f0);
            displayStyle[1] = R.drawable.icon_service_yushua;

        } else if (_equals(R.string.str_accumulator, serviceName)) {
            displayStyle[0] = getColor(R.color.purplece67f4);
            displayStyle[1] = R.drawable.icon_service_xudianchi;

        } else if (_equals(R.string.str_ignition_plug, serviceName)) {
            displayStyle[0] = getColor(R.color.green45cdfd);
            displayStyle[1] = R.drawable.icon_service_huohuasai;

        } else if (_equals(R.string.str_antifreezing_solution, serviceName)) {
            displayStyle[0] = getColor(R.color.green89c9ae);
            displayStyle[1] = R.drawable.icon_service_fadongji;

        } else if (_equals(R.string.str_dynamo_clean, serviceName)) {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_fangdongye;


        } else if (_equals(R.string.str_restrictor, serviceName)) {
            displayStyle[0] = getColor(R.color.purplece67f4);
            displayStyle[1] = R.drawable.icon_service_jieqimen;
        } else {
            displayStyle[0] = getColor(R.color.yellowf8a815);
            displayStyle[1] = R.drawable.icon_service_default;
        }


        return displayStyle;
    }

    private static int getColor(int resId) {
        return App.getInstance().getResources().getColor(resId);
    }

    private static boolean _equals(int fromResId, String to) {
        return ResourceUtils.getString(fromResId).equals(to);
    }

    /**
     * 请求结果是否正常
     *
     * @param json
     * @return
     */
    public static boolean requestIsOk(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject stateJob = jsonObject.getJSONObject("state");
            String code = stateJob.getString("code");
            if (Constants.OK.equals(code))
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 因为兑换号是由userObject加兑换的时间戳精确到毫秒,显示的时候把userObject去掉
     */
    public static String getExchangeNumber(String exchangeNumber, String userObject) {
        L.d("兑换号:" + exchangeNumber + "==userObject:" + userObject);
        if (exchangeNumber.contains(userObject)) {
            int lastIndexOf = exchangeNumber.lastIndexOf(userObject);
            exchangeNumber = exchangeNumber.substring(lastIndexOf + userObject.length(), exchangeNumber.length());
            L.d("截取的兑换号:" + exchangeNumber);
        }
        return exchangeNumber;
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("getMetaValue", "error " + e.getMessage());
        }
        return apiKey;
    }

}
