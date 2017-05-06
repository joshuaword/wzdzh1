package com.dajia.activity;

import java.util.Map;
import java.util.Set;

import com.daijia.chat.ChatActivity;
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
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
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

public class ChatLoginActivity extends BaseActivity {
	private TextView title;
	private LinearLayout backLayout;
	private Button login_go;
	private Button regist_go;
	public static final String SP_KEY_PHONE = "sp_phone";
	private EditText phone_ed;
	private EditText password_ed;
	private TextView dismisspw;
	private String isEmail;
	Button btn_qq, btn_weixin;
	private SharedPreferences sp;
	private UMSocialService mController = UMServiceFactory.getUMSocialService(Constant.DESCRIPTOR);
	UserBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		title = (TextView) findViewById(R.id.title);
		title.setText(getString(R.string.check_and_login));
		isEmail = VehicleApp.getInstance().getSetBean().getClientusername();
		btn_qq = (Button) findViewById(R.id.btn_qq);
		btn_weixin = (Button) findViewById(R.id.btn_weixin);

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
		if (isEmail!=null&&isEmail.equals("email")) {
			phone_ed.setHint("请输入邮箱");
		}
		phone_ed.setText(phone);
		password_ed = (EditText) findViewById(R.id.edt_password);
		dismisspw = (TextView) findViewById(R.id.dismiss_password);
		dismisspw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatLoginActivity.this, DismissPasswordActivity.class);
				intent.putExtra("phone", phone_ed.getText().toString());
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
				params.put("act", "postok");
				fp.post(baseurl + "/api/applogin.php", params, new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Log.e("loginsuccess", t.toString());
						Gson gson = new Gson();
						bean= gson.fromJson(t.toString(), UserBean.class);
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
								Intent intent = new Intent(ChatLoginActivity.this, ChatActivity.class);
								startActivity(intent);
								finish();
							} else {
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
				Intent intent = new Intent(ChatLoginActivity.this, RegiterActivity.class);
				startActivity(intent);
			}
		});

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN,
				SHARE_MEDIA.EMAIL, SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.GOOGLEPLUS,
				SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.LAIWANG, SHARE_MEDIA.LAIWANG_DYNAMIC, SHARE_MEDIA.LINKEDIN,
				SHARE_MEDIA.PINTEREST, SHARE_MEDIA.POCKET, SHARE_MEDIA.SMS, SHARE_MEDIA.TWITTER, SHARE_MEDIA.YIXIN,
				SHARE_MEDIA.YIXIN_CIRCLE, SHARE_MEDIA.YNOTE);
		mController.openShare(ChatLoginActivity.this, false);
		addQQQZonePlatform();
		addWXPlatform();
		btn_qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// login(SHARE_MEDIA.QQ);
				QQLogin(v);
			}
		});
		btn_weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WeiXinlogin(SHARE_MEDIA.WEIXIN);
			}
		});
	}

	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx34e695311ba607b9";
		String appSecret = "6504e0056d1a817dd1215ba4bbf43859";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ChatLoginActivity.this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ChatLoginActivity.this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private void addQQQZonePlatform() {
		String appId = "101399822";
		String appKey = "0541a6ed98298b35f873153352806ef9";
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ChatLoginActivity.this, appId, appKey);
		qqSsoHandler.addToSocialSDK();

	}

	private void WeiXinlogin(final SHARE_MEDIA platform) {
		mController.doOauthVerify(ChatLoginActivity.this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				// 获取uid
				String uid = value.getString("uid");
				Log.e("***uid***", uid);
				if (!TextUtils.isEmpty(uid)) {
					// uid不为空，获取用户信息
					SendUid( "", uid);
//					getUserInfo(platform);
				} else {
					Toast.makeText(ChatLoginActivity.this, "授权失败...", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void QQLogin(View v) {
		mController.doOauthVerify(ChatLoginActivity.this, SHARE_MEDIA.QQ, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权开始", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权完成", Toast.LENGTH_SHORT).show();
				String uid = value.getString("uid");
				// 如果授权完成，则获取授权平台的用户信息
				if (!TextUtils.isEmpty(uid)) {
					SendUid( uid, "");
//					getUserInfo(SHARE_MEDIA.QQ);
				} else {
					Toast.makeText(ChatLoginActivity.this, "授权失败", 0).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				Toast.makeText(ChatLoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
			}
		});
	}

    private void getUserInfo(SHARE_MEDIA platform) {
        mController.getPlatformInfo(ChatLoginActivity.this, SHARE_MEDIA.SINA,
                new UMDataListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(ChatLoginActivity.this, "获取平台数据开始...",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                sb.append(key + "=" + info.get(key).toString()
                                        + "\r\n");
                            }
                            Log.v("TAG", sb.toString());
                        } else {
                            Log.v("TAG", "发生错误：" + status);
                        }
                    }
                });
        
    }



	// 如果有使用任一平台的SSO授权, 则必须在对应的activity中实现onActivityResult方法, 并添加如下代码
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(resultCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	

	private void SendUid(final String qquid,final String weixinuid) {
		Log.e("***qquid***", qquid);
		Log.e("***weixinuid***", weixinuid);
		String baseurl = sp.getString("baseurl", "http://wzd.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("qquid", qquid);
		params.put("weixinuid", weixinuid);	
		params.put("act", "postok");
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						Log.e("aaaaaaaaaaaa", t.toString());
						bean = gson.fromJson(t.toString(), UserBean.class);
						if (bean != null) {
							if(bean.getRet().equalsIgnoreCase("success")){
								Log.e("userBean.getUserid()",bean.getUserid());
								SharedPreferences.Editor editor = sp.edit();
								editor.putString(SP_KEY_PHONE, bean.getTelphone());
								editor.putString("userid", bean.getUserid());
								editor.putString("nickname", bean.getNickname());
								editor.putString("sex", bean.getSex());
								editor.putString("stryouhuijuan", bean.getStryouhuijuan());
								editor.putString("headimgpath", bean.getThefile());
								editor.putString("kehuleixing", bean.getKehuleixing());
								editor.putString("qquid", qquid);
								editor.putString("weixinuid", weixinuid);
								editor.commit();
								Intent intent = new Intent(ChatLoginActivity.this, ChatActivity.class);
								startActivity(intent);
								finish();
							}else{
								if(!qquid.equals("")){
									Toast.makeText(ChatLoginActivity.this, "您的QQ还未绑定，请先用手机号码注册登录!", Toast.LENGTH_SHORT).show();
								}else 	if(!weixinuid.equals("")){
									Toast.makeText(ChatLoginActivity.this, "您的微信还未绑定，请先用手机号码注册登录!", Toast.LENGTH_SHORT).show();
								}
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
}
