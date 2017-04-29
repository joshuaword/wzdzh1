package com.dajia.activity;


import net.k76.wzd.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CaptureActivity extends Activity {
	//private final static int SCANNIN_GREQUEST_CODE = 1;
	/**
	 * 显示扫描结果
	 */
	private TextView mTextView;
	/**
	 * 显示扫描拍的图片
	 */
	private ImageView mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erwema);
		
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("二维码扫描");
		LinearLayout  backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CaptureActivity.this.finish();
			}
		});
		
		mTextView = (TextView) findViewById(R.id.result);
		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		// 显示扫描到的内容
		mTextView.setText(bundle.getString("result"));
		// 显示图片
		mImageView.setImageBitmap((Bitmap) intent.getParcelableExtra("bitmap"));
		
		Button copy_btn=(Button) findViewById(R.id.erwei_copy);
		Button go_btn=(Button) findViewById(R.id.erwei_go);
		copy_btn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager board=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				board.setText(mTextView.getText().toString().trim());
				Toast.makeText(CaptureActivity.this, board.getText(), Toast.LENGTH_LONG).show();
			}
		});
		//打开扫描结果
		go_btn.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebView tqWebView = (WebView) findViewById(R.id.er_tq);
				tqWebView.loadUrl(mTextView.getText().toString());
				tqWebView.getSettings().setJavaScriptEnabled(true);
				//tqWebView.setWebChromeClient(new WebChromeClient());
			}
		});
		
	}
}
