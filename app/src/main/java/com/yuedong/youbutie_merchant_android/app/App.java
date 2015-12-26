package com.yuedong.youbutie_merchant_android.app;

import android.app.Application;
import android.content.Context;

import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class App extends Application {
    private static App instance;
    private Context context;
    private Integer[] phoneWh;
    // 线程池
    private Executor executor;
    // 用户对象
    private User user;
    // 我的门店信息
    private List<Merchant> meMerchant;
    // 我的门店信息是否改变
    public boolean meMerchantInfoChange;
    // 用户信息是否变更
    public boolean userInfoChange;
    // 订单信息是否变更
    public boolean orderInfoChange;

    public void setMeMerchant(List<Merchant> meMerchant) {
        this.meMerchant = meMerchant;
    }

    public List<Merchant> getMeMerchant() {
        return meMerchant;
    }

    public Executor getExecutor() {
        return executor;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        if (userInfoChange) {
            user = null;
        }
        if (user == null)
            user = BmobUser.getCurrentUser(this, User.class);
        return user;
    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return getUser() != null;
    }


    public Integer[] getPhoneWh() {
        return phoneWh;
    }

    public static App getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        phoneWh = WindowUtils.getPhoneWH(this);
        L.i("PHONE-PARAMS", "手机屏幕参数width" + phoneWh[0] + "::height:" + phoneWh[1]);
        initBmob();
        init();
    }

    private void init() {
        executor = Executors.newCachedThreadPool();
        SPUtils.FILE_NAME = Config.SP_NAME_USER;
    }

    /**
     * 初始化bmob
     */
    private void initBmob() {
        Bmob.initialize(this, Constants.APIKEY_BMOB);
    }
}
