package com.dajia.activity;

import java.io.File;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dajia.VehicleApp;
import com.dajia.Bean.UpDateInfo;
import com.dajia.util.Assign_UpDateDialog;
import com.dajia.util.ConfirmDialogListener;
import com.google.gson.Gson;
import com.twzs.core.download.Downloadhelper;

public class SettingActivity extends BaseActivity {

	private TextView title;
	private LinearLayout backLayout;
	private RelativeLayout share;
	String shareContent;
	String sharePicUrl;
	String shareUrl;
	String shareTitle;
	private UpDateInfo upDateBean;
	RelativeLayout aboutLayout;
	public static final String TEMPPATH = "temp";
	private RelativeLayout protocol,protocol1,protocol2,protocol3,protocol4;
	String userid;
	String phoneString;

	RelativeLayout phoneLayout;
	private String appbanquan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		title = (TextView) findViewById(R.id.title);
		title.setText("更多");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		String ver = getAppVersionName(this.getApplication());
		TextView vertext = (TextView) findViewById(R.id.text_ver);
		vertext.setText(ver);

		TextView phone = (TextView) findViewById(R.id.text_phone);
		TextView text_shangwuhezuo = (TextView) findViewById(R.id.text_shangwuhezuo);
		TextView text_appbanqan = (TextView) findViewById(R.id.text_banquan);

		phoneLayout = (RelativeLayout) findViewById(R.id.layout_phone);

		SharedPreferences sp = getSharedPreferences("setting", 0);
		phoneString = sp.getString("kefuphone", "");
		appbanquan = sp.getString("appbanquan", "");
		text_appbanqan.setText(appbanquan);
		
		userid = sp.getString("userid", "");
		phone.setText(phoneString);
		text_shangwuhezuo.setText("商务合作(" + userid + ")");

		phoneLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ phoneString));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		aboutLayout = (RelativeLayout) findViewById(R.id.layout_about);
		aboutLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						BusinessCooperationActivity.class);
				startActivity(intent);
			}
		});

		RelativeLayout checkLayout = (RelativeLayout) findViewById(R.id.layout_checkversion);
		checkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkUPDATE();
				// Intent intent = new Intent(SettingActivity.this,
				// CheckHtmlActivity.class);
				// startActivity(intent);
			}
		});

		// share = (RelativeLayout) findViewById(R.id.layout_inviteperson);
		/*
		 * share.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * FinalHttp fp = new FinalHttp(); AjaxParams ap = new AjaxParams();
		 * ap.put("dengluhao", "45"); ap.put("act", "postok");
		 * fp.post("http://zgjj.k76.net/api/fenxiangclient.php", ap, new
		 * AjaxCallBack<Object>() {
		 * 
		 * @Override public void onSuccess(Object t) { Gson gson = new Gson();
		 * 
		 * ShareBean shareBean = gson.fromJson(t.toString(), ShareBean.class);
		 * shareContent = shareBean.getFx_content(); shareUrl =
		 * shareBean.getFx_url(); sharePicUrl = shareBean.getFx_logo();
		 * shareTitle = shareBean.getFx_title(); share(); }
		 * 
		 * @Override public void onFailure(Throwable t, int errorNo, String
		 * strMsg) { L.e("aaaa", t.toString() + "----------" + errorNo +
		 * "------" + strMsg); } });
		 * 
		 * } });
		 */

		protocol = (RelativeLayout) findViewById(R.id.layout_protocol);
		protocol1 = (RelativeLayout) findViewById(R.id.layout_protocol1);
		protocol2 = (RelativeLayout) findViewById(R.id.layout_protocol2);
		protocol3 = (RelativeLayout) findViewById(R.id.layout_protocol3);
		protocol4 = (RelativeLayout) findViewById(R.id.layout_protocol4);
		protocol.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						LicenseActivity.class);
				intent.putExtra("flag", true);
				startActivity(intent);
			}
		});
		protocol1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", VehicleApp.getInstance().getSetBean().getAndroidstore()+"?n="+userid);
				intent.putExtra("title", "评分");
				startActivity(intent);
			}
		});
		protocol2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", VehicleApp.getInstance().getSetBean().getWentifankui()+"?n="+userid);
				intent.putExtra("title", "问题反馈");
				startActivity(intent);
			}
		});
		protocol3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", VehicleApp.getInstance().getSetBean().getGerenzhongxin()+"?n="+userid);
				intent.putExtra("title", "个人中心");
				startActivity(intent);
			}
		});
		protocol4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", VehicleApp.getInstance().getSetBean().getGongnengjieshao()+"?n="+userid);
				intent.putExtra("title", "新版本说明");
				startActivity(intent);
			}
		});
	}

	/*
	 * public void share() {
	 * 
	 * // 首先在您的Activity中添加如下成员变量 final UMSocialService mController =
	 * UMServiceFactory .getUMSocialService("com.umeng.share"); // 设置分享内容
	 * mController.setShareContent(shareContent + shareUrl + ""); // 设置分享图片,
	 * 参数2为图片的url地址 mController .setShareMedia(new UMImage(SettingActivity.this,
	 * sharePicUrl)); // 设置分享图片，参数2为本地图片的资源引用 // mController.setShareMedia(new
	 * UMImage(getActivity(), // R.drawable.icon)); // 设置分享图片，参数2为本地图片的路径(绝对路径)
	 * // mController.setShareMedia(new UMImage(getActivity(), //
	 * BitmapFactory.decodeFile("/mnt/sdcard/icon.png"))); // 选择平台
	 * mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
	 * SHARE_MEDIA.DOUBAN,
	 * SHARE_MEDIA.EMAIL,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA
	 * .QZONE,SHARE_MEDIA.TENCENT); // 设置分享平台面板中显示的平台
	 * mController.getConfig().setPlatformOrder
	 * (SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN);
	 * 
	 * 
	 * SmsHandler shandler = new SmsHandler();// 短信分享 shandler.addToSocialSDK();
	 * 
	 * 
	 * String appId = Constant.WEIXINID; String appSecret =
	 * "e4b074d5fcbc366a8778adc11e527af8"; // 添加微信平台 UMWXHandler wxHandler = new
	 * UMWXHandler(SettingActivity.this, appId, appSecret);
	 * wxHandler.addToSocialSDK();
	 * 
	 * 
	 * //设置微信好友分享内容 WeiXinShareContent weixinContent = new WeiXinShareContent();
	 * //设置分享文字 weixinContent.setShareContent(shareContent + shareUrl + "");
	 * //设置title weixinContent.setTitle(shareTitle); //设置分享内容跳转URL
	 * weixinContent.setTargetUrl(shareUrl); //设置分享图片
	 * weixinContent.setShareImage(new UMImage(SettingActivity.this,
	 * sharePicUrl)); mController.setShareMedia(weixinContent);
	 * 
	 * //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
	 * UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(SettingActivity.this,
	 * "1103967351", "rsiGc3BHWZ1FkYbp"); qqSsoHandler.addToSocialSDK();
	 * 
	 * QQShareContent qqShareContent = new QQShareContent(); //设置分享文字
	 * qqShareContent.setShareContent(shareContent); //设置分享title
	 * qqShareContent.setTitle("我知道"); //设置分享图片
	 * qqShareContent.setShareImage(new UMImage(SettingActivity.this,
	 * sharePicUrl)); //设置点击分享内容的跳转链接 qqShareContent.setTargetUrl(shareUrl);
	 * mController.setShareMedia(qqShareContent);
	 * 
	 * 
	 * mController.openShare(SettingActivity.this, false); }
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			// versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	private void checkUPDATE() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		params.put("appname", "android");
		fp.post(baseurl + "/api/shengjiclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						upDateBean = gson.fromJson(t.toString(),
								UpDateInfo.class);
						if (upDateBean != null) {
							if (!upDateBean.getVer().equals("7.4.20170419")) {
								showUpdate_Dialog(upDateBean.getVer(),
										upDateBean.getUrl(),
										upDateBean.getApk());
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

	/***
	 * @return
	 */
	public static final String getAppSDPath() {
		File file = new File(Environment.getExternalStorageDirectory(), "apk");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static final String getAppTmpSDPath() {
		File file = new File(getAppSDPath(), TEMPPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static String getFileNameFromUrl(String s) {
		int i = s.lastIndexOf("\\");
		if (i < 0 || i >= s.length() - 1) {
			i = s.lastIndexOf("/");
			if (i < 0 || i >= s.length() - 1)
				return s;
		}
		return s.substring(i + 1);
	}

	/**
	 * 显示升级对话框
	 */
	public void showUpdate_Dialog(String newVersion, final String downUrl,
			final String isAPK) {
		String newverStr = "有新版本更新啦！";
		final String version_downloading = this.getResources().getString(
				R.string.version_downloading, newVersion);
		final String installapkfile = getAppTmpSDPath() + "/"
				+ getFileNameFromUrl(downUrl);
		Assign_UpDateDialog.showUpdateDialog(this, newverStr, "增加新功能，优化性能！",
				"立即尝鲜", "残忍拒绝", new ConfirmDialogListener() {
					@Override
					public void onPositive(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (isAPK.equals("yes")) {
							Downloadhelper dm = new Downloadhelper(
									SettingActivity.this, true);
							dm.downLoadFile(version_downloading, downUrl,
									installapkfile);
							dialog.cancel();
						} else {
							if (downUrl != null && !downUrl.equals("")) {
								// Uri uri = null;
								// uri = Uri.parse(downUrl);
								// Intent intent = new
								// Intent(Intent.ACTION_VIEW,
								// uri);
								// startActivity(intent);
								Intent intent = new Intent(
										SettingActivity.this,
										CheckHtmlActivity.class);
								intent.putExtra("baseurl", downUrl);
								startActivity(intent);
							}
						}

					}

					@Override
					public void onNegative(DialogInterface dialog) {
						dialog.cancel();
					}
				});

	}

}