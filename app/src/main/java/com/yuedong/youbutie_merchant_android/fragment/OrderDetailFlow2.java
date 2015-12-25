package com.yuedong.youbutie_merchant_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.ServiceEvaluateActivity;
import com.yuedong.youbutie_merchant_android.adapter.OrderDetailMerchantServiceContentAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.IUserEvaluateEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Appraise;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.impl.UserEvaluateEventImpl;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.SupportScrollConflictGridView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 订单详情流程1 包含已经下单和商家接单
 */
public class OrderDetailFlow2 extends BaseFragment {
    private TextView orderNumber, downOrderTime, payTime, payWay, orderStatus, orderTotalMoney, evaluateContent;
    private RatingBar clientRb;
    private SupportScrollConflictGridView gridView;
    private Order order;
    private Merchant merchant;
    private IUserEvaluateEvent<Appraise> evaluateEvent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        evaluateEvent = new UserEvaluateEventImpl();
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
        findOrderEvaluate();


    }

    /**
     * 获取订单评价
     */
    private void findOrderEvaluate() {
        evaluateEvent.findUserEvaluateByOrder(order.getObjectId(), new FindListener<Appraise>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<Appraise> list) {
                L.i("findUserEvaluateByOrder:" + list.toString());
                if (CommonUtils.listIsNotNull(list)) {
                    // 已经评价了
                    Appraise appraise = list.get(0);
                    changeAlreadyEvaluateUi(appraise);
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    private void changeAlreadyEvaluateUi(Appraise appraise) {
        ViewUtils.showLayout(clientRb);
        AppUtils.setGrade(appraise.getStar(), clientRb);
        evaluateContent.setText(appraise.getContent());
    }

    private void ui() {
        order = (Order) getArguments().getSerializable(Constants.KEY_BEAN);
        orderNumber.setText(order.getOrderNumber());
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
//        List<ServiceInfoDetailBean> serviceInfoDetailBeans = DataUtils.parseJSONToServiceInfoDatailBean(orderServices);
        if (orderServices != null) {
            OrderDetailMerchantServiceContentAdapter adapter = new OrderDetailMerchantServiceContentAdapter(getActivity(), orderServices);
            gridView.setAdapter(adapter);
        }

    }

    @Override
    public void initEvents() {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUESTCODE_ORDER_EVALUATE && resultCode == Constants.RESULT_ORDER_EVALUATE && data != null) {
            Appraise appraise = (Appraise) data.getSerializableExtra(Constants.KEY_BEAN);
            changeAlreadyEvaluateUi(appraise);

        }
    }
}
