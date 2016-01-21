package com.yuedong.youbutie_merchant_android.mouble;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.Button;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;

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
     * 获取用户根据id
     *
     * @param userId
     * @param listener
     */
    public void findUserById(String userId, final GetListener<User> listener) {
        listener.onStart();
        BmobQuery<User> userBmobQuery = new BmobQuery<User>();
        userBmobQuery.getObject(context, userId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                listener.onSuccess(user);
                listener.onFinish();
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });
    }

    /**
     * 同步user根据idl
     */
    public void pullUserById(final String userId, final UpdateListener listener) {
        listener.onStart();
        findUserById(userId, new GetListener<User>() {
            @Override
            public void onSuccess(User user) {
                User updateUser = new User();
                updateUser.setCash(user.getCash());
                updateUser.setNickname(user.getNickname());
                updateUser.setDrawCount(user.getDrawCount());
                updateUser.setPhoto(user.getPhoto());
                updateUser.setMobilePhoneNumber(user.getMobilePhoneNumber());
                updateUser.setAddress(user.getAddress());
                updateUser.setDrawTotalCash(user.getDrawTotalCash());
                updateUser.setAge(user.getAge());
                updateUser.setBankCard(user.getBankCard());
                updateUser.setCarNumber(user.getCarNumber());
                updateUser.setCarString(user.getCarString());
                updateUser.setChannelId(user.getChannelId());
                updateUser.setIdnumber(user.getIdnumber());
                updateUser.setDeviceType(user.getDeviceType());
                updateUser.setLocation(user.getLocation());
                updateUser.setStrokeLength(user.getStrokeLength());
                updateUser.setType(user.getType());
                updateUser.setVIN(user.getVIN());
                updateUser.setTotalMoney(user.getTotalMoney());
                updateUser.update(context, userId, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        listener.onSuccess();
                        listener.onFinish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        listener.onFailure(i, s);
                        listener.onFinish();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure(i, s);
                listener.onFinish();
            }
        });

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
