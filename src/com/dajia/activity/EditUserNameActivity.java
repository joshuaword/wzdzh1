package com.dajia.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import net.k76.wzd.R;
import com.dajia.Bean.UserBean;
import com.google.gson.Gson;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditUserNameActivity extends BaseActivity {
	
	private TextView title;
	private LinearLayout backLayout;
	EditText edit;
	String flag;
	String dengluhao;
	String userid;
	
	RadioButton femaleRadioButton;
	RadioButton maleRadioButton;
	
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
		userid = getIntent().getStringExtra("userid");
		edit = (EditText) findViewById(R.id.edt_input);
		edit.setText(content);
		
		femaleRadioButton=(RadioButton)findViewById(R.id.radioFemale); 
        maleRadioButton=(RadioButton)findViewById(R.id.radioMale); 
		
		 //根据ID找到RadioGroup实例
		         RadioGroup group = (RadioGroup)this.findViewById(R.id.radioGroup);
		        
		        
		         group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		             @Override
		             public void onCheckedChanged(RadioGroup arg0, int arg1) {
		            	 //获取变更后的选中项的ID
		            	            if (arg1 == femaleRadioButton.getId()) {
		            	            	edit.setText("女");
									}
		            	            if (arg1 == maleRadioButton.getId()) {
		            	            	edit.setText("男");
									}
		                	 
		             }
		         });
		         
		         if ("sex".equals(flag)) {
		        	 edit.setVisibility(View.GONE);
		        	 group.setVisibility(View.VISIBLE);
		        	 if ("男".equals(content)) {
		        		 group.check(maleRadioButton.getId());
						}
		        	 if ("女".equals(content)) {
		        		 group.check(femaleRadioButton.getId());
						}
				}
		
		Button button = (Button) findViewById(R.id.btn_confirm);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FinalHttp fp = new FinalHttp();
				AjaxParams ap = new AjaxParams();
				ap.put(flag, edit.getText().toString().trim());
				ap.put("act", "postok");
				ap.put("userid", userid);
				
				final SharedPreferences settings = getSharedPreferences("setting", 0);
				String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
				
				fp.post(baseurl + "/api/registerclient.php", ap,new AjaxCallBack<Object>() {
					
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Toast.makeText(EditUserNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						finish();
					}
					
				});
			}
		});
		
	}
	
}
