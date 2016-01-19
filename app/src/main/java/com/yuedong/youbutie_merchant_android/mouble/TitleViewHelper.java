package com.yuedong.youbutie_merchant_android.mouble;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.utils.ActivityTaskUtils;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.lang.ref.WeakReference;


/**
 * @author 俊鹏
 */
public class TitleViewHelper {

    public TitleViewHelper() {
        cache = new SparseArray<View>();
        backIcon = R.drawable.icon_left_black_arrows;
        titleColor = Color.parseColor("#f0c010");
    }

    //---------------------------------------------------------------------默认
    private int backIcon;
    private int titleColor;
    private SparseArray<View> cache;
    private WeakReference<View> normalTitleViewCache;
    private View titleView;
    private ImageView backImage;
    private LinearLayout leftLl;
    private TextView titleText;
    private LinearLayout rightLl;
    private ImageView moreImage;
    private TextView rightTextView;

    /**
     * 含有返回标题右边的图标
     *
     * @param title
     * @param moreIcon
     * @param moreListener
     * @return
     */
    public View createDefaultTitleView(CharSequence title, int moreIcon, View.OnClickListener moreListener) {
        return createDefaultTitleView(backIcon, title, titleColor, moreIcon, null, moreListener);
    }

    public View createDefaultTitleView(CharSequence title, int moreIcon, View.OnClickListener leftListener, View.OnClickListener moreListener) {
        return createDefaultTitleView(backIcon, title, titleColor, moreIcon, leftListener, moreListener);
    }


    public View createDefaultTitleView(int icon, CharSequence title, int titleColor, int moreIcon, View.OnClickListener leftListener, View.OnClickListener moreListener) {

        initDefaultTitleView();
        backImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        moreImage.setVisibility(View.VISIBLE);
        backImage.setImageResource(icon);
        titleText.setTextColor(titleColor);
        titleText.setText(title);
        moreImage.setImageResource(moreIcon);
        if (leftListener != null) {
            backImage.performClick();
            leftLl.setOnClickListener(leftListener);
        } else {
            leftLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }

        if (moreListener != null) {
            moreImage.performClick();
            rightLl.setOnClickListener(moreListener);
        }
        return titleView;
    }

    /**
     * 只有标题
     *
     * @return
     */
    public View createDefaultTitleView2(CharSequence title) {
        return createDefaultTitleView2(title, -1);

    }

    public View createDefaultTitleView2(CharSequence title, int titleColor) {
        initDefaultTitleView();
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
        if (titleColor != -1)
            titleText.setTextColor(titleColor);
        return titleView;
    }

    /**
     * 含有标题回退按钮
     *
     * @param title
     * @return
     */
    public View createDefaultTitleView3(CharSequence title) {
        return createDefaultTitleView3(title, null);
    }

    public View createDefaultTitleView3(CharSequence title, View.OnClickListener leftListener) {
        initDefaultTitleView();
        backImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
        if (leftListener != null) {
            leftLl.setOnClickListener(leftListener);
        } else {
            leftLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        }
        return titleView;
    }


    public View createDefaultTitleView4(CharSequence title, CharSequence rightText, final View.OnClickListener rightClickListener) {
        initDefaultTitleView();
        backImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText(rightText);
        titleText.setText(title);
        leftLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        rightLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClickListener != null)
                    rightClickListener.onClick(v);
            }
        });
        return titleView;
    }

    public View createDefaultTitleView5(CharSequence title, CharSequence rightText, int rightTextColor, int moreIcon, final View.OnClickListener rightClickListener) {
        initDefaultTitleView();
        backImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        rightTextView.setVisibility(View.VISIBLE);
        moreImage.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = moreImage.getLayoutParams();
//        lp.width = ViewUtils.getViewDisplaySize(52, ViewUtils.ViewEnum.W);
//        lp.height = ViewUtils.getViewDisplaySize(52, ViewUtils.ViewEnum.W);
        titleText.setText(title);
        rightTextView.setTextColor(rightTextColor);
        rightTextView.setText(rightText);
        moreImage.setImageResource(moreIcon);
        leftLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        rightLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClickListener != null)
                    rightClickListener.onClick(v);
            }
        });
        return titleView;
    }


    public View createDefaultTitleView6(CharSequence title, CharSequence rightText, int rightTextColor, int moreIcon, final View.OnClickListener rightClickListener) {
        initDefaultTitleView();
//        backImage.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        rightTextView.setVisibility(View.VISIBLE);
        moreImage.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = moreImage.getLayoutParams();
//        lp.width = ViewUtils.getViewDisplaySize(52, ViewUtils.ViewEnum.W);
//        lp.height = ViewUtils.getViewDisplaySize(52, ViewUtils.ViewEnum.W);
        titleText.setText(title);
        rightTextView.setTextColor(rightTextColor);
        rightTextView.setText(rightText);
        moreImage.setImageResource(moreIcon);
//        leftLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                back();
//            }
//        });
        rightLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClickListener != null)
                    rightClickListener.onClick(v);
            }
        });
        return titleView;
    }


    public void initDefaultTitleView() {
        if (normalTitleViewCache != null) {
            titleView = normalTitleViewCache.get();
        } else {
            titleView = ViewUtils.inflaterView(App.getInstance().getAppContext(), R.layout.layout_headview_normal);
            normalTitleViewCache = new WeakReference<View>(titleView);
        }
        hideTotalView((ViewGroup) titleView);
        backImage = getIdByView(R.id.id_likeBack_headView, titleView);
        leftLl = getIdByView(R.id.ll_left_layout, titleView);
        titleText = getIdByView(R.id.id_title_headView, titleView);
        rightLl = getIdByView(R.id.ll_right_layout, titleView);
        moreImage = getIdByView(R.id.iv_more_headview, titleView);
        rightTextView = getIdByView(R.id.tv_right_text, titleView);
        backImage.setImageResource(backIcon);
        titleText.setTextColor(titleColor);
    }


    /**
     * 隐藏所有的控件
     *
     * @param rootView
     */
    private void hideTotalView(ViewGroup rootView) {
        int childCount = rootView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = rootView.getChildAt(i);
            if (childView instanceof ViewGroup) {
                hideTotalView((ViewGroup) childView);
            } else {
                childView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getIdByView(int viewId, View contentView) {
        View view = cache.get(viewId);
        if (view == null) {
            view = contentView.findViewById(viewId);
            cache.put(viewId, view);
        }
        return (T) view;
    }

    private void back() {
        // 从task中获取最后一个activity
        Activity lasActivity = ActivityTaskUtils.getInstance().getLasActivity();
        if (lasActivity != null) {
            ((BaseActivity) lasActivity).defaultFinished();
        }
    }

}
