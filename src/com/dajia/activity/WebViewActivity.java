package com.dajia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import net.k76.wzd.R;
public class WebViewActivity extends BaseActivity {
	private WebView tqWebView;
	private String url,title;
	TextView title_view;
	View back_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		back_layout =findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		title_view = (TextView)findViewById(R.id.title);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		title_view.setText(title);
		
				
		tqWebView = (WebView) findViewById(R.id.tq);
		tqWebView.loadUrl(url);
		WebSettings settings = tqWebView.getSettings();
		//settings.setPluginsEnabled(true);
		settings.setJavaScriptEnabled(true);
		
		tqWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});

	}
}
