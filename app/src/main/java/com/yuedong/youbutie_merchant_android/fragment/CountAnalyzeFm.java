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
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class CountAnalyzeFm extends BaseFragment {
    private String[] titles = new String[]{"消费类目", "返店率", "客户评价"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView item1ListView;
    private CountConsumeAdapter item1Adapter;
    private CheckBox[] cbs = new CheckBox[3];
    private Fragment[] items = new Fragment[3];

    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("统计分析"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_count_analyze, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        items[0] = new ConsumeTypeFm();
        tabLayout = fvById(R.id.id_tablayout);
        viewPager = fvById(R.id.id_viewpager);
        cbs[0] = fvById(R.id.id_cb_xiaofei);
        cbs[1] = fvById(R.id.id_cb_fandian);
        View item1 = ViewUtils.inflaterView(getActivity(), R.layout.item_vp_count_consume);
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
