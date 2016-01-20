package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.OrderDetailMerchantServiceContentAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.SupportScrollConflictGridView;

import java.util.List;

/**
 * 订单详情流程1 包含已经下单和商家接单
 */
public class OrderDetailFlow2 extends BaseFragment {
    private TextView orderNumber, downOrderTime, payTime, payWay, orderStatus, orderTotalMoney, evaluateContent;
    private RatingBar clientRb;
    private SupportScrollConflictGridView gridView;
    private Order order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_order_detail_flow2, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        evaluateContent = fvById(R.id.id_evaluate_cotent);
        orderNumber = fvById(R.id.id_order_number);
        downOrderTime = fvById(R.id.id_down_order_time);
        payTime = fvById(R.id.id_pay_time);
        payWay = fvById(R.id.id_pay_way);
        orderStatus = fvById(R.id.id_pay_status);
        orderTotalMoney = fvById(R.id.id_order_total_money);
        gridView = fvById(R.id.id_service_content_list);
        clientRb = fvById(R.id.id_me_evaluate);
        ui();
    }


    private void changeAlreadyEvaluateUi(String content, float star) {
        ViewUtils.showLayout(clientRb);
        AppUtils.setGrade(star, clientRb);
        evaluateContent.setText(content);
    }

    private void ui() {
        order = (Order) getArguments().getSerializable(Constants.KEY_BEAN);
        orderNumber.setText(AppUtils.getExchangeNumber(order.getOrderNumber(), App.getInstance().getUser().getObjectId()));
        downOrderTime.setText(order.getOrderTime().getDate());
        payTime.setText(order.getUpdatedAt());
        orderStatus.setText("已支付");
        orderTotalMoney.setText("￥" + order.getPrice());
        int orderPayWay = order.getPayWay();
        if (orderPayWay != 0) {
            if (orderPayWay == 1) {
                payWay.setText("支付宝支付");
            } else if (orderPayWay == 2) {
                payWay.setText("微信支付 ");
            }
        } else {
            payWay.setText("未支付");
        }

        // 解析服务信息
        List<ServiceInfoDetailBean> orderServices = order.getServices();
        if (orderServices != null) {
            OrderDetailMerchantServiceContentAdapter adapter = new OrderDetailMerchantServiceContentAdapter(getActivity(), orderServices);
            gridView.setAdapter(adapter);
        }
        String content = order.getContent();
        if (TextUtils.isEmpty(content)) return;
        float star = order.getStar();
        changeAlreadyEvaluateUi(content, star);

    }

    @Override
    public void initEvents() {
    }

}
