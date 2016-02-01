package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;

import java.util.Iterator;
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

    /**
     * 删除除了指定class之外的activity
     */
    public void delExceptAssign(Class cls) {
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            if (next != null) {
                if (next.getClass().equals(cls))
                    continue;
                iterator.remove();
                next.finish();
                next = null;
            }
        }
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
        delAll();
        System.exit(0);
    }

    public void delAll() {
        if (activities != null) {
            while (activities.size() > 0) {
                Activity activity = getLasActivity();
                if (activity == null) break;
                delActivity(activity);
            }
        }
    }
}
