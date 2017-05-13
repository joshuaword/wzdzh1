package com.twzs.zouyizou.photo.activity;

import com.dajia.constant.Constant;
import com.twzs.zouyizou.photo.adapter.FolderAdapter;
import com.twzs.zouyizou.photo.util.PublicWay;
import com.twzs.zouyizou.photo.util.Res;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 * @author fucheng
 * 2015年2月4日
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;//gaopeng:这个应该是返回按钮
	private Context mContext;
	private String fromActivity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		fromActivity = getIntent().getStringExtra("fromActivity");
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res
				.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res
				.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this, fromActivity);
		gridView.setAdapter(folderAdapter);
		
		IntentFilter finishfilter=new IntentFilter("finish.broadcast.action");
		registerReceiver(finishBroadcast,finishfilter);
	}
	
	BroadcastReceiver finishBroadcast=new BroadcastReceiver(){

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if(arg1.getBooleanExtra("isfinish", false))
				ImageFile.this.finish();
		}
		
	};

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			// 清空选择的图片
//			Bimp.tempSelectBitmap.clear();
//			Intent intent = new Intent();
//			if (!StringUtil.isEmpty(fromActivity)) {
//				if (fromActivity.equals("PhotoUpLoadActivity")) {
//					intent.setClass(mContext, PhotoUpLoadActivity.class);
//					startActivity(intent);
//					finish();
//				}
//			}
			finish();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent intent = new Intent();
//			if (!StringUtil.isEmpty(fromActivity)) {
//				if (fromActivity.equals("PhotoUpLoadActivity")) {
//					intent.setClass(mContext, PhotoUpLoadActivity.class);
//					startActivity(intent);
//					finish();
//				}
//			}
			 Intent intent = new Intent();
             setResult(Constant.CHOOSE_OK, intent);
             finish();
		}

		return true;
	}

}
