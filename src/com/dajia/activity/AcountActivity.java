package com.dajia.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.k76.wzd.R;

import com.dajia.Bean.UserBean;
import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AcountActivity extends BaseActivity {

	private TextView title;
	private LinearLayout backLayout;
	TextView nameText;
	TextView phoneText;
	TextView sexText;
	TextView carNum;
	TextView carType;
	TextView yaoqingmaText;
	TextView userId;
	String dengluhao;
	UserBean userBean;
	private String chepai, chexing;
	private String phoneString,passwordString,kehuleixing;
	private BroadcastReceiver finish = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			//from = arg1.getStringExtra("from");
			chepai = arg1.getStringExtra("chepai");
			chexing = arg1.getStringExtra("chexing");
			Log.d("chepaichepai", chepai + chexing);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		registerReceiver(finish, new IntentFilter("acountfinish"));
		title = (TextView) findViewById(R.id.title);
		title.setText("编辑我的资料");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		nameText = (TextView) findViewById(R.id.name);

		phoneText = (TextView) findViewById(R.id.phone);
		sexText = (TextView) findViewById(R.id.sex);
		carNum = (TextView) findViewById(R.id.car_num);
		carType = (TextView) findViewById(R.id.car_type);
		yaoqingmaText = (TextView) findViewById(R.id.yaoqingma);
		userId = (TextView) findViewById(R.id.user_id);

		SharedPreferences sp = getSharedPreferences("setting", 0);
		dengluhao = sp.getString("dengluhao", "");
		phoneString=sp.getString("sp_phone", "");
		passwordString=sp.getString("sp_password", "");
		kehuleixing=sp.getString("kehuleixing", "");
		Log.e("kehuleixing======", kehuleixing);
		
		String ifshowcheliang = sp.getString("ifshowcheliang", "no");

		// 姓名编辑
		LinearLayout nameLayout = (LinearLayout) findViewById(R.id.name_layout);
		nameLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sent("nickname", userBean.getNickname());
			}
		});

		// 手机号编辑
		LinearLayout phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
		phoneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
		
				if (userBean != null) {
					Intent intent = new Intent(AcountActivity.this,
							ChatLoginActivity.class);
					startActivity(intent);
				}
			}
		});

		// 性别编辑
		LinearLayout sexLayout = (LinearLayout) findViewById(R.id.sex_layout);
		sexLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sent("sex", userBean.getSex());
			}
		});

		// 车牌号编辑
		LinearLayout chepaihaoLayout = (LinearLayout) findViewById(R.id.carnum_layout);
		if (!TextUtils.isEmpty(kehuleixing)) {
			if (kehuleixing.equals("会员")) {
				Log.e("kehuleixing111111=", kehuleixing);
				return;
			}else {
				Log.e("kehuleixing222222=", kehuleixing);
				chepaihaoLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// sent("chepaihao",userBean.getChepaihao());
						if (userBean != null) {
							// Intent intent = new
							// Intent(AcountActivity.this,VehicleActivity.class);
							// intent.putExtra("chexing",userBean.getChexing());
							// intent.putExtra("chepaihao",userBean.getChepaihao());
							// startActivity(intent);	
								Intent intent = new Intent(AcountActivity.this,
										CheLiangguanliListActivity.class);
								intent.putExtra("from", "account");
								intent.putExtra("userid", userBean.getUserid());
								startActivity(intent);			
						}
					}
				});
			}
		}
		

		// 车型编辑
		LinearLayout chexingLayout = (LinearLayout) findViewById(R.id.cartype_layout);
		if (!TextUtils.isEmpty(kehuleixing)) {
			if (kehuleixing.equals("会员")) {
				return;
			}else {
				chexingLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// sent("chexing",userBean.getChexing());
						if (userBean != null) {
							// Intent intent = new
							// Intent(AcountActivity.this,VehicleActivity.class);
							// intent.putExtra("chexing",userBean.getChexing());
							// intent.putExtra("chepaihao",userBean.getChepaihao());
							// startActivity(intent);
							
							Intent intent = new Intent(AcountActivity.this,
									CheLiangguanliListActivity.class);
							intent.putExtra("from", "account");
							startActivity(intent);
						}
					}
				});
			}
		}
		
		// 隐藏车辆
		if ("no".equals(ifshowcheliang)) {
			chepaihaoLayout.setVisibility(View.GONE);
			chexingLayout.setVisibility(View.GONE);
		}
		//
		LinearLayout yaoqingmaLayout = (LinearLayout) findViewById(R.id.yaoqing_layout);
		yaoqingmaLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sent("yaoqingma", userBean.getYaoqingma());
			}
		});

		// 登录号编辑
		//LinearLayout loginId = (LinearLayout) findViewById(R.id.login_id);
		/*
		 * loginId.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { return; } });
		 */
		
		Button exitlogin_btn=(Button) findViewById(R.id.btn_exitlogin);
		exitlogin_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AcountActivity.this,
						ChatLoginActivity.class);
				startActivity(intent);
			}
		});

	}

	public void sent(String falg, String content) {
		if (userBean != null) {
			Intent intent = new Intent(AcountActivity.this,
					EditUserNameActivity.class);
			intent.putExtra("flag", falg);
			intent.putExtra("content", content);
			intent.putExtra("userid", userBean.getUserid());
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.e("MUSIC", "LOGIN AcountActivity 1111--");
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("telphone", phoneString);
		params.put("password", passwordString);	
		params.put("qquid", settings.getString("qquid", ""));
		params.put("weixinuid", settings.getString("weixinuid", ""));
		params.put("act", "postok");
		Log.e("MUSIC", "LOGINAcount--baseurl=" + baseurl + " dengluhao="
				+ dengluhao);
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.e("aaaaaaaaaaaa", t.toString());
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {
							if (!TextUtils.isEmpty(userBean.getNickname())) {
								nameText.setText(userBean.getNickname());
							}
							if (!TextUtils.isEmpty(userBean.getTelphone())) {
								phoneText.setText(userBean.getTelphone());
							}
							sexText.setText(userBean.getSex());
							carNum.setText(userBean.getChepaihao());
							carType.setText(userBean.getChexing());
							yaoqingmaText.setText(userBean.getYaoqingma());
							userId.setText(userBean.getUserid());
							if (TextUtils.isEmpty(kehuleixing)) {
								kehuleixing=userBean.getKehuleixing();
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(finish);
		super.onDestroy();
	}

}
