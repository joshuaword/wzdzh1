package com.dajia.activity;

import java.util.Map;

import com.dajia.VehicleApp;
import com.dajia.Bean.UserBean;
import com.dajia.constant.Constant;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class OldLoginActivity extends BaseActivity {
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
	Button btn_qq, btn_weixin;
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constant.DESCRIPTOR);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		isEmail = VehicleApp.getInstance().getSetBean().getClientusername();
		Log.d("isEmail", isEmail);
		title = (TextView) findViewById(R.id.title);
		btn_qq = (Button) findViewById(R.id.btn_qq);
		btn_weixin = (Button) findViewById(R.id.btn_weixin);
		btn_qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login(SHARE_MEDIA.QQ);
			}
		});
		btn_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login(SHARE_MEDIA.WEIXIN);
			}
		});
		title.setText(getString(R.string.check_and_login));
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
		if (isEmail.equals("email")) {
			phone_ed.setHint("请输入邮箱");
		}

		phone_ed.setText(phone);

		password_ed = (EditText) findViewById(R.id.edt_password);
		dismisspw = (TextView) findViewById(R.id.dismiss_password);
		dismisspw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OldLoginActivity.this, DismissPasswordActivity.class);
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
				editor.putString(SP_KEY_PHONE, phone_ed.getText().toString().trim());
				editor.putString("sp_password", password_ed.getText().toString().trim());
				editor.commit();
				String baseurl = sp.getString("baseurl", "http://wzd.k76.net");
				FinalHttp fp = new FinalHttp();
				AjaxParams params = new AjaxParams();
				params.put("telphone", phone_ed.getText().toString().trim());
				params.put("password", password_ed.getText().toString().trim());
				params.put("qquid", sp.getString("qquid", ""));
				params.put("weixinuid", sp.getString("weixinuid", ""));
				params.put("act", "postok");
				fp.post(baseurl + "/api/applogin.php", params, new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.e("loginsuccess", t.toString());
						Gson gson = new Gson();
						UserBean bean = gson.fromJson(t.toString(), UserBean.class);
						if (bean != null) {
							if (bean.getRet().equals("success")) {
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, bean.getTelphone());
								editor.putString("userid", bean.getUserid());
								editor.putString("nickname", bean.getNickname());
								editor.putString("sex", bean.getSex());
								editor.putString("stryouhuijuan", bean.getStryouhuijuan());
								editor.putString("headimgpath", bean.getThefile());
								editor.putString("kehuleixing", bean.getKehuleixing());
								editor.commit();
								Intent intent = new Intent(OldLoginActivity.this, HomepageActivity.class);
								startActivity(intent);
							} else {
								Toast.makeText(OldLoginActivity.this, bean.getRet(), Toast.LENGTH_LONG).show();
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
				Intent intent = new Intent(OldLoginActivity.this, RegiterActivity.class);
				startActivity(intent);
			}
		});
		addQQQZonePlatform();
		addWXPlatform();
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.EMAIL, SHARE_MEDIA.EVERNOTE,
				SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.GOOGLEPLUS,
				SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.LAIWANG,
				SHARE_MEDIA.LAIWANG_DYNAMIC, SHARE_MEDIA.LINKEDIN,
				SHARE_MEDIA.PINTEREST, SHARE_MEDIA.POCKET, SHARE_MEDIA.SMS,
				SHARE_MEDIA.TWITTER, SHARE_MEDIA.YIXIN,
				SHARE_MEDIA.YIXIN_CIRCLE, SHARE_MEDIA.YNOTE);
		mController.openShare(OldLoginActivity.this, false);
	}

	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx34e695311ba607b9";
		String appSecret = "6504e0056d1a817dd1215ba4bbf43859";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(OldLoginActivity.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(OldLoginActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private void addQQQZonePlatform() {
		String appId = "101399822";
		String appKey = "0541a6ed98298b35f873153352806ef9";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(OldLoginActivity.this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(OldLoginActivity.this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}
	
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(OldLoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(OldLoginActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(OldLoginActivity.this, "授权失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 获取uid
						String uid = value.getString("uid");
						if (!TextUtils.isEmpty(uid)) {
							// uid不为空，获取用户信息
							getUserInfo(platform);
						} else {
							Toast.makeText(OldLoginActivity.this, "授权失败...",
									Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(OldLoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(OldLoginActivity.this, platform,
				new UMDataListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onComplete(int status, Map<String, Object> info) {
						// String showText = "";
						// if (status == StatusCode.ST_CODE_SUCCESSED) {
						// showText = "用户名：" +
						// info.get("screen_name").toString();
						// Log.d("#########", "##########" + info.toString());
						// } else {
						// showText = "获取用户信息失败";
						// }

						if (info != null) {
							Toast.makeText(OldLoginActivity.this, info.toString(),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

}
