package com.yuedong.youbutie_merchant_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuedong.youbutie_merchant_android.OrderDetailActivity;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 订单管理
 */
public class OrderManagerFm extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] tabTitles = new String[]{"新订单", "已接单", "已完结"};
    private OrderManagerVpAdapter vpAdapter;
    private List<OrderFm> fmLists = new ArrayList<OrderFm>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFm();
    }


    @Override
    public View getContentView(ViewGroup container) {
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_order_manager, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        tabLayout = (TabLayout) contentView.findViewById(R.id.id_tab_order_manager);
        viewPager = (ViewPager) contentView.findViewById(R.id.id_vp_order_manager);
        vpAdapter = new OrderManagerVpAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        // 保留3个在缓存里
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void initEvents() {

    }

    private void initFm() {
        OrderFm fm1 = new OrderFm();
        Bundle newOrderB = new Bundle();
        newOrderB.putString(Constants.KEY_ACTION, tabTitles[0]);
        fm1.setArguments(newOrderB);

        OrderFm fm2 = new OrderFm();
        Bundle jieDanB = new Bundle();
        jieDanB.putString(Constants.KEY_ACTION, tabTitles[1]);
        fm2.setArguments(jieDanB);

        OrderFm fm3 = new OrderFm();
        Bundle finishedB = new Bundle();
        finishedB.putString(Constants.KEY_ACTION, tabTitles[2]);
        fm3.setArguments(finishedB);
        fmLists.add(fm1);
        fmLists.add(fm2);
        fmLists.add(fm3);
    }

    public class OrderManagerVpAdapter extends FragmentPagerAdapter {

        public OrderManagerVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            L.i("getItem---------------------->>");
            return fmLists.get(position);
        }

        @Override
        public int getCount() {
            L.i("getCount---------------------->>");
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    /**
     * 更新所有子fragment
     */
    public void refreshTotalChildFm() {
        for (OrderFm orderFm : fmLists) {
            if (orderFm != null && orderFm.inintFinished) {
                orderFm.updateData();
            }
        }
    }

    /**
     * 通过当前fragment来启动activity 主要这样做是为了响应当前的fragment的activityResult方法
     *
     * @param order
     */
    public void passFmStartOrderDetail(Order order, int reqeustCode) {
        L.d("passFmStartOrderDetail------------->>");
        if (order == null) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_BEAN, order);
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtras(bundle);
        LaunchWithExitUtils.startActivityForResult(OrderManagerFm.this, intent, reqeustCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d("onActivityResult------------->>");
        if (requestCode == Constants.REQUESTCODE_RECEIVE_ORDER && resultCode == Constants.RESULT_RECEIVE_ORDER && data != null) {
            Order order = (Order) data.getSerializableExtra(Constants.KEY_BEAN);
            receiveOrder(order);
        }
    }


    public void receiveOrder(Order order) {
        loadDialog.setMessage("努力提交数据..");
        dialogStatus(true);
        Order updateOrder = new Order();
        updateOrder.setState(2);
        updateOrder.update(getActivity(), order.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                dialogStatus(false);
                T.showShort(getActivity(), "接单成功");
                // 更新3个切换页数据
                refreshTotalChildFm();
            }

            @Override
            public void onFailure(int i, String s) {
                error(s);
                dialogStatus(false);
            }
        });
    }
}
