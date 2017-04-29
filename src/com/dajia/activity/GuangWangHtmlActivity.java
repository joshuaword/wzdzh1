package com.dajia.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.k76.wzd.R;
/**
 * 公司简介页面
 * @author Administrator
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class GuangWangHtmlActivity extends BaseActivity {

	private WebView tqWebView;
	String dengluhao;
	private TextView title;
	private LinearLayout backLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhifu);
		
		title = (TextView) findViewById(R.id.title);
		title.setText("公司简介");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		tqWebView = (WebView) findViewById(R.id.tq);
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("weiweb", "http://www.4000001651.net/channel.php?id=3");
		Log.e("MUSIC1", "weiweb="+baseurl);
		tqWebView.loadUrl(baseurl );
		
		//tqWebView.loadUrl("http://xiaozhu.k76.net/index.php?g=Wap&m=Index&a=index&token=uczrlt1420299959&wecha_id=of7kFj1vlFLvws9-XO19lmB_mXeI");
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
