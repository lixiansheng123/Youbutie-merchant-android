package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class SupportScrollConflictGridView extends GridView {

	public SupportScrollConflictGridView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SupportScrollConflictGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SupportScrollConflictGridView(Context context) {
		super(context);
	}

	/** 在ScrollView内，所以要进行计算高度 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
