package com.dajia.activity;

import net.k76.wzd.R;

import com.daijia.chat.ChatActivity;
import com.dajia.VehicleApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
/**
 * 协议
 * @author Administrator
 *
 */
public class LicenseActivity extends BaseActivity {

	private WebView tqWebView;

	private Button resufeBtn;

	private Button agreeBtn;

	LinearLayout btnLayout;

	LinearLayout backLayout;
	private boolean mIsLoginShow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use_license);

		Boolean flag = getIntent().getBooleanExtra("flag", false);
		mIsLoginShow = getIntent().getBooleanExtra(
				YanzhenActivity.KEY_LONGIN_FLAG, false);
		btnLayout = (LinearLayout) findViewById(R.id.btn_layout);

		if (flag) {
			backLayout = (LinearLayout) findViewById(R.id.back_layout);
			backLayout.setVisibility(View.VISIBLE);
			backLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					finish();
				}
			});

			btnLayout.setVisibility(View.GONE);
		}

		tqWebView = (WebView) findViewById(R.id.tq);

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		tqWebView.loadUrl(baseurl + "/m/license.php");
		tqWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});

		resufeBtn = (Button) findViewById(R.id.btn_refuse);
		agreeBtn = (Button) findViewById(R.id.btn_agree);

		resufeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mIsLoginShow) {
					setResult(Activity.RESULT_CANCELED);
					finish();
				} else {
					SharedPreferences settings = getSharedPreferences(
							"setting", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("isFirst", true);
					editor.commit();
					VehicleApp.getInstance().exit();
				}
			}
		});
		 

		agreeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mIsLoginShow) {
					setResult(Activity.RESULT_OK);
					finish();
				} else {
					SharedPreferences settings = getSharedPreferences(
							"setting", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("isFirst", false);
					editor.commit();
//					Intent intent = new Intent(LicenseActivity.this,
//							HomepageActivity.class);
					Intent intent = new Intent(LicenseActivity.this,
							ChatActivity.class);

					startActivity(intent);
					finish();
				}
			}
		});
		
		

	}
}
