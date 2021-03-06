package com.yuedong.youbutie_merchant_android;

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
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.listener.ObtainSecretKeyListener;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.TextUtils;

import java.util.ArrayList;
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
        buildUi(new TitleViewHelper().createDefaultTitleView3("收款"),false,false,false,R.layout.activity_merchant_collection);
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
                                                        List<String> ids = new ArrayList<String>();
                                                        for (ServiceInfoDetailBean bean : selectService) {
                                                            ids.add(bean.objectId);
                                                        }
                                                        String price = TextUtils.getText(inputServiceTotalMoney);
                                                        Double priceI = Double.parseDouble(price.replace("￥", "").trim());
                                                        //1更改订单状态
                                                        dialogStatus(true);
                                                        Order updateOrder = new Order();
                                                        updateOrder.setState(6);
                                                        updateOrder.setServices(selectService);
                                                        updateOrder.setPrice(priceI);
                                                        updateOrder.setServiceIds(ids);
                                                        updateOrder.update(activity, order.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                dialogStatus(false);
                                                                //2刷新订单管理data并刷新ui
                                                                App.getInstance().orderInfoChange = true;
//                                                                defaultFinished();
                                                                LaunchWithExitUtils.startActivity(activity, MainActivity.class);
                                                                App.getInstance().getYdApiSecretKey(new ObtainSecretKeyListener() {
                                                                    @Override
                                                                    public void start() {

                                                                    }

                                                                    @Override
                                                                    public void end() {

                                                                    }

                                                                    @Override
                                                                    public void succeed(String secretKey) {
                                                                        // 通知客户端收款
                                                                        RequestYDHelper requestYDHelper = new RequestYDHelper();
                                                                        requestYDHelper.setAppSecretkey(secretKey);
                                                                        requestYDHelper.requestPushSingle(getString(R.string.str_push_merchant_collect_title)//
                                                                                , getString(R.string.str_push_merchant_collect_msg),//
                                                                                order.getUser().getObjectId(), RequestYDHelper.PUSH_TYPE_MERCHANT_COLLECTION, order.getObjectId(), "");
                                                                    }

                                                                    @Override
                                                                    public void fail(int code, String error) {

                                                                    }
                                                                });

                                                            }


                                                            @Override
                                                            public void onFailure(int i, String s) {
                                                                dialogStatus(false);
                                                                error(i);
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
        MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {
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
                error(i);
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
