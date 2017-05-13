package com.daijia.chat;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import net.k76.wzd.R;

public class VideoTasteActivity extends Activity {

	private MyVideoView mVideoView;
	PopupWindow popupWindow;
	Dialog progressDialog;
	ImageView btn_pay_zhifu;
	private String url;
	Button play, pause, baonianchoose, btn_zhifubao1, btn_weixin1, btn_duanxin1, btn_zhifubao2, btn_weixin2,
			btn_duanxin2, baoyuechoose;
	View zhifu_layout1, zhifu_layout2, seekbar_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.video_taste_activity);
		progressDialog = ProgressDialog.show(VideoTasteActivity.this, null, "正在加载视频...");
		progressDialog.show();
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mVideoView = (MyVideoView) findViewById(R.id.videoView);
		MyVideoView.WIDTH = dm.widthPixels;
		MyVideoView.HEIGHT = dm.heightPixels;
		MediaController mc = new MediaController(this);
		mVideoView.setMediaController(mc);
		url = getIntent().getStringExtra("url");
		if(url!=null){
			Uri uri = Uri.parse(url);
			mVideoView.setVideoURI(uri);
			mVideoView.requestFocus();
			mVideoView.start();
			
			// 视频准备好
			mVideoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					mp.setOnSeekCompleteListener(new OnSeekCompleteListener() {
						@Override
						public void onSeekComplete(MediaPlayer mp) {
							
							mp.start();
						}
					});
				}
			});

		}
		
	}

}
