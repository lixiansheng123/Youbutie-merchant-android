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
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.ContributionRankingActivity;
import com.yuedong.youbutie_merchant_android.IncomeDetailActivity;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.CountConsumeAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.IncomeDetailListBean;
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.MoneyContributionEvent;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
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
public class CountAnalyzeFm extends BaseFragment implements View.OnClickListener {
    private String[] titles = new String[]{"消费类目", "返店率", "客户评价"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView item1ListView;
    private CountConsumeAdapter item1Adapter;
    private CheckBox[] cbs = new CheckBox[3];
    private Fragment[] items = new Fragment[3];
    private Merchant merchant;
    private TextView curMoneySales, curMoneyUser, avgConsume, oliContribution;
    private int oliContributionNum, totalSalesNum;

    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("统计分析"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_count_analyze, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        curMoneySales = fvById(R.id.id_cur_month_sales);
        curMoneyUser = fvById(R.id.id_cur_month_user);
        avgConsume = fvById(R.id.id_avg_consume);
        oliContribution = fvById(R.id.id_oli_contribution);
        items[0] = new ConsumeTypeFm();
        items[1] = new ConsumeTypeFm();
        items[2] = new CountUserEvaluateFm();
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
        MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {
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
                        if (ary != null) {
                            try {
                                JSONObject obj = ary.getJSONObject(0);
                                totalSalesNum = obj.getInt("_sumPrice");//_(关键字)+首字母大写的列名
                                L.d("当月总销售额:" + totalSalesNum);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            T.showShort(getActivity(), "查询当月总销售额成功，无数据");
                        }

                        final int finalTotalSales = totalSalesNum;
                        orderEvent.getCurMonthUser(merchantObjectId, new FindListener<Order>() {
                            @Override
                            public void onSuccess(List<Order> list) {
                                final List<String> userObjectIds = new ArrayList<String>();
                                if (CommonUtils.listIsNotNull(list)) {
                                    for (Order order : list) {
                                        String userOrderId = order.getUser().getObjectId();
                                        if (!userObjectIds.contains(userOrderId)) {
                                            userObjectIds.add(userOrderId);
                                        }
                                    }
                                }
                                L.d("当月用户:" + userObjectIds.size());
                                MoneyContributionEvent.getInstance().getMerchantMoneyContribution(merchantObjectId, new FindStatisticsListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        dialogStatus(false);
                                        JSONArray ary = (JSONArray) o;
                                        if (ary != null) {//
                                            try {
                                                JSONObject obj = ary.getJSONObject(0);
                                                oliContributionNum = obj.getInt("_sumMoney");//_(关键字)+首字母大写的列名
                                                L.d("油点贡献度:" + oliContributionNum);
                                                curMoneySales.setText("￥" + finalTotalSales);
                                                curMoneyUser.setText(userObjectIds.size() + "");
                                                avgConsume.setText((int) (finalTotalSales * 1.0f / userObjectIds.size()) + "");
                                                oliContribution.setText(oliContributionNum + "");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            T.showShort(getActivity(), "查询油点贡献成功，无数据");
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
                                dialogStatus(false);
                            }
                        });


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
        fvById(R.id.id_total_sell_num_layout).setOnClickListener(this);
        fvById(R.id.id_oli_layout).setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_oli_layout:
                if (merchant != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, merchant);
                    bundle.putInt(Constants.KEY_INT, oliContributionNum);
                    LaunchWithExitUtils.startActivity(getActivity(), ContributionRankingActivity.class, bundle);
                }
                break;

            case R.id.id_total_sell_num_layout:
                if (merchant != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, merchant);
                    bundle.putInt(Constants.KEY_INT, totalSalesNum);
                    LaunchWithExitUtils.startActivity(getActivity(), IncomeDetailActivity.class, bundle);
                }
                break;
        }
    }
}
