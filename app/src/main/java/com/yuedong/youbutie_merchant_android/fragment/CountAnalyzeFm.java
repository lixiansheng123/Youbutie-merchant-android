package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.CountConsumeAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * 统计分析fragment
 */
public class CountAnalyzeFm extends BaseFragment {
    private String[] titles = new String[]{"消费类目", "返店率", "客户评价"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView item1ListView;
    private CountConsumeAdapter item1Adapter;
    private CheckBox[] cbs = new CheckBox[3];
    private Fragment[] items = new Fragment[3];
    private Merchant merchant;

    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("统计分析"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_count_analyze, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        items[0] = new ConsumeTypeFm();
        items[1] = new ConsumeTypeFm();
        items[2] = new ConsumeTypeFm();
        tabLayout = fvById(R.id.id_tablayout);
        viewPager = fvById(R.id.id_viewpager);
        cbs[0] = fvById(R.id.id_cb_xiaofei);
        cbs[1] = fvById(R.id.id_cb_fandian);
        cbs[2] = fvById(R.id.id_cb_pingjia);
        viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return items[position];
            }

            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);


        ui();
    }

    private void ui() {
        // TODO 测试用户
        MerchantEvent.getInstance().findMeMetchant(Constants.TEST_USER_ID, new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                dialogStatus(true);
                merchant = list.get(0);
                final String merchantObjectId = merchant.getObjectId();
                final OrderEvent orderEvent = OrderEvent.getInstance();
                // 获取当月销售额
                orderEvent.getCurMonthSale(merchantObjectId, new FindStatisticsListener() {
                    @Override
                    public void onSuccess(Object o) {
                        JSONArray ary = (JSONArray) o;
                        if (ary != null) {//
                            try {
                                JSONObject obj = ary.getJSONObject(0);
                                int sum = obj.getInt("_sumPrice");//_(关键字)+首字母大写的列名
                                L.d("当月总销售额:" + sum);
                                orderEvent.getCurMonthUser(merchantObjectId, new FindListener<Order>() {
                                    @Override
                                    public void onSuccess(List<Order> list) {
                                        List<String> userObjectIds = new ArrayList<String>();
                                        if (CommonUtils.listIsNotNull(list)) {
                                            for (Order order : list) {
                                                String userOrderId = order.getUser().getObjectId();
                                                if (!userObjectIds.contains(userOrderId)) {
                                                    userObjectIds.add(userOrderId);
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        error(s);
                                        dialogStatus(false);
                                    }
                                });
                            } catch (JSONException e) {
                                dialogStatus(false);
                                e.printStackTrace();
                            }
                        } else {
                            dialogStatus(false);
                            T.showShort(getActivity(), "查询成功，无数据");
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        error(s);
                        dialogStatus(false);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                error(s);
                dialogStatus(true);
            }
        });

    }

    @Override
    public void initEvents() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIconStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeIconStatus(int position) {
        for (int i = 0; i < cbs.length; i++) {
            CheckBox cb = cbs[i];
            if (i == position)
                cb.setChecked(true);
            else
                cb.setChecked(false);
        }
    }


}
