package com.yuedong.youbutie_merchant_android.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Config;

/**
 * @author 俊鹏
 */
public class ViewUtils {
//    private static LayoutInflater inflater = LayoutInflater.from(App.getInstance().getAppContext());

    public static <T extends View> T fvById(int id, View view) {
        return (T) view.findViewById(id);
    }

    public static View inflaterView(Context context, int id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(id, null);
    }

    public static View inflaterView(Context context, int id, ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(id, parentView, false);
    }

    /**
     * 适配屏幕上的textSize
     *
     * @param viewGroup
     */
    public static void adapterUiText(ViewGroup viewGroup) {
//        int childCount = viewGroup.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View childView = viewGroup.getChildAt(i);
//            if (childView instanceof ViewGroup)
//                adapterUiText((ViewGroup) childView);
//            else if (childView instanceof TextView) {
//                TextView textView = ((TextView) childView);
//                int textSize = (int) textView.getTextSize();
//                // setTextSize 默认是以dp
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getViewDisplaySize(textSize, ViewEnum.H));
//            }
//
//        }
    }

    /**
     * 获取view合适的尺寸 根据设计的值
     *
     * @param designSize 设计的尺寸
     * @param viewEnum   是宽还是高
     * @return
     */
    public static int getViewDisplaySize(int designSize, ViewEnum viewEnum) {
        Integer[] phoneWh = App.getInstance().getPhoneWh();
        int valua = 0;
        if (viewEnum == null) return -1;
        switch (viewEnum) {
            case W:
                valua = (int) (designSize * (phoneWh[0] * 1.0f / Config.DESIGN_W) /*+ 0.5f*/);
                break;

            case H:
                valua = (int) (designSize * (phoneWh[1] * 1.0f / Config.DESIGN_H) /*+ 0.5f*/);
                break;
        }
        L.i("designSize" + designSize + "----getViewDisplaySize-value" + valua);
        return valua;
    }

    public enum ViewEnum {
        W, H;
    }

    public static void hideLayout(View view) {
        hideLayouts(view);
    }

    public static void hideLayouts(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.GONE) {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static void invisibleLayout(View view) {
        invisibleLayouts(view);
    }

    public static void invisibleLayouts(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.INVISIBLE) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    public static void showLayout(View view) {
        showLayouts(view);
    }

    public static void showLayouts(View... views) {
        for (View v : views) {
            if (v != null && v.getVisibility() != View.VISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }
}
