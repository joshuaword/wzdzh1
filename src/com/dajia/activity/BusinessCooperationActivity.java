package com.dajia.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import net.k76.wzd.R;

public class BusinessCooperationActivity extends BaseActivity {

	private WebView tqWebView;

	LinearLayout btnLayout;
	LinearLayout backLayout;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busycooper);

		btnLayout = (LinearLayout) findViewById(R.id.btn_layout);
		backLayout = (LinearLayout) findViewById(R.id.back_layout);

		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		tqWebView = (WebView) findViewById(R.id.tq);

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("shangwu",
				"http://www.chinajiujia.cc/bd.php");
		tqWebView.loadUrl(baseurl);

		tqWebView.getSettings().setJavaScriptEnabled(true);
		tqWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		tqWebView.setWebChromeClient(new WebChromeClient());
	}
}
