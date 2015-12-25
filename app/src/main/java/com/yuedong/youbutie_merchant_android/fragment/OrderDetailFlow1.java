package com.yuedong.youbutie_merchant_android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

/**
 * 订单详情流程1 包含已经下单和商家接单
 */
public class OrderDetailFlow1 extends BaseFragment {
    private NetworkImageView pic;
    private TextView name, time, status;
    private Order orderBean;
    private Button btnCollection;
    private IOrderBizListener collectionResponse;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            collectionResponse = (IOrderBizListener) activity;
        } catch (Exception e) {
            L.d("OrderDetailFlow1 所依附的activity必须实现ICollectionResponse接口");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_order_detail_flow1, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        pic = fvById(R.id.id_main_pic);
        name = fvById(R.id.id_name);
        time = fvById(R.id.id_time);
        status = fvById(R.id.id_status);
        btnCollection = fvById(R.id.id_btn_collection);
        ui();
    }

    private void ui() {
        Bundle data = getArguments();
        orderBean = (Order) data.getSerializable(Constants.KEY_BEAN);
        Merchant merchant = orderBean.getMerchant();
        DisplayImageByVolleyUtils.loadImage(pic, merchant.getPhoto());
        name.setText(merchant.getName());
        time.setText(orderBean.getOrderTime().getDate());
        int state = orderBean.getState();
        switch (state) {
            case 1:
                status.setText(getString(R.string.str_str_already_down_order));
                ViewUtils.showLayout(btnCollection);
                btnCollection.setText(getString(R.string.str_receive_order));
                break;

            case 2:
                status.setText(getString(R.string.str_merchant_receive_order));
                ViewUtils.showLayout(btnCollection);
                break;

            case 3:
            case 5:
                status.setText("等待付款");
                break;

        }
    }

    @Override
    public void initEvents() {
        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickBtn = (Button) v;
                if (clickBtn.getText().toString().equals(getString(R.string.str_collection)))
                    // 收款
                    collectionResponse.goCollection();
                else
                    // 接单
                    collectionResponse.receiveOrder();
            }
        });
    }

    public interface IOrderBizListener {
        // 去收款
        void goCollection();

        // 接单
        void receiveOrder();
    }
}
