package com.dajia.activity;

import net.k76.wzd.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedBackActivity extends BaseActivity {
	
	private TextView tv_empty_title;
	private TextView title;
	private TextView rightBtn;
	private LinearLayout backLayout;
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.view_feed_back);
	
	title = (TextView) findViewById(R.id.title);
	title.setText("意见反馈");
	backLayout = (LinearLayout) findViewById(R.id.back_layout);
	backLayout.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			finish();
		}
	});
	
	rightBtn = (TextView) findViewById(R.id.btnRight);
	rightBtn.setText("写反馈");
	tv_empty_title = (TextView) findViewById(R.id.tv_empty_title);
	tv_empty_title.setText("暂无反馈");
	
}
}
