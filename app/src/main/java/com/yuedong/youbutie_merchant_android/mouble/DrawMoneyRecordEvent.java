package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.DrawMoneyRecord;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;

/**
 * Created by Administrator on 2016/1/8.
 */
public class DrawMoneyRecordEvent implements BaseEvent {

    private static DrawMoneyRecordEvent INSTANCE;

    private DrawMoneyRecordEvent() {
    }

    public static DrawMoneyRecordEvent getInstance() {
        if (INSTANCE == null) {
            synchronized (DrawMoneyRecordEvent.context) {
                if (INSTANCE == null) {
                    INSTANCE = new DrawMoneyRecordEvent();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 查询当日提现次数
     */
    public void queryCurDayWithdrawCount(String userObjectId, final CountListener listener) {
        listener.onStart();
        BmobQuery<DrawMoneyRecord> query = new BmobQuery<DrawMoneyRecord>();
        List<BmobQuery<DrawMoneyRecord>> ands = new ArrayList<BmobQuery<DrawMoneyRecord>>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        // 是指定用户的记录
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userObjectId);
        query.addWhereMatchesQuery("user", "_User", userBmobQuery);
        Date startDate = new Date(DateUtils.getCurDayStartTime());
        BmobQuery<DrawMoneyRecord> q1 = new BmobQuery<DrawMoneyRecord>();
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(startDate));
        ands.add(q1);
        Date endDate = new Date(DateUtils.getCurDayEndTime());
        BmobQuery<DrawMoneyRecord> q2 = new BmobQuery<DrawMoneyRecord>();
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(endDate));
        // 是当天的记录
        ands.add(q2);
        query.and(ands);
        query.count(context, DrawMoneyRecord.class, new CountListener() {
            @Override
            public void onSuccess(int i) {
                listener.onSuccess(i);
                listener.onFinish();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });
    }
}
