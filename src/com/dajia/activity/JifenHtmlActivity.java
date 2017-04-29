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
import android.widget.TextView;

import net.k76.wzd.R;

@SuppressLint("SetJavaScriptEnabled")
public class JifenHtmlActivity extends BaseActivity {

	private WebView tqWebView;
	String dengluhao;
	private TextView title;
	private LinearLayout backLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhifu);
		
		title = (TextView) findViewById(R.id.title);
		title.setText("我的账户");
		String userid = getIntent().getStringExtra("userid");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		SharedPreferences sp = getSharedPreferences("setting", 0);
	    dengluhao = sp.getString("dengluhao", "");
	    
		tqWebView = (WebView) findViewById(R.id.tq);

		
		String baseurl = sp.getString("baseurl", "http://wzd.k76.net");
		tqWebView.loadUrl(baseurl + "/api/myjifen.php?userid=" + userid);
		
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
