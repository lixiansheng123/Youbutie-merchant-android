package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by Administrator on 2015/12/1.
 *
 * @author 俊鹏
 */
public class ActivityTaskUtils {
    private static ActivityTaskUtils instance;
    private LinkedList<Activity> activities = new LinkedList<Activity>();

    public static ActivityTaskUtils getInstance() {
        if (instance == null) {
            synchronized (ActivityTaskUtils.class) {
                if (instance == null) {
                    instance = new ActivityTaskUtils();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activity != null) {
            if (!activities.contains(activity))
                activities.add(activity);
        }
    }

    public boolean reomveActivity(Activity activity) {
        if (activity != null) {
            if (activities.contains(activity)) {
                return activities.remove(activity);
            }
        }
        return false;
    }

    public void delActivity(Activity activity) {
        if (reomveActivity(activity))
            activity.finish();
    }

    public boolean delLasActivity() {
        Activity last = getLasActivity();
        if (last != null) {
            last.finish();
            return activities.remove(last);
        }
        return false;
    }

    public Activity getLasActivity() {
        if (!activities.isEmpty()) {
            return activities.getLast();
        }
        return null;
    }

    public void exit() {
        for (Activity activity : activities) {
            if (activity != null)
                activity.finish();
            activities.remove(activity);
        }
        System.exit(0);
    }
}
