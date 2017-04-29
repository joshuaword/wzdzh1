package com.dajia.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public NoScrollGridView(Context context) {
		super(context);
	}
	public NoScrollGridView(android.content.Context context,
			android.util.AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		int t = getMeasuredHeight();
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
