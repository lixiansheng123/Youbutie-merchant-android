package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

/**
 * 封装的轮播 支持手动自动轮播 可点击轮播
 */
public class BannerView<T> extends FrameLayout implements OnPageChangeListener {
    private ViewPager mViewPager;
    private LinearLayout mPoints;
    private List<T> mData;
    private int defaultShowPic = -1;
    /**
     * 当前页面
     */
    private int mCurPage = 300;
    public final String ERROR_DATA_IS_NULL = "data is null";
    public final String ERROR_DATA_TO_SHOOT = "data too shoot";
    public long lastScroll;
    private int mScrollTime = 3000;
    /**
     * 自动轮播
     */
    private boolean mAutoScroll = true;
    private boolean mNeedIndicator = true;
    private boolean mInitFinshed = false;
    private IClickListener mClickListener;
    private IPagerSelectListener mPagerSelectListener;
    // 采用ImageLoader来加载图片
//    private DisplayImageOptions mDisplayImageOptions;

    public void setIPagerSelectListener(IPagerSelectListener listener) {
        this.mPagerSelectListener = listener;
    }

    public void setIClickListener(IClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setIsNeedIndicator(boolean needIndicator) {
        this.mNeedIndicator = needIndicator;
    }

    public void setIsAutoScroll(boolean autoScroll) {
        this.mAutoScroll = autoScroll;
    }

    public boolean getIsScroll() {
        return mInitFinshed && mAutoScroll;
    }

    public void buildData(List<T> mData) {
        this.mData = mData;
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        if (mData == null && mData.isEmpty())
            throw new NullPointerException(ERROR_DATA_IS_NULL);
//        initDisplayOptions();
        mViewPager = new ViewPager(getContext());
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        if (mNeedIndicator) {
            mPoints = new LinearLayout(getContext());
            for (int i = 0; i < mData.size(); i++) {
                ImageView point = new ImageView(getContext());
                point.setPadding(10, 5, 10, 5);
//                if (i == 0)
//                    point.setImageResource(R.drawable.bg_point_selected);
//                else
//                    point.setImageResource(R.drawable.bg_point_default);
                mPoints.addView(point);
            }
            LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            flp.gravity = Gravity.CENTER | Gravity.BOTTOM;
            flp.bottomMargin = ViewUtils.getViewDisplaySize(28, ViewUtils.ViewEnum.H);
            mPoints.setLayoutParams(flp);
            addView(mPoints);
        }
        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(mData.size() != 1 ? mCurPage = mData.size() * 100 : 0);
        if (mData.size() == 1) {
            mAutoScroll = false;
        }
        lastScroll = System.currentTimeMillis();
        if (mAutoScroll) {
            // 轮播
            mHandler.postDelayed(run, 0);
        }
        mInitFinshed = true;
    }

//	private void initDisplayOptions() {
//		ImageLoaderProcess imageLoaderProcess = ImageLoaderProcess.getInstance(getContext());
//		imageLoaderProcess.loadErrorImage = R.drawable.icon_display_default;
//		imageLoaderProcess.loadShowImage = R.drawable.icon_display_default;
//		imageLoaderProcess.loadUrlIsEmpty = R.drawable.icon_display_default;
//		mDisplayImageOptions = imageLoaderProcess.getDisplayImageOptions();
//	}

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // 解决当图片只有一张的时候还在轮播 这里也是无缝轮播的关键
            return mData.size() != 1 ? Integer.MAX_VALUE : mData.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            // 采用在这里new view的方式可以解决一些莫名的bug
            final int index = position % mData.size();
            NetworkImageView iv = new NetworkImageView(getContext());
            iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            iv.setScaleType(ScaleType.CENTER_CROP);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onClick((ViewPager) container, v, index);
                    }
                }
            });
            if (defaultShowPic != -1) {
                iv.setImageResource(defaultShowPic);
            } else {
                iv.setImageResource(android.R.color.black);
            }
            ((ViewPager) container).addView(iv);
            if (mData.get(index) instanceof String) {
                // 加载网络图片
                DisplayImageByVolleyUtils.loadImage(iv, (String) mData.get(index));
//				ImageLoader.getInstance().displayImage((String) mData.get(index), iv, mDisplayImageOptions);
            } else if (mData.get(index) instanceof Integer) {
                iv.setImageResource((Integer) mData.get(index));
            }
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

    }

    private Handler mHandler = new Handler();

    public Runnable run = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - lastScroll >= mScrollTime) {
                mCurPage++;
                mViewPager.setCurrentItem(mCurPage, true);
                lastScroll = System.currentTimeMillis();
            }
            mHandler.postDelayed(run, mScrollTime);
        }
    };

    @Override
    public void onPageSelected(int arg0) {
        mCurPage = arg0;
        lastScroll = System.currentTimeMillis();
        int index = arg0 % mData.size();
        if (mNeedIndicator) {
            curPointsStatus(index);
        }
        if (mPagerSelectListener != null)
            mPagerSelectListener.onPagerSelect(index);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    private void curPointsStatus(int index) {
        for (int i = 0; i < mData.size(); i++) {
            ImageView point = (ImageView) mPoints.getChildAt(i);
//            if (index == i) {
//                point.setImageResource(R.drawable.bg_point_selected);
//            } else {
//                point.setImageResource(R.drawable.bg_point_default);
//            }
        }
    }

    /**
     * 调用这个方法才开始使用
     */
    public void use() {
        init();
    }

    /**
     * 停止滚动 如果是滚动的话
     */
    public void stopScroll() {
        if (mAutoScroll)
            mHandler.removeCallbacks(run);
    }

    /**
     * 开始滚动 如果可以滚动的话
     */
    public void startScroll() {
        if (mAutoScroll)
            mHandler.postDelayed(run, 0);
    }

    /**
     * 内部点击回调
     */
    public interface IClickListener {
        void onClick(ViewPager viewPager, View view, int position);
    }

    /**
     * 滚动翻页回调
     */
    public interface IPagerSelectListener {
        void onPagerSelect(int index);
    }

}
