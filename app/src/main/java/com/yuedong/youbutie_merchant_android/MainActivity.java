package com.yuedong.youbutie_merchant_android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.SerializableMap;
import com.yuedong.youbutie_merchant_android.fragment.ClientManagetFm;
import com.yuedong.youbutie_merchant_android.fragment.CountAnalyzeFm;
import com.yuedong.youbutie_merchant_android.fragment.MerchantManagerFm;
import com.yuedong.youbutie_merchant_android.fragment.OrderManagerFm;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.UmengFeedbackAgent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.receive.BDPushReceiver;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.view.HomeBarSpanView;

import org.json.JSONObject;

import java.util.Map;

import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends BaseActivity implements HomeBarSpanView.OnBottomBarClickListener {
    private HomeBarSpanView[] homeBarSpanViews = new HomeBarSpanView[4];
    private OrderManagerFm orderManagerFm;
    private ClientManagetFm clientManagetFm;
    private MerchantManagerFm merchantManagerFm;
    private CountAnalyzeFm countAnalyzeFm;
    private Bundle savedInstanceState;
    private BDPushBindReceive bdPushBindReceive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        buildUi(null, false, false, false, R.layout.activity_main);
        ActivityTaskUtils.getInstance().delExceptAssign(MainActivity.class);
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

        UmengUpdateAgent.setUpdateAutoPopup(false);


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
        bdPushBindReceive = new BDPushBindReceive();
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

    // 注册广播接受者 App进行百度推送版定成功的时候会发广播发送到这里
    private class BDPushBindReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Bundle extras = intent.getExtras();
                SerializableMap serializableMap = (SerializableMap) extras.getSerializable(Constants.KEY_BEAN);
                Map<String, Object> map = serializableMap.getMap();
                String channelId = (String) map.get(BDPushReceiver.CHANNEL_ID);
                int errorCode = (int) map.get(BDPushReceiver.ERROR_CODE);
                if (errorCode == 0)
                    submitChannelIdAndDeviceType(channelId);
            }
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

    @Override
    protected void notifyMsg(int msgType, Map<String, Object> data, JSONObject jsonObject) {
        if (msgType == RequestYDHelper.PUSH_TYPE_DOWNORDER ||//
                msgType == RequestYDHelper.PUSH_TYPE_CLIENT_PAY_SUCCEED) {
            // 刷新订单列表
            if (orderManagerFm != null && orderManagerFm.initFinshed)
                orderManagerFm.refreshTotalChildFm();
        }
    }

    private long firstBackClickTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstBackClickTime <= 1500) {
            ActivityTaskUtils.getInstance().exit();
        } else {
            firstBackClickTime = System.currentTimeMillis();
            T.showShort(context, "您真的想退出吗 再次回退退出应用");
        }
    }

}
