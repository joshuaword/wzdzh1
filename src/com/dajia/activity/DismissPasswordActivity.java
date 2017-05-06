package com.dajia.activity;

import com.dajia.Bean.MessageBean;
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
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DismissPasswordActivity extends BaseActivity {
	private TextView title;
	private LinearLayout backLayout;
	private Button sumbit_go;
	public static final String SP_KEY_PHONE = "sp_phone";
	private EditText phone_ed;
	private EditText password_ed, yanzhenmaEdit;
	private SharedPreferences sp;
	private Button codeBtn;
	private TimeCount mTime;
	private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
	private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间
	private String baseurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dismisspw);

		title = (TextView) findViewById(R.id.title);
		title.setText("修改密码");
		// 返回
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mTime = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
		sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
		String phone = getIntent().getStringExtra("phone");
		baseurl = sp.getString("baseurl", "http://wzd.k76.net");

		phone_ed = (EditText) findViewById(R.id.dlogin_phone);
		phone_ed.setText(phone);
		phone_ed.setFocusableInTouchMode(false);

		password_ed = (EditText) findViewById(R.id.dedt_password);

		yanzhenmaEdit = (EditText) findViewById(R.id.dvalicode_tv);
		codeBtn = (Button) findViewById(R.id.dget_valicode);
		codeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mTime.start();
				yanzhen("01");
			}
		});

		sumbit_go = (Button) findViewById(R.id.btn_sureedit);
		sumbit_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = sp.edit();
				editor.putString(SP_KEY_PHONE, phone_ed.getText().toString()
						.trim());
				editor.putString("sp_password", password_ed.getText()
						.toString().trim());
				editor.commit();
				yanzhen("02");
			}

		});

	}

	/**
	 * 输入手机号验证
	 * 
	 * @param flag
	 */
	public void yanzhen(final String flag) {
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("telphone", phone_ed.getText().toString().trim());

		if ("01".equals(flag)) {
			params.put("yanzhenma", "");
		}

		if ("02".equals(flag)) {
			if (TextUtils.isEmpty(yanzhenmaEdit.getText().toString().trim())) {
				Toast.makeText(DismissPasswordActivity.this, "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			params.put("yanzhenma", yanzhenmaEdit.getText().toString().trim());
		}

		params.put("act", "postok");

		Log.d("jiang", baseurl + "/api/askyanzhenmaclient.php" + " params:"
				+ params);
		fp.post(baseurl + "/api/askyanzhenmaclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						if (t == null) {
							Toast.makeText(DismissPasswordActivity.this,
									"服务器未返回数据", Toast.LENGTH_SHORT).show();
							return;
						}
						Gson gson = new Gson();
						MessageBean msg = gson.fromJson(t.toString(),
								MessageBean.class);
						if (msg != null) {
							Toast.makeText(DismissPasswordActivity.this,
									msg.getMsg(), Toast.LENGTH_SHORT).show();

							if ("01".equals(flag))
								return;

							String theret = msg.getRet();
							// 如果返回success，把手机号和userid放入本地
							if (theret.equals("success")) {
								SharedPreferences sp = getSharedPreferences(
										"setting", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, phone_ed
										.getText().toString().trim());

								editor.putString("userid", msg.getUserid());
								editor.commit();
								SubmitEdit();
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						cancelTimer();
					}
				});
	}

	// 提交修改信息
	public void SubmitEdit() {
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("telphone", phone_ed.getText().toString().trim());
		params.put("passwd", password_ed.getText().toString().trim());
		params.put("act", "postok");
		fp.post(baseurl + "/api/registerclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Gson gson = new Gson();
						UserBean bean = gson.fromJson(t.toString(),
								UserBean.class);
						if (bean != null) {
							if (bean.getRet().equals("success")) {							
								Intent intent = new Intent(
										DismissPasswordActivity.this,
										ChatLoginActivity.class);
								startActivity(intent);
							} else {
								Toast.makeText(DismissPasswordActivity.this,
										bean.getRet(), Toast.LENGTH_LONG)
										.show();
							}
						}
					}
				});
	}

	private void cancelTimer() {
		mTime.cancel();
		codeBtn.setText("获取验证码");
		codeBtn.setEnabled(true);
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			codeBtn.setText(millisUntilFinished / COUNT_DOWN_INTERVAL + "秒后重发");
			codeBtn.setEnabled(false);
		}

		@Override
		public void onFinish() {
			codeBtn.setText("获取验证码");
			codeBtn.setEnabled(true);
		}

	}

}
