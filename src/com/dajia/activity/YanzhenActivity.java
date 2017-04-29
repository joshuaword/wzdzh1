package com.dajia.activity;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.MessageBean;
import com.dajia.Bean.UserBean;
import com.google.gson.Gson;
/**
 * 登录
 * @author Administrator
 *
 */
public class YanzhenActivity extends BaseActivity {
	public static final String KEY_LONGIN_FLAG = "licence_show";
	public static final String SP_KEY_PHONE = "sp_phone";
	public static final String SP_KEY_ISCHECKED = "sp_is_checked";
	private static final int REQUEST_CODE = 99;
	private TextView title;
	private LinearLayout backLayout;
	EditText phoneEdit;
	EditText yanzhenmaEdit;
	Button codeBtn;
	Button yanzhenBtn;
	String userid;
	private static final long MILLIS_IN_FUTURE = 60000; // 总额时间数
	private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间
	private TimeCount mTime;
	private CheckBox mLicenceCheckBox;
	private boolean mIsCheck;
	private UserBean userBean;
	private String ifshowcheliang;
	private BroadcastReceiver refresh = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			finish();
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yanzhen);
		registerReceiver(refresh, new IntentFilter("finish"));
		SharedPreferences sp = getSharedPreferences("setting", 0);
		userid = sp.getString("userid", "");
		ifshowcheliang = sp.getString("ifshowcheliang", "no");
		mTime = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);

		title = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.check_and_login));
		//返回
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		phoneEdit = (EditText) findViewById(R.id.yedt_phone);
		yanzhenmaEdit = (EditText) findViewById(R.id.edt_verifycode);

		codeBtn = (Button) findViewById(R.id.btn_get_verifycode);
		yanzhenBtn = (Button) findViewById(R.id.btn_yanzhen);
		mLicenceCheckBox = (CheckBox) findViewById(R.id.licence_check);
		mLicenceCheckBox.setMovementMethod(LinkMovementMethod.getInstance());
		mLicenceCheckBox.setText(getString(R.string.licence_begin));
		mLicenceCheckBox.append(getClickableSpan());
		mLicenceCheckBox.setChecked(true);
		/*
		 * mLicenceCheckBox .setOnCheckedChangeListener(new
		 * OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { yanzhenBtn.setEnabled(isChecked); } });
		 */
		yanzhenBtn.setEnabled(mLicenceCheckBox.isChecked());
		phoneEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// ^1[3|4|5|8][0-9]\\d{8}$

				if (checkString(String.valueOf(arg0),
						"^1[3|4|5|7|8][0-9]\\d{8}$")) {
					codeBtn.setEnabled(true);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		codeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mTime.start();
				mIsCheck = false;
				yanzhen("01");
			}
		});

		//确定验证
		yanzhenBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(phoneEdit.getText().toString().trim())) {
					Toast.makeText(YanzhenActivity.this, "请填写手机号码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mIsCheck = true;
				yanzhen("02");
			}
		});

	}

	private SpannableString getClickableSpan() {

		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(YanzhenActivity.this,
						LicenseActivity.class);
				intent.putExtra(KEY_LONGIN_FLAG, true);
				startActivityForResult(intent, REQUEST_CODE);
			}
		};

		SpannableString spanableInfo = new SpannableString(
				YanzhenActivity.this.getString(R.string.licence_end));
		int start = 0;
		int end = spanableInfo.length();
		spanableInfo.setSpan(new Clickable(l), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanableInfo;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(refresh);
	}

	class Clickable extends ClickableSpan implements OnClickListener {
		private final View.OnClickListener mListener;

		public Clickable(View.OnClickListener l) {
			mListener = l;
		}

		@Override
		public void onClick(View v) {
			mListener.onClick(v);
		}
	}

	private void getInfo() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		SharedPreferences sp = getSharedPreferences("setting", 0);
		String dengluhao = sp.getString("dengluhao", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("act", "postok");
		Log.e("MUSIC", "LOGINAcount--baseurl=" + baseurl + " dengluhao="
				+ dengluhao);
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {
							Log.e("yyyyy+ifshowcheliang===", ifshowcheliang);
							if (TextUtils.isEmpty(userBean.getChepaihao())
									&& TextUtils.isEmpty(userBean.getChexing())&& "yes".equals(ifshowcheliang)) {
								Intent intent = new Intent(
										YanzhenActivity.this,
										VehicleActivity.class);
								intent.putExtra("from", "yanzhen");
								startActivity(intent);
							}
							finish();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			mLicenceCheckBox.setChecked(resultCode == Activity.RESULT_OK ? true
					: false);
		}
	}

	// 正则表达式验证
	public static boolean checkString(String s, String regex) {
		return s.matches(regex);
	}
/**
 * 输入手机号验证
 * @param flag
 */
	public void yanzhen(final String flag) {
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		Log.d("userid", "**********" + userid + "***************");
		params.put("telphone", phoneEdit.getText().toString().trim());

		if ("01".equals(flag)) {
			params.put("yanzhenma", "");
		}

		if ("02".equals(flag)) {
			if (TextUtils.isEmpty(yanzhenmaEdit.getText().toString().trim())) {
				Toast.makeText(YanzhenActivity.this, "请输入验证码",
						Toast.LENGTH_SHORT).show();
				return;
			}
			params.put("yanzhenma", yanzhenmaEdit.getText().toString().trim());
		}

		params.put("act", "postok");

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		Log.d("jiang", baseurl + "/api/askyanzhenmaclient.php" + " params:"
				+ params);
		fp.post(baseurl + "/api/askyanzhenmaclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						if (t == null) {
							Toast.makeText(YanzhenActivity.this, "服务器未返回数据",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Gson gson = new Gson();
						MessageBean msg = gson.fromJson(t.toString(),
								MessageBean.class);
						if (msg != null) {
							Toast.makeText(YanzhenActivity.this, msg.getMsg(),
									Toast.LENGTH_SHORT).show();

							if ("01".equals(flag))
								return;

							String theret = msg.getRet();
							//如果返回success，把手机号和userid放入本地
							if (theret.equals("success")) {
								SharedPreferences sp = getSharedPreferences(
										"setting", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, phoneEdit
										.getText().toString().trim());
								editor.putString("userid", msg.getUserid());
								editor.putBoolean(SP_KEY_ISCHECKED, true);
								editor.commit();
								getInfo();
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
