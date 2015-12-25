package com.yuedong.youbutie_merchant_android.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PopupWindowUtils {

	static PopupWindow loadPw = null;
	private static final String COLOR_WHITE = "#ffffff";

	/**
	 * 加载过程的view
	 * 
	 * @param isShow
	 *            是否显示
	 * @param dependOnView
	 *            依附的view
	 */
	public static void loadProcessView(boolean isShow, View dependOnView,
			Context con) {
		if (loadPw == null)
			loadPw = new PopupWindow(con);
		if (isShow) {
			if (loadPw.isShowing())
				return;
			loadPw.setWidth(LayoutParams.MATCH_PARENT);
			loadPw.setHeight(LayoutParams.MATCH_PARENT);
			LinearLayout ll = new LinearLayout(con);
			ll.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			ll.setGravity(Gravity.CENTER);
			LinearLayout llChildView = new LinearLayout(con);
			ProgressBar pb = new ProgressBar(con);
			TextView tv = new TextView(con);
			llChildView.setGravity(Gravity.CENTER);
			tv.setText("加载中...");
			tv.setTextColor(Color.parseColor(COLOR_WHITE));
			llChildView.addView(pb);
			llChildView.addView(tv);
			ll.addView(llChildView);
			// 实例一个半透明的背景
			ColorDrawable cd = new ColorDrawable(0xb0000000);
			loadPw.setBackgroundDrawable(cd);
			loadPw.setContentView(ll);
			loadPw.showAtLocation(dependOnView, Gravity.CENTER, 0, 0);
			loadPw.setTouchInterceptor(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (loadPw != null && loadPw.isShowing()) {
						loadPw.dismiss();
						return true;
					}
					return false;
				}
			});
		} else {
			if (loadPw != null && loadPw.isShowing())
				loadPw.dismiss();
		}
	}
}
