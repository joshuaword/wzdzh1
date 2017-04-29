package com.dajia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.k76.wzd.R;

public class ZhifuMidActivity extends BaseActivity {
	
	private TextView title;
	private LinearLayout backLayout;
	EditText edit;
	String flag;
	String dengluhao;
	String userid;
	String dingdanid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhifu_mid);
		
		title = (TextView) findViewById(R.id.title);
		title.setText("订单支付");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		String content = getIntent().getStringExtra("money");
		dingdanid = getIntent().getStringExtra("dingdanid");
		edit = (EditText) findViewById(R.id.edt_input);
		edit.setText(content);
		
		Button button = (Button) findViewById(R.id.btn_zhifubao);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent(ZhifuMidActivity.this,PayHtmlActivity.class);
	                intent.putExtra("money",edit.getText().toString());
	                intent.putExtra("dingdanid",dingdanid);
	                startActivity(intent);
			}
		});
		
		Button xinjinButton = (Button) findViewById(R.id.btn_xianjin);
		xinjinButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
	                finish();
			}
		});
		
		
	}
	
}
