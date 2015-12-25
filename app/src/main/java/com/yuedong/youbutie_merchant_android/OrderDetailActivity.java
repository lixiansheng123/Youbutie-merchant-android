package com.yuedong.youbutie_merchant_android;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.fragment.OrderDetailFlow1;
import com.yuedong.youbutie_merchant_android.fragment.OrderDetailFlow2;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

public class OrderDetailActivity extends BaseActivity implements OrderDetailFlow1.IOrderBizListener {
    private static final String TAG = "OrderDetailActivity";
    private OrderDetailFlow1 flow1Fm;
    private OrderDetailFlow2 flow2Fm;
    private ImageView iconDownOrder, iconMerchantReceiveOrder, iconServiceFinished, iconGetCar;
    private TextView textDownOrder, textMerchantReceiveOrder, textServiceFinished, textGetCar;
    private View line1, line2, line3;
    private Dialog dialog;
    private Order orderBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("订单详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        }));
        setShowContentView(R.layout.activity_order_detail);
        initDl();
    }

    @Override
    protected void initViews() {
        L.d(TAG + "-------------initViews");
        iconDownOrder = fvById(R.id.id_icon_down_order);
        textDownOrder = fvById(R.id.id_text_down_order);
        iconMerchantReceiveOrder = fvById(R.id.id_icon_merchant_receive_order);
        textMerchantReceiveOrder = fvById(R.id.id_text_merchant_receive_order);
        iconServiceFinished = fvById(R.id.id_icon_service_finish);
        textServiceFinished = fvById(R.id.id_text_service_finish);
        iconGetCar = fvById(R.id.id_icon_get_car);
        textGetCar = fvById(R.id.id_text_get_car);
        line1 = fvById(R.id.id_line1);
        line2 = fvById(R.id.id_line2);
        line3 = fvById(R.id.id_line3);
    }

    @Override
    protected void initEvents() {
        L.d(TAG + "-------------initEvents");
    }

    @Override
    protected void ui() {
        L.d(TAG + "-------------ui");
        Bundle bundle = getIntent().getExtras();
        orderBean = (Order) bundle.getSerializable(Constants.KEY_BEAN);

        int state = orderBean.getState();
        switch (state) {
            case 1:
            case 2:
            case 3:
            case 5:
                flow1Fm = new OrderDetailFlow1();
                flow1Fm.setArguments(bundle);
                addFragment(flow1Fm, R.id.container, false, null);
                break;

            case 4:
                flow2Fm = new OrderDetailFlow2();
                flow2Fm.setArguments(bundle);
                addFragment(flow2Fm, R.id.container, false, null);
                break;
        }

        viewProgress(state);
    }

    private void initDl() {
        dialog = new Dialog(context);
        View contentView = ViewUtils.inflaterView(context, R.layout.dialog_down_order_tips);
        TextView tips = (TextView) contentView.findViewById(R.id.id_tips);
        Button see = (Button) contentView.findViewById(R.id.id_btn_see);
        ImageView close = (ImageView) contentView.findViewById(R.id.id_close_window);
        RelativeLayout.LayoutParams tipRlp = (RelativeLayout.LayoutParams) tips.getLayoutParams();
        tipRlp.topMargin = ViewUtils.getViewDisplaySize(140, ViewUtils.ViewEnum.H);
        RelativeLayout.LayoutParams btnRlp = (RelativeLayout.LayoutParams) see.getLayoutParams();
        btnRlp.bottomMargin = ViewUtils.getViewDisplaySize(90, ViewUtils.ViewEnum.H);
        dialog.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewUtils.getViewDisplaySize(845, ViewUtils.ViewEnum.W), ViewUtils.getViewDisplaySize(425, ViewUtils.ViewEnum.H)));
//        dialog.show();
    }


    /**
     * 视图进度
     *
     * @param progress
     */
    private void viewProgress(int progress) {
        int yellowColor = Color.parseColor("#f0c010");
        switch (progress) {
            case 1:
                progress1(yellowColor);
                break;
            case 2:
                progress1(yellowColor);
                progress2(yellowColor);
                break;
            case 3:
                progress1(yellowColor);
                progress2(yellowColor);
                progress3(yellowColor);
                break;
            case 4:
                progress1(yellowColor);
                progress2(yellowColor);
                progress3(yellowColor);
                progress4(yellowColor);
                break;
        }

    }

    private void progress1(int color) {
        iconDownOrder.setImageResource(R.drawable.icon_down_order_select);
        textDownOrder.setTextColor(color);
    }

    private void progress2(int color) {
        iconMerchantReceiveOrder.setImageResource(R.drawable.icon_merchant_receive_order_select);
        textMerchantReceiveOrder.setTextColor(color);
        line1.setBackgroundColor(color);
    }

    private void progress3(int color) {
        iconServiceFinished.setImageResource(R.drawable.icon_service_finish_select);
        textServiceFinished.setTextColor(color);
        line2.setBackgroundColor(color);
    }

    private void progress4(int color) {
        iconGetCar.setImageResource(R.drawable.icon_get_car_select);
        textGetCar.setTextColor(color);
        line3.setBackgroundColor(color);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        defaultFinished();
    }

    @Override
    public void goCollection() {
        L.d("goCollection----------------------");
//        Intent intent = new Intent();
//        intent.putExtra(Constants.KEY_BEAN, orderBean);
//        setResult(Constants.RESULT_COLLECTION, intent);
//        defaultFinished();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_BEAN,orderBean);
        LaunchWithExitUtils.startActivity(activity, MerchantCollectionActivity.class,bundle);
    }

    @Override
    public void receiveOrder() {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_BEAN, orderBean);
        setResult(Constants.RESULT_RECEIVE_ORDER, intent);
        defaultFinished();
    }
}
