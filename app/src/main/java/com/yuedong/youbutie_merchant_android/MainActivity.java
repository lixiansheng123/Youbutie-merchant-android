package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.fragment.ClientManagetFm;
import com.yuedong.youbutie_merchant_android.fragment.MerchantManagerFm;
import com.yuedong.youbutie_merchant_android.fragment.OrderManagerFm;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.view.HomeBarSpanView;

public class MainActivity extends BaseActivity implements HomeBarSpanView.OnBottomBarClickListener {
    private HomeBarSpanView[] homeBarSpanViews = new HomeBarSpanView[4];
    private OrderManagerFm orderManagerFm;
    private ClientManagetFm clientManagetFm;
    private MerchantManagerFm merchantManagerFm;
    private Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setShowContentView(R.layout.activity_main);
    }

    private void ititFragment() {
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
                chooseIndex = 1;
                switchContent(mDisplayContext, orderManagerFm, R.id.id_container);
                break;

            case R.id.id_home_customer_manager:
                chooseIndex = 2;
                switchContent(mDisplayContext, clientManagetFm, R.id.id_container);
                break;

            case R.id.id_home_merchant_manager:
                switchContent(mDisplayContext, merchantManagerFm, R.id.id_container);
                chooseIndex = 3;
                break;

            case R.id.id_home_count:
                chooseIndex = 4;
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


}
