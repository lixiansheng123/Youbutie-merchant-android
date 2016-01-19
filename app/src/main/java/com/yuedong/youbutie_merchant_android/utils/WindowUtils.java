package com.yuedong.youbutie_merchant_android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuedong.youbutie_merchant_android.R;

public class WindowUtils {
    /**
     * 获取手机的宽高
     *
     * @param context
     */

    public static Integer[] getPhoneWH(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width2 = dm.widthPixels;
        int height2 = dm.heightPixels;
        // PhoneWH wh = new PhoneWH(width2, height2);
        Integer[] it = new Integer[2];
        it[0] = width2;
        it[1] = height2;
        return it;
    }

    ;

    /**
     * 判断是否为平板 好像有点问题最好别用咯
     *
     * @return
     */
    public static boolean isPad(Context con) {
        WindowManager wm = (WindowManager) con
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是平板
     */
    public static boolean isPad2(Context context) {
        return (context.getResources().getConfiguration().screenLayout & //
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideSystemBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 給状态设置背景 对于5.0以上的手机效果较好· 4.4以上有渐变  4.4以下不支持
     *
     * @param activity
     * @param statusBarBg
     */
    public static void setStatusBarColor(Activity activity, int statusBarBg) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(activity, true);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // statusBar
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(statusBarBg);
            systemBarTransparent(activity);
        }
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);
    }

    /**
     * 让状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void systemBarTransparent(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
//        window.setNavigationBarColor(Color.TRANSPARENT);

    }

}
