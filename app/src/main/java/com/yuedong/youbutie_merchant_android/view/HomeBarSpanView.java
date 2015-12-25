package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

public class HomeBarSpanView extends RelativeLayout implements OnClickListener {

    private ImageView imageView;
    private TextView textView;
    private Drawable imageIcon;
    private Drawable imageClickIcon;
    private String textStr;
    private static final String DEFAULT_TEXT = "测试";
    // 是否是点击状态
    private boolean isClickStatus = false;
    private OnBottomBarClickListener bottomBarClickListener;
    private int iconW, iconH;

    public void setOnBottomBarClickListener(OnBottomBarClickListener bottomBarClickListener) {
        this.bottomBarClickListener = bottomBarClickListener;
    }

    public void setClickStatus(boolean clickStatus) {
        this.isClickStatus = clickStatus;
    }

    public HomeBarSpanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBarSpanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 定义图标的宽高
        iconW = ViewUtils.getViewDisplaySize(70, ViewUtils.ViewEnum.W);
        iconH = ViewUtils.getViewDisplaySize(70, ViewUtils.ViewEnum.H);


        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.attHomeSpan);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.attHomeSpan_imageBackground:
                    imageIcon = array.getDrawable(attr);
                    break;
                case R.styleable.attHomeSpan_textStr:
                    textStr = array.getString(attr);
                    break;

                case R.styleable.attHomeSpan_imageClickBackground:
                    imageClickIcon = array.getDrawable(attr);
                    break;

                case R.styleable.attHomeSpan_click:
                    isClickStatus = array.getBoolean(attr, false);
                    break;

                default:
                    break;
            }
        }

        array.recycle();
        if (null == imageIcon) {
            imageIcon = context.getResources().getDrawable(R.mipmap.ic_launcher);
        }

        if (null == imageClickIcon) {
            // TODO 这里先什么都不干
        }

        if ("".equals(textStr)) {
            textStr = DEFAULT_TEXT;
        }

        initView();
    }

    @SuppressWarnings("ResourceType")
    private void initView() {

        setClickable(true);
        setGravity(Gravity.CENTER);
        imageView = new ImageView(getContext());
        imageView.setId(0x001);
        textView = new TextView(getContext());
        textView.setTextSize(12f);
        LayoutParams imageLp = new LayoutParams(iconW,
                iconH);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, imageView.getId());
        lp.topMargin = 1;
        imageLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(textView, lp);
        addView(imageView, imageLp);
        textView.setGravity(Gravity.CENTER);
        imageView.setImageDrawable(imageIcon);
        textView.setText(textStr);
        changeStyle(isClickStatus);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        isClickStatus = true;
        changeStyle(isClickStatus);
        if (bottomBarClickListener != null) {
            bottomBarClickListener.onClick(v);
        }
    }

    public void resetBackground() {
        if (isClickStatus = true) {
            isClickStatus = false;
            changeStyle(isClickStatus);
        }
    }

    /**
     * 改变样式 点击和没点击的样式是不一样的
     *
     * @param isClick 是否点击
     */
    public void changeStyle(boolean isClick) {
        if (isClick) {
            textView.setTextColor(Color.parseColor("#f0c008"));
            if (imageClickIcon != null)
                imageView.setImageDrawable(imageClickIcon);
            else
                imageView.setImageDrawable(imageIcon);
        } else {
            textView.setTextColor(Color.parseColor("#7e6e6c"));
            imageView.setImageDrawable(imageIcon);
        }
    }

    public void resetClickStatus() {
        if (isClickStatus != false) {
            isClickStatus = false;
        }
    }

    public interface OnBottomBarClickListener {
        public void onClick(View view);
    }

}
