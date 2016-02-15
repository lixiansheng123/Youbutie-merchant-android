package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.AbstractPagerAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private View itemView = null;
    private List<View> views = new ArrayList<View>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        WindowUtils.hideSystemBar(this);
        super.onCreate(savedInstanceState);
        buildUi(null, false, false, false, R.layout.activity_guide);
    }

    @Override
    protected void initViews() {
        for (int i = 0; i < 3; i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_guide, null);
            View bg = itemView.findViewById(R.id.id_bg);
            View goLayout = itemView.findViewById(R.id.id_go_layout);
            ViewUtils.hideLayout(goLayout);
            switch (i) {
                case 0:
                    bg.setBackgroundResource(R.drawable.guide_1);
                    break;

                case 1:
                    bg.setBackgroundResource(R.drawable.guide_2);
                    break;

                case 2:
                    bg.setBackgroundResource(R.drawable.guide_3);
                    ViewUtils.showLayout(goLayout);
                    goLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LaunchWithExitUtils.startActivity(activity, LoginActivity.class);
                            defaultFinished();
                        }
                    });
                    break;
            }
            views.add(itemView);
        }
        viewPager = fvById(R.id.id_vp_guide);
        viewPager.setAdapter(new AbstractPagerAdapter(3) {
            @Override
            public Object getView(ViewGroup container, int position) {
                return views.get(position);
            }
        });
    }

    @Override
    protected void initEvents() {
        SPUtils.put(context, Constants.SP_GUIDE, true);
    }

    @Override
    protected void ui() {

    }
}
