package com.dajia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class myExpandListview extends ExpandableListView {

	public myExpandListview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public myExpandListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public myExpandListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
