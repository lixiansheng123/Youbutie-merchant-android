package com.yuedong.youbutie_merchant_android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


/**
 * Created by Administrator on 2015/11/28.
 */
public class LaunchWithExitUtils {
    private LaunchWithExitUtils() {
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
//        pagerAnimation(context);
    }

    public static void startActivity(Activity context, Class<? extends Activity> cls) {
        startActivity(context, cls, null);

    }

    public static void startActivity(Activity context, Class<? extends Activity> cls, Bundle data) {
        Intent intent = new Intent(context, cls);
        if (data != null)
            intent.putExtras(data);
        context.startActivity(intent);
//        pagerAnimation(context);
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
//        pagerAnimation(fragment.getActivity());
    }


    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> cls, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        fragment.startActivityForResult(intent, requestCode);
//        pagerAnimation(fragment.getActivity());
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> cls, int requestCode) {
        startActivityForResult(activity, cls, requestCode, null);
    }


    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
//        pagerAnimation(activity);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityForResult(Activity context, Class<? extends Activity> cls, int requestCode, Bundle data) {
        Intent intent = new Intent(context, cls);
        if (data != null)
            context.startActivityForResult(intent, requestCode, data);
        else
            context.startActivityForResult(intent, requestCode);
//        pagerAnimation(context);
    }

    public static void back(FragmentActivity act) {
        if (act.getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            act.finish();
        } else {
            act.getSupportFragmentManager().popBackStack();
        }
//        pagerAnimation2(act);
    }

//    private static void pagerAnimation2(Activity activity) {
//
//    }


//    private static void pagerAnimation(Activity activity) {
//        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }

}
