package com.yuedong.youbutie_merchant_android;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.ClientMerchantConsumeRecordAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 客户详情
 */
public class ClientDetailActivity extends BaseActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RoundImageView userHead;
    private TextView userName, userMobile, carNum, carDesc, carMileageNum;
    private View editMileageLayout;
    private PullToRefreshListView refreshListView;
    private String[] tabTitles = new String[]{"车辆信息", "消费记录"};
    private List<View> views;
    private RefreshHelper<Order> refreshHelper = new RefreshHelper<Order>();
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = (Order) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        initTitleView(new TitleViewHelper().createDefaultTitleView4("客户详情", "邀请会员", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        setShowContentView(R.layout.activity_client_detail);
    }

    @Override
    protected void initViews() {
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
        User orderUser = order.getUser();
        DisplayImageByVolleyUtils.loadImage(orderUser.getPhoto(), userHead);
        userName.setText(orderUser.getNickname());
        userMobile.setText(orderUser.getMobilePhoneNumber());
        carNum.setText(orderUser.getCarNumber());
        carDesc.setText(orderUser.getCarString());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_member_num_layout:
                break;
        }
    }
}
