package com.dajia.activity;

import com.daijia.chat.ChatActivity;
import com.dajia.VehicleApp;
import com.dajia.Bean.UserBean;
import com.google.gson.Gson;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatLoginActivity extends BaseActivity {
	private TextView title;
	private LinearLayout backLayout;
	private Button login_go;
	private Button regist_go;
	public static final String SP_KEY_PHONE = "sp_phone";
	private EditText phone_ed;
	private EditText password_ed;
	private SharedPreferences sp;
	private TextView dismisspw;
	private String isEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		title = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.check_and_login));
		isEmail = VehicleApp.getInstance().getSetBean().getClientusername();
		// 返回
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
		String phone = sp.getString(SP_KEY_PHONE, "");

		phone_ed = (EditText) findViewById(R.id.login_phone);
		if(isEmail.equals("email")){
			phone_ed.setHint("请输入邮箱");
		}
		phone_ed.setText(phone);
		password_ed = (EditText) findViewById(R.id.edt_password);
		dismisspw = (TextView) findViewById(R.id.dismiss_password);
		dismisspw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ChatLoginActivity.this,DismissPasswordActivity.class);
				intent.putExtra("phone", phone_ed.getText().toString().trim());
				startActivity(intent);
			}
		});
		
		login_go = (Button) findViewById(R.id.btn_login);
		login_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(SP_KEY_PHONE,phone_ed.getText().toString().trim());
				editor.putString("sp_password", password_ed.getText().toString().trim());
				editor.commit();
				String baseurl = sp.getString("baseurl", "http://wzd.k76.net");
				FinalHttp fp = new FinalHttp();
				AjaxParams params = new AjaxParams();
				params.put("telphone", phone_ed.getText().toString().trim());
				params.put("password", password_ed.getText().toString().trim());
				params.put("act", "postok");
				fp.post(baseurl + "/api/applogin.php", params, new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.e("loginsuccess", t.toString());
						Gson gson = new Gson();
						UserBean bean = gson.fromJson(t.toString(),
								UserBean.class);
						if (bean!=null) {
							if (bean.getRet().equals("success")) {								
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, bean.getTelphone());
								editor.putString("userid",bean.getUserid());
								editor.putString("nickname",bean.getNickname());
								editor.putString("sex",bean.getSex());
								editor.putString("stryouhuijuan",bean.getStryouhuijuan());
								editor.putString("headimgpath",bean.getThefile());
								editor.putString("kehuleixing", bean.getKehuleixing());
								editor.commit();
								Intent intent=new Intent(ChatLoginActivity.this,ChatActivity.class);
								startActivity(intent);	
								finish();
							}else {
								Toast.makeText(ChatLoginActivity.this, bean.getRet(), Toast.LENGTH_LONG).show();
							} 
						}
					}
				});
			}
		});
		regist_go = (Button) findViewById(R.id.btn_regist_go);
		regist_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatLoginActivity.this,
						RegiterActivity.class);
				startActivity(intent);
			}
		});
	}
}
