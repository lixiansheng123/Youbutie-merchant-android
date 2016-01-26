package com.yuedong.youbutie_merchant_android.model;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

public class MessageEvent implements BaseEvent {
    private static MessageEvent INSTANCE = new MessageEvent();

    public static MessageEvent getInstance() {
        return INSTANCE;
    }

    /**
     * 获取消息通过user
     *
     * @param userId
     * @param listener
     */
    public void findMessageByUserId(int skip, int limit, String userId, boolean filterOverdue, final FindListener<Messages> listener) {
        listener.onStart();
        BmobQuery<Messages> bmobQuery = new BmobQuery<Messages>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userId);
        bmobQuery.addWhereMatchesQuery("sender", "_User", userBmobQuery);
        bmobQuery.addWhereEqualTo("type", 2);
        bmobQuery.setSkip(skip);
        bmobQuery.setLimit(limit);
        bmobQuery.order("-createdAt");
        // 是否筛选掉过期的数据
        if (filterOverdue) {
            // 查询不过期的数据
            bmobQuery.addWhereGreaterThanOrEqualTo("endTime", new BmobDate(new Date()));
        }
        bmobQuery.findObjects(context, new FindListener<Messages>() {
            @Override
            public void onSuccess(List<Messages> list) {
                listener.onSuccess(list);
                listener.onFinish();
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
                listener.onFinish();
            }
        });
    }

    /**
     * 统计消息个数根据userId
     *
     * @param userId
     * @param listener
     */
    public void countMessaeByUserId(String userId, CountListener listener) {
        listener.onStart();
        BmobQuery<Messages> bmobQuery = new BmobQuery<Messages>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, userId);
        bmobQuery.addWhereMatchesQuery("sender", "_User", userBmobQuery);
        bmobQuery.addWhereEqualTo("type", 2);
        bmobQuery.count(context, Messages.class, listener);
    }

    /**
     * 统计这个月发出的广告数目
     */
    public void countCurMonthSendMessageNumber(final CountListener listener) {
        listener.onStart();
        BmobQuery<Messages> messagesBmobQuery = new BmobQuery<Messages>();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereEqualTo(OBJECT_ID, App.getInstance().getUser().getObjectId());
        messagesBmobQuery.addWhereMatchesQuery("sender", "_User", userBmobQuery);
        List<BmobQuery<Messages>> ands = new ArrayList<BmobQuery<Messages>>();
        BmobQuery<Messages> q1 = new BmobQuery<Messages>();
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(new Date(DateUtils.getCurMonthStartTime())));
        ands.add(q1);
        BmobQuery<Messages> q2 = new BmobQuery<Messages>();
        q2.addWhereLessThanOrEqualTo("createdAt", new BmobDate(new Date(DateUtils.getCurMonthEndTime())));
        ands.add(q2);
        q2.and(ands);
        messagesBmobQuery.count(context, Messages.class, new CountListener() {
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
