package com.yuedong.youbutie_merchant_android.mouble;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/31.
 */
public class UserEvent implements BaseEvent {
    private static UserEvent INSTANCE;
    private UserEvent(){}
    public static UserEvent getInstance() {
        if (INSTANCE == null) {
            synchronized (UserEvent.class) {
                if (INSTANCE == null)
                    INSTANCE = new UserEvent();
            }
        }
        return INSTANCE;

    }

    /**
     * 查找大量用户根据多个手机号
     *
     * @param mobiles
     * @param listener
     */
    public void findUserByMobiles(List<String> mobiles, final FindListener<User> listener) {
        listener.onStart();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.addWhereContainedIn("mobilePhoneNumber", mobiles);
        userBmobQuery.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
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
}
