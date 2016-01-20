package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.ClientMerchantConsumeRecordAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 客户详情
 */
public class ClientDetailActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RoundImageView userHead;
    private TextView userName, userMobile, carNum, carDesc, carMileageNum;
    private ImageView iconVip;
    private View editMileageLayout;
    private PullToRefreshListView refreshListView;
    private String[] tabTitles = new String[]{"车辆信息", "消费记录"};
    private List<View> views;
    private RefreshHelper<Order> refreshHelper = new RefreshHelper<Order>();
    private Order order;
    private boolean isVip;
    private User orderUser;
    // 更新上一页数据
    private boolean updatePreviousPagerDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        order = (Order) bundle.getSerializable(Constants.KEY_BEAN);
        isVip = bundle.getBoolean(Constants.KEY_BOO);
        initTitleView(new TitleViewHelper().createDefaultTitleView4_2("客户详情", "邀请会员", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchWithExitUtils.startActivity(activity, InviteMemberActivity.class);
            }
        }));
        setShowContentView(R.layout.activity_client_detail);
    }

    @Override
    protected void initViews() {
        iconVip = fvById(R.id.id_icon_vip);
        views = new ArrayList<View>();
        tabLayout = fvById(R.id.id_tab_client_detail);
        viewPager = fvById(R.id.id_vp_client_detail);
        userHead = fvById(R.id.id_user_pic);
        userName = fvById(R.id.id_user_name);
        userMobile = fvById(R.id.id_user_mobile);
        View item1 = ViewUtils.inflaterView(context, R.layout.item_vp_car_info);
        View item2 = ViewUtils.inflaterView(context, R.layout.item_vp_consume_record);
        views.add(item1);
        views.add(item2);
        refreshListView = (PullToRefreshListView) item2.findViewById(R.id.id_refresh_view);
        carNum = (TextView) item1.findViewById(R.id.id_car_num);
        carDesc = (TextView) item1.findViewById(R.id.id_car_desc);
        carMileageNum = (TextView) item1.findViewById(R.id.id_mileage_num);
        editMileageLayout = item1.findViewById(R.id.id_mileage_layout);
        refreshHelper.setPulltoRefreshRefreshProxy(this, refreshListView, new RefreshHelper.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return new ClientMerchantConsumeRecordAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<Order> listener) {
                OrderEvent.getInstance().getUserOrderByMerchant(skip, limit, order.getUser().getObjectId(), order.getMerchant().getObjectId(), listener);
            }
        });
        viewPager.setAdapter(new AbstractPagerAdapter(views.size()) {
            @Override
            public Object getView(ViewGroup container, int position) {
                return views.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initEvents() {
        editMileageLayout.setOnClickListener(this);
    }

    @Override
    protected void ui() {
        orderUser = order.getUser();
        DisplayImageByVolleyUtils.loadImage(orderUser.getPhoto(), userHead);
        userName.setText(orderUser.getNickname());
        userMobile.setText(orderUser.getMobilePhoneNumber());
        carNum.setText(orderUser.getCarNumber());
        carDesc.setText(orderUser.getCarString());
        carMileageNum.setText(orderUser.getStrokeLength());
        if (isVip)
            ViewUtils.showLayout(iconVip);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_mileage_layout:
                Intent intent = new Intent(activity, InfoEditActivity.class);
                intent.putExtra(Constants.KEY_TEXT, "行驶公里数");
                intent.putExtra(Constants.KEY_ACTION, InfoEditActivity.ACTION_INPUT_MILEAGE);
                LaunchWithExitUtils.startActivityForResult(activity, intent, Constants.REQUESTCODE_INPUT_MILEAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_INPUT_MILEAGE && resultCode == Constants.RESULT_INPUT_MILEAGE && data != null) {
            final String stringExtra = data.getStringExtra(Constants.KEY_TEXT);
            if (!TextUtils.isEmpty(stringExtra)) {
                try {
                    dialogStatus(true);
                    JSONObject params = new JSONObject();
                    params.put("userId", orderUser.getObjectId());
                    params.put("strokeLength", stringExtra);
                    // 使用云端代码更新别的用户行驶公里数
                    AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
                    //第一个参数是上下文对象，第二个参数是云端代码的方法名称，第三个参数是上传到云端代码的参数列表（JSONObject cloudCodeParams），第四个参数是回调类
                    ace.callEndpoint(context, "updateUser", params,
                            new CloudCodeListener() {
                                @Override
                                public void onSuccess(Object object) {
                                    dialogStatus(false);
                                    carMileageNum.setText(stringExtra);
                                    updatePreviousPagerDate = true;

                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    dialogStatus(false);
                                    T.showShort(context, msg);
                                }
                            });
                } catch (JSONException e) {
                    dialogStatus(false);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        if (updatePreviousPagerDate) {
            setResult(0x225);
        }
        defaultFinished();
    }

}
