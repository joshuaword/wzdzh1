package com.dajia.view;

import net.k76.wzd.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SegmentLayout extends RelativeLayout {

	private RelativeLayout rl;
	private RelativeLayout moneyLayout;
	private TextView moneyTv;
	
	private Boolean flag = false;

	public SegmentLayout(Context context) {
		super(context);
		initView(context);
			addView(rl);
	}

	public SegmentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
			addView(rl);
	}

	public SegmentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
			addView(rl);
	}

	public void initView(Context context) {
		rl = (RelativeLayout) View.inflate(context, R.layout.segment_layout, null);
		moneyLayout = (RelativeLayout) rl.findViewById(R.id.money_layout);
				moneyTv = (TextView) rl.findViewById(R.id.money_tv);
		show();
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
		show();
		
	}
	
	public void show(){
		if (flag) {
			moneyLayout.setBackgroundResource(R.drawable.segment_rec);
			moneyTv.setTextColor(getResources().getColor(R.color.white));
		}else {
			moneyLayout.setBackgroundResource(R.color.white);
			moneyTv.setTextColor(getResources().getColor(R.color.btn_blue));
		}
	}

	
}
