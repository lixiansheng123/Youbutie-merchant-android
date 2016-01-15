package com.yuedong.youbutie_merchant_android;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.umeng.update.UmengUpdateAgent;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.fragment.ClientManagetFm;
import com.yuedong.youbutie_merchant_android.fragment.CountAnalyzeFm;
import com.yuedong.youbutie_merchant_android.fragment.MerchantManagerFm;
import com.yuedong.youbutie_merchant_android.fragment.OrderManagerFm;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.UmengFeedbackAgent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.receive.BDPushReceiver;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.view.HomeBarSpanView;

import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity implements HomeBarSpanView.OnBottomBarClickListener {
    private HomeBarSpanView[] homeBarSpanViews = new HomeBarSpanView[4];
    private OrderManagerFm orderManagerFm;
    private ClientManagetFm clientManagetFm;
    private MerchantManagerFm merchantManagerFm;
    private CountAnalyzeFm countAnalyzeFm;
    private Bundle savedInstanceState;
    private boolean isStartPush;
    private BDPushReceiver bdPushBindReceive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setShowContentView(R.layout.activity_main);
        init();
        registerDBPushBindReceive();
    }

    private void init() {
        // 其他网络环境下进行更新自动提醒
//        UmengUpdateAgent.setUpdateOnlyWifi(false);
        // 检查更新
        UmengUpdateAgent.update(this);
        // umeng开发者回复用户反馈 通知提醒用户
        UmengFeedbackAgent.getInstance(context).sync();
        // 百度推送启动云推送
        PushSettings.enableDebugMode(context, true); // 打开调试模式
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Constants.APIKEY_PUSH_BAIDU);
    }


    private void ititFragment() {
        countAnalyzeFm = new CountAnalyzeFm();
        orderManagerFm = new OrderManagerFm();
        clientManagetFm = new ClientManagetFm();
        merchantManagerFm = new MerchantManagerFm();
    }

    private void loadDefaultFm() {
        addFragment(orderManagerFm, R.id.id_container, false, Constants.ORDERMANAGER_FM_TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRecyle) {
            homeBarSpanViews[chooseIndex].changeStyle(true);
            resetBottomBarBackground(homeBarSpanViews[chooseIndex]);
            onClick(homeBarSpanViews[chooseIndex]);
        }
        if (!isStartPush && App.getInstance().isLogin()) {
            L.d("MainActivity-startWork");
            // 开启百度推送
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    Constants.APIKEY_PUSH_BAIDU);
            isStartPush = true;
        }

        // 更新订单管理信息
        if (App.getInstance().orderInfoChange && (orderManagerFm != null && orderManagerFm.initFinshed)) {
            orderManagerFm.refreshTotalChildFm();
            App.getInstance().orderInfoChange = false;
        }

        if (App.getInstance().meMerchantInfoChange && (merchantManagerFm != null && merchantManagerFm.initFinshed)) {
            merchantManagerFm.ui();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterDBPushBindReceive();
    }

    private void unRegisterDBPushBindReceive() {
        unregisterReceiver(bdPushBindReceive);
        bdPushBindReceive = null;
    }

    private void registerDBPushBindReceive() {
        bdPushBindReceive = new BDPushReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_DB_PUSH_ONBIND_NOTIFY);
        registerReceiver(bdPushBindReceive, intentFilter);

    }


    @Override
    protected void initViews() {
        homeBarSpanViews[0] = fvById(R.id.id_home_order_manager);
        homeBarSpanViews[1] = fvById(R.id.id_home_customer_manager);
        homeBarSpanViews[2] = fvById(R.id.id_home_merchant_manager);
        homeBarSpanViews[3] = fvById(R.id.id_home_count);

    }

    @Override
    protected void initEvents() {
        for (HomeBarSpanView bar : homeBarSpanViews) {
            bar.setOnBottomBarClickListener(this);
        }
    }

    @Override
    protected void ui() {
        ititFragment();
        if (savedInstanceState == null)
            loadDefaultFm();
    }

    private boolean isRecyle;
    private int chooseIndex;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 这个activity recyle被回收了
        outState.putBoolean("isRecyle", true);
        outState.putInt("index", chooseIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isRecyle = savedInstanceState.getBoolean("isRecyle");
        chooseIndex = savedInstanceState.getInt("index");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (view instanceof HomeBarSpanView)
            resetBottomBarBackground(view);
        switch (viewId) {
            case R.id.id_home_order_manager:
                chooseIndex = 0;
                switchContent(mDisplayContext, orderManagerFm, R.id.id_container);
                break;

            case R.id.id_home_customer_manager:
                chooseIndex = 1;
                switchContent(mDisplayContext, clientManagetFm, R.id.id_container);
                break;

            case R.id.id_home_merchant_manager:
                switchContent(mDisplayContext, merchantManagerFm, R.id.id_container);
                chooseIndex = 2;
                break;

            case R.id.id_home_count:
                chooseIndex = 3;
                switchContent(mDisplayContext, countAnalyzeFm, R.id.id_container);
                break;
        }
    }

    @Override
    protected Fragment getDefaultFrag() {
        return orderManagerFm;
    }

    /**
     * 重置底部栏状态
     *
     * @param v
     */
    public void resetBottomBarBackground(View v) {
        for (HomeBarSpanView bar : homeBarSpanViews) {
            if (bar != v)
                bar.resetBackground();
        }
    }


    /**
     * 提交推送标识符和设备类型
     */
    private void submitChannelIdAndDeviceType(String channelId) {
        User curUser = App.getInstance().getUser();
        User updateUser = new User();
        updateUser.setDeviceType(User.DEVICE_ANDROID);
        updateUser.setChannelId(channelId);
        updateUser.update(context, curUser.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                L.d("submitChannelIdAndDeviceType-onSucceed");
                App.getInstance().appIsStart = true;
            }

            @Override
            public void onFailure(int i, String s) {
                L.d("submitChannelIdAndDeviceType-onFailure");
                // 下次再进行提交channelId
                App.getInstance().appIsStart = false;
            }
        });
    }


}
