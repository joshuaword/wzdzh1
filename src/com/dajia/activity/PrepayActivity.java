package com.dajia.activity;

import com.dajia.Bean.DriveBean;
import com.google.gson.Gson;

import net.k76.wzd.R;
import net.k76.wzd.wxapi.WXPayEntryActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrepayActivity extends BaseActivity {

	private TextView title;
	private LinearLayout backLayout;
	private String zhifuType = "accountpay";
	private CheckBox carcapay_checkbox;
	private CheckBox accountpay_checkbox;
	// private EditText kanumber_edt;
	private Button recharge_btn;
	private int needpay;
	private int useryue;
	private String dingdanid;
	private String kanumString;
	private String pwdString;
	private EditText kanum_ed;
	private EditText pwd_ed;
	private String type;
	private TextView odrernum;
	private TextView servicetype;
	private TextView paymoney;
	private TextView yue_tv;
	private String dingdanhao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prepay_layout);
		title = (TextView) findViewById(R.id.title);
		title.setText("订单支付");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		needpay = getIntent().getIntExtra("needpay", 0);
		useryue = getIntent().getIntExtra("useryue", 0);
		dingdanid = getIntent().getStringExtra("Dingdanid");
		type = getIntent().getStringExtra("servicetype");
		dingdanhao = getIntent().getStringExtra("Dingdanhao");
		initView();

	}
	private void initView() {
		odrernum = (TextView) findViewById(R.id.odrernum_tv);
		odrernum.setText(dingdanhao);
		servicetype = (TextView) findViewById(R.id.servicetype_tv);
		servicetype.setText(type);
		paymoney = (TextView) findViewById(R.id.paymoney_tv);
		paymoney.setText(needpay+"元");
		yue_tv = (TextView) findViewById(R.id.yue_tv);
		yue_tv.setText(useryue+"元");
		
		accountpay_checkbox = (CheckBox) findViewById(R.id.accountpay_checkbox);
		carcapay_checkbox = (CheckBox) findViewById(R.id.carcapay_checkbox);
		accountpay_checkbox.setChecked(true);
		carcapay_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							accountpay_checkbox.setChecked(false);
							zhifuType = "carcapay";
							showlogindialog();
						}
					}

				});
		accountpay_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							carcapay_checkbox.setChecked(false);
							zhifuType = "accountpay";
						}

					}
				});
		recharge_btn = (Button) findViewById(R.id.recharge_go);
		recharge_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (zhifuType.equals("accountpay")) {
					accountpay();

				} else if (zhifuType.equals("carcapay")) {
					if (kanumString != null && pwdString != null) {
						carcapay();
					} else {
						Toast.makeText(PrepayActivity.this, "请输入卡号和密码",
								Toast.LENGTH_SHORT).show();
					}

				}
			}

		});
	}
	private void accountpay() {
		if ((useryue < needpay) || (useryue == 0)) {
			AlertDialog.Builder builder = new Builder(
					PrepayActivity.this);
			builder.setTitle("提示");
			builder.setMessage("您的余额不足请充值");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Intent intent = new Intent(
									PrepayActivity.this,
									WXPayEntryActivity.class);
							startActivity(intent);
						}

					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			builder.show();
			return;
		} else {
			SharedPreferences settings = getSharedPreferences(
					"setting", 0);
			String userid = settings.getString("userid", "");
			FinalHttp fp = new FinalHttp();
			AjaxParams params = new AjaxParams();
			params.put("userid", userid);
			params.put("dingdanid", dingdanid);
			params.put("payyue", String.valueOf(needpay));
			params.put("act", "postok");
			String baseurl = settings.getString("baseurl",
					"http://wzd.k76.net");

			fp.post(baseurl + "/api/yufufei.php", params,
					new AjaxCallBack<Object>() {
						@Override
						public void onSuccess(Object t) {
							Gson gson = new Gson();
							DriveBean bean = gson.fromJson(
									t.toString(), DriveBean.class);
							Toast.makeText(PrepayActivity.this,
									bean.getMsg(), Toast.LENGTH_SHORT)
									.show();
							if ("success".equals(bean.getRet())) {
								Intent intent = new Intent(
										PrepayActivity.this,
										PaiDanActivity.class);
								intent.putExtra("Dingdanid",
										bean.getDingdanid());
								intent.putExtra("money", needpay);
								intent.putExtra("from", "order");
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
								startActivity(intent);
								finish();
							}

						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(PrepayActivity.this,
									"下单失败，请稍后再试", Toast.LENGTH_SHORT)
									.show();
						}

					});
		}
	}
	private void carcapay() {
		SharedPreferences settings = getSharedPreferences("setting", 0);
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("dingdanid", dingdanid);
		if (kanumString != null) {
			params.put("daijiakahao", kanumString);
		}
		if (pwdString != null) {
			params.put("daijiakamima", pwdString);
		}

		params.put("act", "postok");
		String baseurl = settings.getString("baseurl",
				"http://wzd.k76.net");
		fp.post(baseurl + "/api/yufufei.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						DriveBean bean = gson.fromJson(t.toString(),
								DriveBean.class);
						Toast.makeText(PrepayActivity.this,
								bean.getMsg(), Toast.LENGTH_SHORT)
								.show();
						if ("success".equals(bean.getRet())) {
							Intent intent = new Intent(
									PrepayActivity.this,
									PaiDanActivity.class);
							intent.putExtra("Dingdanid",
									bean.getDingdanid());
							intent.putExtra("money", needpay);
							intent.putExtra("from", "order");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
							startActivity(intent);
							finish();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(PrepayActivity.this,
								"下单失败，请稍后再试", Toast.LENGTH_SHORT)
								.show();
					}

				});
	}
	// 正则表达式验证
	public static boolean checkString(String s, String regex) {
		return s.matches(regex);
	}

	public void showlogindialog() {
		// TODO Auto-generated method stub
		// LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
		LayoutInflater factory = LayoutInflater.from(PrepayActivity.this);
		// 把activity_login中的控件定义在View中
		final View textEntryView = factory.inflate(
				R.layout.login_dialog_layout, null);
		AlertDialog.Builder builder = new Builder(PrepayActivity.this);
		builder.setView(textEntryView);
		builder.setTitle("洗车卡支付");
		kanum_ed = (EditText) textEntryView.findViewById(R.id.kanum_edit);

		pwd_ed = (EditText) textEntryView.findViewById(R.id.password_edit);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(kanum_ed.getText()) || TextUtils.isEmpty(pwd_ed.getText())) {
					Toast.makeText(PrepayActivity.this, "请输入卡号和密码",
							Toast.LENGTH_SHORT).show();
				}else {
					kanumString = kanum_ed.getText().toString();
					pwdString = pwd_ed.getText().toString();
				}
				
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
