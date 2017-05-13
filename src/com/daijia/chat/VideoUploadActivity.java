package com.daijia.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.navisdk.util.common.LogUtil;
import com.dajia.Bean.BitmapEntity;
import com.dajia.activity.NoScrollGridView;
import com.dajia.constant.Constant;
import com.twzs.core.download.ActivityUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;
import net.k76.wzd.R;

/**
 * @author fucheng
 *
 */
public class VideoUploadActivity extends Activity {
	private NoScrollGridView shipin_gridview;
	private VideodetailListviewAdapter adapter;
	private List<BitmapEntity> bit = new ArrayList<BitmapEntity>();
	private static final int VIDEO_CAPTURE0 = 0;
	private Button btn_yulan, btn_upload;
	private String url;
	private List<String> photoList = new ArrayList<String>();
	PopupWindow popupWindow;
	View popubg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_upload);
		shipin_gridview = (NoScrollGridView) this.findViewById(R.id.shipin_gridview);
		btn_yulan = (Button) findViewById(R.id.btn_yulan);
		btn_upload = (Button) findViewById(R.id.btn_upload);
		new Search_photo().start();
		btn_yulan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		btn_upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Constant.RECEIVEACTION);
				intent.putExtra("url_data", url);
				sendBroadcast(intent );
				finish();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1 && bit != null) {
				if(bit.size()>0){
					adapter = new VideodetailListviewAdapter(VideoUploadActivity.this, bit);
					shipin_gridview.setAdapter(adapter);
					adapter.setSeclection(0);
					url = bit.get(0).getUri();
				}else{
					Toast.makeText(VideoUploadActivity.this, "暂无本地视频!", Toast.LENGTH_SHORT).show();
				}
				shipin_gridview.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
						if (bit.size() > 0) {
							url = bit.get(position).getUri();
							adapter.setSeclection(position);
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		};
	};

	/**
	 * 
	 * 遍历系统数据库找出相应的是视频的信息，每找出一条视频信息的同时利用与之关联的找出对应缩略图的uri 再异步加载缩略图，
	 * 由于查询速度非常快，全部查找完成在设置，等待时间不会太长
	 * 
	 * @author Administrator
	 *
	 */
	class Search_photo extends Thread {
		@Override
		public void run() {
			// 如果有sd卡（外部存储卡）
			bit.clear();
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = VideoUploadActivity.this.getApplicationContext().getContentResolver();
				Cursor cursor = cr.query(originalUri, null, null, null, null);
				if (cursor == null) {
					return;
				}
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
					String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
					long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
					long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
					// 获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
					int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
					String[] selectionArgs = new String[] { id + "" };
					String[] thumbColumns = new String[] { MediaStore.Video.Thumbnails.DATA,
							MediaStore.Video.Thumbnails.VIDEO_ID };
					String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";

					String uri_thumb = "";
					Cursor thumbCursor = (VideoUploadActivity.this.getApplicationContext().getContentResolver()).query(
							MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs,
							null);

					if (thumbCursor != null && thumbCursor.moveToFirst()) {
						uri_thumb = thumbCursor
								.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

					}

					BitmapEntity bitmapEntity = new BitmapEntity(title, path, size, uri_thumb, duration);
					bit.add(bitmapEntity);
				}
				if (cursor != null) {
					cursor.close();
					mHandler.sendEmptyMessage(1);
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE0) {
			new Search_photo().start();
		}

	}
	
}
