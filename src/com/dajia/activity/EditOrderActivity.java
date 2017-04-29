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

public class EditOrderActivity extends BaseActivity {
	
	private TextView title;
	private LinearLayout backLayout;
	EditText edit;
	String flag;
	String dengluhao;
	String userid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_user_name);
		
		title = (TextView) findViewById(R.id.title);
		title.setText("编辑");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		String content = getIntent().getStringExtra("content");
		flag = getIntent().getStringExtra("flag");
		edit = (EditText) findViewById(R.id.edt_input);
		edit.setText(content);
		
		Button button = (Button) findViewById(R.id.btn_confirm);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 Intent intent = new Intent();
	                intent.putExtra("content",edit.getText().toString());
	                intent.putExtra("flag",flag);
	                setResult(RESULT_OK, intent);
	                finish();
			}
		});
		
	}
	
}
