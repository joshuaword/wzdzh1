package com.dajia.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.k76.wzd.R;

@SuppressLint("SetJavaScriptEnabled")
public class ChaxunWGActivity extends BaseActivity {

	private WebView tqWebView;
	String dengluhao;
	private TextView title;
	private LinearLayout backLayout;
	  PackageInfo info;
	  String baseurl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhifu);
		baseurl = getIntent().getStringExtra("baseurl");
		 PackageManager manager = this.getPackageManager();
       
		try {
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		title = (TextView) findViewById(R.id.title);
		title.setText("违章查询");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		tqWebView = (WebView) findViewById(R.id.tq);
		tqWebView.loadUrl(baseurl);
		tqWebView.getSettings().setJavaScriptEnabled(true);  
		/*tqWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});*/
		tqWebView.setWebChromeClient(new WebChromeClient());
	
	}
}
