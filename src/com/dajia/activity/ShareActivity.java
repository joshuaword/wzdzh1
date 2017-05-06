package com.dajia.activity;

import com.dajia.Bean.ShareBean;
import com.dajia.Bean.UserBean;
import com.dajia.constant.Constant;
import com.dajia.util.L;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.k76.wzd.R;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class ShareActivity extends BaseActivity {
	
	private TextView title;
	private LinearLayout backLayout;
	private RelativeLayout downLayout;
	private RelativeLayout publicLayout;
	FinalBitmap finalBitMap;
	ImageView imageView;
	View topView;
	ImageView downImage;
	ImageView publicImage;
	TextView downTextView;
	TextView publicTextView;
	TextView btnRight;
	
	String shareContent;
	String sharePicUrl;
	String shareUrl;
	String shareTitle;
	String dengluhao;
	
	TextView fenxiangView;
	String userid;
	
	UserBean userBean;
	String liantuUrl;
	String weiXinliantuUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		SharedPreferences sp = getSharedPreferences("setting", 0);
	    dengluhao = sp.getString("dengluhao", "");
	    userid = sp.getString("userid", "");
	    String baseurl = sp.getString("baseurl", "http://wzd.k76.net");
	    
	    //String fenxiangString = sp.getString("fenxiang", "");
		
		title = (TextView) findViewById(R.id.title);
		title.setText("分享");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		fenxiangView = (TextView) findViewById(R.id.fenxiang);
		//fenxiangView.setText(fenxiangString);
		fenxiangView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ShareActivity.this,FenxiangUrlHtmlActivity.class);
				startActivity(intent);
			}
		});
		
		btnRight = (TextView) findViewById(R.id.btnRight);
		btnRight.setVisibility(View.GONE);
		/*btnRight.setBackgroundResource(R.drawable.share);
		btnRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			}
		});*/
		
		imageView = (ImageView) findViewById(R.id.img_qrcode);
		finalBitMap=FinalBitmap.create(ShareActivity.this);
		
		topView = findViewById(R.id.view_topbutton_back);
		
		downImage = (ImageView) findViewById(R.id.imageview_download);
		publicImage = (ImageView) findViewById(R.id.imageview_public_account);
		
		downTextView = (TextView) findViewById(R.id.btn_download);
		publicTextView = (TextView) findViewById(R.id.btn_public_account);
		downImage.setBackgroundResource(R.drawable.share_download_hl);
		publicImage.setBackgroundResource(R.drawable.share_weixin);
		downLayout = (RelativeLayout) findViewById(R.id.layout_download);
		publicLayout = (RelativeLayout) findViewById(R.id.layout_public_account);
		//finalBitMap.display(imageView, "http://wzd.k76.net/m/info/t/liantu.png");
		downLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//imageView.setBackgroundResource(R.drawable.logo);
				topView.setBackgroundResource(R.drawable.share_topbutton_back_app);
				downImage.setBackgroundResource(R.drawable.share_download_hl);
				publicImage.setBackgroundResource(R.drawable.share_weixin);
				downTextView.setTextColor(getResources().getColor(R.color.white));
				publicTextView.setTextColor(getResources().getColor(R.color.segment_button_color));
				finalBitMap.display(imageView, liantuUrl);
			}
		});
		publicLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				topView.setBackgroundResource(R.drawable.share_topbutton_back_public_account);
				downImage.setBackgroundResource(R.drawable.share_download);
				publicImage.setBackgroundResource(R.drawable.share_weixin_hl);
				downTextView.setTextColor(getResources().getColor(R.color.segment_button_color));
				publicTextView.setTextColor(getResources().getColor(R.color.white));
				finalBitMap.display(imageView, weiXinliantuUrl);
			}
		});
		
		FinalHttp fp = new FinalHttp();
        AjaxParams params = new AjaxParams();
        params.put("userid", userid);
        params.put("act", "postok");
        fp.post(baseurl +"/api/userinfoclient.php", params, new AjaxCallBack<Object>() {
        	@Override
        	public void onSuccess(Object t) {
        		Gson gson = new Gson();
				userBean = gson.fromJson(t.toString(), UserBean.class);
				if (userBean != null) {
					if (!TextUtils.isEmpty(userBean.getFenxiang())) {
						fenxiangView.setText(userBean.getFenxiang());
					}
					liantuUrl = userBean.getLiantu();
					weiXinliantuUrl = userBean.getWeixinliantu();
					finalBitMap.display(imageView, liantuUrl);
				}
        	}
        	
        	@Override
        	public void onFailure(Throwable t, int errorNo, String strMsg) {
        		super.onFailure(t, errorNo, strMsg);
        		Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
        	}
		});
		
		queryShare();
		
	}
	
	public void queryShare(){
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("dengluhao", dengluhao);
		ap.put("act", "postok");
		
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		
		fp.post(baseurl + "/api/fenxiangclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();

						ShareBean shareBean = gson.fromJson(t.toString(),
								ShareBean.class);
						shareContent = shareBean.getFx_content();
						shareUrl = shareBean.getFx_url();
						sharePicUrl = shareBean.getFx_logo();
						shareTitle = shareBean.getFx_title();
						share();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						L.e("aaaa", t.toString() + "----------" + errorNo
								+ "------" + strMsg);
					}
				});
	}
	
	public void share() {
		
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent(shareContent + shareUrl + "");
		// 设置分享图片, 参数2为图片的url地址
		mController
				.setShareMedia(new UMImage(ShareActivity.this, sharePicUrl));
		// 设置分享图片，参数2为本地图片的资源引用
		// mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.icon));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		// mController.setShareMedia(new UMImage(getActivity(),
		// BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));
		// 选择平台
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN, SHARE_MEDIA.EMAIL,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE,SHARE_MEDIA.TENCENT);
		// 设置分享平台面板中显示的平台
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.SMS,
				  SHARE_MEDIA.WEIXIN);
		
		
		SmsHandler shandler = new SmsHandler();// 短信分享
		shandler.addToSocialSDK();
		
		
		String appId = Constant.WEIXINID;
		String appSecret = "ac6b8f7d1ab6dea9c66326a44788cb85";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ShareActivity.this, appId,
				appSecret);
		wxHandler.addToSocialSDK();
		
		
		//设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置分享文字
		weixinContent.setShareContent(shareContent + shareUrl + "");
		//设置title
		weixinContent.setTitle(shareTitle);
		//设置分享内容跳转URL
		weixinContent.setTargetUrl(shareUrl);
		//设置分享图片
		weixinContent.setShareImage(new UMImage(ShareActivity.this, sharePicUrl));
		mController.setShareMedia(weixinContent);
		
		//参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ShareActivity.this, "1103967351",
		                "rsiGc3BHWZ1FkYbp");
		qqSsoHandler.addToSocialSDK(); 
		
		QQShareContent qqShareContent = new QQShareContent();
		//设置分享文字
		qqShareContent.setShareContent(shareContent);
		//设置分享title
		qqShareContent.setTitle("我知道");
		//设置分享图片
		qqShareContent.setShareImage(new UMImage(ShareActivity.this, sharePicUrl));
		//设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(shareUrl);
		mController.setShareMedia(qqShareContent);
		

		mController.openShare(ShareActivity.this, false);
	}
}
