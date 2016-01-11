package com.yuedong.youbutie_merchant_android.mouble;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * Created by Administrator on 2015/12/31.
 */
public class UserEvent implements BaseEvent {
    private static UserEvent INSTANCE;

    private UserEvent() {
    }

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

    public void requestSms(final Activity activity, final Button btn, String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.matches(Config.REGEX_TEL)) {
            BmobSMS.requestSMSCode(context, phoneNumber, "regist", new RequestSMSCodeListener() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e == null) {
                        T.showShort(context, "发送短信成功,请留意短信");
                        AppUtils.startCountDown(activity, btn, Color.parseColor("#ffffff"), R.color.grey938381, Color.parseColor("#ffffff"), R.color.yellowf0c008);
                    } else {
                        T.showShort(context, "发送短信失败:" + e.toString());
                    }
                }
            });
        } else {
            T.showShort(context, "手机号码不合法,请检查");
        }
    }
}
