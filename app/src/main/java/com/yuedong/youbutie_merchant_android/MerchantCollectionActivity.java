package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.adapter.CollectionMerchantServiceAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.TextUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MerchantCollectionActivity extends BaseActivity {
    private TextView downOrderUser, downOrderTime;
    private EditText inputServiceTotalMoney;
    private GridView gvMerchantService;
    private NetworkImageView downOrderUserPic;
    private CollectionMerchantServiceAdapter adapter;
    private Button confirmCollectionBtn;
    private List<ServiceInfoDetailBean> selectService;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("收款"));
        setShowContentView(R.layout.activity_merchant_collection);
    }

    @Override
    protected void initViews() {
        confirmCollectionBtn = fvById(R.id.id_btn_collection);
        downOrderUser = fvById(R.id.id_user_name);
        downOrderUserPic = fvById(R.id.id_user_pic);
        downOrderTime = fvById(R.id.id_down_order_time);
        inputServiceTotalMoney = fvById(R.id.id_input_box);
        gvMerchantService = fvById(R.id.id_merchant_service);

    }

    @Override
    protected void initEvents() {
        inputServiceTotalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputContent = s.toString();
                if (StringUtil.isNotEmpty(inputContent)) {
                    if (inputContent.indexOf("￥") == -1) {
                        String value = "￥" + inputContent;
                        inputServiceTotalMoney.setText(value);
                        inputServiceTotalMoney.setSelection(value.length());
                    }
                }

                btnStatus();

            }
        });
        confirmCollectionBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String price = TextUtils.getText(inputServiceTotalMoney);
                                                        Double priceI = Double.parseDouble(price.replace("￥", "").trim());
                                                        //1更改订单状态
                                                        dialogStatus(true);
                                                        Order updateOrder = new Order();
                                                        updateOrder.setState(3);
                                                        updateOrder.setServices(selectService);
                                                        updateOrder.setPrice(priceI);
                                                        updateOrder.update(activity, order.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                dialogStatus(false);
                                                                //2刷新订单管理data并刷新ui
                                                                App.getInstance().orderInfoChange = true;
//                                                                defaultFinished();
                                                                LaunchWithExitUtils.startActivity(activity, MainActivity.class);
                                                            }


                                                            @Override
                                                            public void onFailure(int i, String s) {
                                                                dialogStatus(false);
                                                                error(s);
                                                            }
                                                        });

                                                    }
                                                }

        );
    }

    @Override
    protected void ui() {
        confirmCollectionBtn.setClickable(false);
        order = (Order) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        User orderUser = order.getUser();
        downOrderUser.setText(orderUser.getNickname());
        DisplayImageByVolleyUtils.loadImage(downOrderUserPic, orderUser.getPhoto());
        downOrderTime.setText(order.getOrderTime().getDate());
        MerchantEvent.getInstance().findMeMetchant(Constants.TEST_USER_ID, new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                if (CommonUtils.listIsNotNull(list)) {
                    Merchant merchant = list.get(0);
                    adapter = new CollectionMerchantServiceAdapter(context, merchant.getServiceInfo());
                    gvMerchantService.setAdapter(adapter);
                    adapter.setItemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnStatus();
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    private void btnStatus() {
        // 通过输入内容和选择服务来确定btn是否可以响应事件
        String inputContent = inputServiceTotalMoney.getText().toString();
        if (adapter != null) {
            selectService = adapter.getSelect();
            if (CommonUtils.listIsNotNull(selectService) && StringUtil.isNotEmpty(inputContent)) {
                confirmCollectionBtn.setClickable(true);
                confirmCollectionBtn.setBackgroundResource(R.drawable.bg_round_yellow);
            } else {
                confirmCollectionBtn.setClickable(false);
                confirmCollectionBtn.setBackgroundResource(R.drawable.bg_round_grey);
            }
        } else {
            confirmCollectionBtn.setClickable(false);
            confirmCollectionBtn.setBackgroundResource(R.drawable.bg_round_grey);
        }
    }
}
