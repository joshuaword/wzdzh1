package com.dajia.activity;

import java.io.File;
import java.io.FileNotFoundException;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.AddressBean;
import com.dajia.constant.Constant;
import com.google.gson.Gson;

public class AddAddressActivity extends BaseActivity {
	private final static String TAG = "AddAddressActivity";
	private Context mContext;
	private TextView mAdd;
	private String mUserid;
	private String mX;
	private String mY;
	private EditText mCurrentAddress;
	private Button mStartRecord;
	private Button mSelectButton;
	private boolean mStartedFlg = false;
	private MediaRecorder mRecorder;
	private LinearLayout mRecordItem;
	private TextView mRecordTime;
	private String mPath;
	private boolean mHasRecord = false;

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_add_address);
		((TextView) findViewById(R.id.title))
				.setText(getString(R.string.add_address));
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						reSelect();
					}
				});

		mRecordItem = (LinearLayout) findViewById(R.id.record);
		mRecordItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final MediaPlayer mediaplayer = new MediaPlayer();
				try {
					mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaplayer.setDataSource(mPath);
					Log.d(TAG, "mPath exists:" + new File(mPath).exists());
					mediaplayer.prepare();

					mediaplayer
							.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								public void onCompletion(MediaPlayer arg0) {
									Log.d(TAG, "MediaPlayer onCompletion " + arg0);
									mediaplayer.stop();
									mediaplayer.reset();
									mediaplayer.release();
								}
							});

					mediaplayer
							.setOnErrorListener(new MediaPlayer.OnErrorListener() {
								public boolean onError(MediaPlayer arg0,
										int arg1, int arg2) {
									Log.d(TAG, "MediaPlayer OnError " + arg0 + " "+arg1 + " " + arg2);
									mediaplayer.stop();
									mediaplayer.reset();
									mediaplayer.release();
									return false;
								}
							});
					mediaplayer.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mRecordTime = (TextView) findViewById(R.id.record_time);
		mAdd = (TextView) findViewById(R.id.btnpay);
		mAdd.setText(R.string.ensure);
		mAdd.setVisibility(View.VISIBLE);
		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UploadAddress();
			}
		});
		SharedPreferences sp = getSharedPreferences("setting", 0);
		mUserid = sp.getString("userid", "");
		mX = sp.getString("x", "");
		mY = sp.getString("y", "");
		
		mCurrentAddress = (EditText) findViewById(R.id.current_address);
		mCurrentAddress.setText(getIntent().getStringExtra(
				SelectAddressActivity.KEY_CURRENT_ADDRESS));

		mStartRecord = (Button) findViewById(R.id.start_record);
		mStartRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mStartedFlg) {
					if (mHasRecord) {
						mHasRecord = false;
						mPath = null;
						mRecordItem.setVisibility(View.GONE);
						mStartRecord.setText(R.string.start_record);
						return;
					}
					mRecorder = new MediaRecorder();
					try {
						mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mRecorder
								.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
						mRecorder
								.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

						// Set output file path
						String path = getSDPath();
						if (path != null) {
							File dir = new File(path + "/dashou/record");
							if (!dir.exists()) {
								dir.mkdir();
							}
							path = dir + "/" + "record" + ".m4a";
							File f = new File(path);
							if (f.exists()) {
								f.delete();
							}
							mPath = path;
							mRecorder.setOutputFile(path);
							Log.d(TAG, "bf mRecorder.prepare()");
							mRecorder.prepare();
							Log.d(TAG, "af mRecorder.prepare()");
							Log.d(TAG, "bf mRecorder.start()");
							mRecorder.start(); // Recording is now started
							Log.d(TAG, "af mRecorder.start()");
							mStartedFlg = true;
							mStartRecord.setText(R.string.stop_record);
							Log.d(TAG, "Start recording ...");
						}
					} catch (Exception e) {
						e.printStackTrace();
						mStartRecord.setText(R.string.start_record);
					}
				} else {
					if (mStartedFlg) {
						try {
							mRecorder.stop();
							mRecorder.reset();
							mRecorder.release();
							if (mPath != null) {
								MediaPlayer mediaplayer = new MediaPlayer();
								mediaplayer.setDataSource(mPath);
								mediaplayer.prepare();
								int duration = mediaplayer.getDuration();
								int time = duration< 1000 ? 1:duration/1000;
								mRecordTime.setText("" + time + "秒");
								mediaplayer.stop();
								mediaplayer.reset();
								mediaplayer.release();
								mediaplayer = null;

								mStartRecord.setText(R.string.delete_record);
								mRecordItem.setVisibility(View.VISIBLE);
								UploadRecord(mPath);
								mHasRecord = true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mStartedFlg = false; // Set button status flag
				}
			}
		});

		mSelectButton = (Button) findViewById(R.id.select);
		mSelectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reSelect();
			}
		});

	}

	private void reSelect(){
		Intent intent = new Intent(mContext, SelectAddressActivity.class);
		startActivity(intent);
		finish();
	}
	private void UploadRecord(String path) {
		FinalHttp fp = new FinalHttp();
		File file = new File(path);
		AjaxParams params = new AjaxParams();
		params.put("userid", mUserid);
		try {
			params.put("voice", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("act", "postok");
		Log.d(TAG, Constant.API_UPLOAD_RECORD + " params:" + params);
		fp.post(Constant.API_UPLOAD_RECORD, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				if (t == null) {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_no_return),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Log.d(TAG, t.toString());
				Gson gson = new Gson();
				DeleteBean resulte = gson.fromJson(t.toString(),
						DeleteBean.class);
				Log.d(TAG, "UploadRecord resulte:" + resulte.getRet());
				if (!("success".equals(resulte.getRet()))) {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_success),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(mContext,
						mContext.getString(R.string.server_fail),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void UploadAddress() {
		
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", mUserid);
		params.put("x", mX);
		params.put("y", mY);
		params.put("address", mCurrentAddress.getText().toString());
		params.put("deladdressid", "0");
		params.put("act", "postok");
		Log.d(TAG, Constant.API_ADDRESS_MANAGE + " params:" + params);
		fp.post(Constant.API_ADDRESS_MANAGE, params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				if (t == null) {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_no_return),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Log.d(TAG, t.toString());
				Gson gson = new Gson();
				AddressBean resulte = gson.fromJson(t.toString(), AddressBean.class);
				Log.d(TAG, "UploadRecord resulte:" + resulte.getRet());
				if (!("success".equals(resulte.getRet()))) {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_fail),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext,
							mContext.getString(R.string.server_success),
							Toast.LENGTH_SHORT).show();
				}
				exit();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(mContext,
						mContext.getString(R.string.server_fail),
						Toast.LENGTH_SHORT).show();
				exit();
			}
		});
	}
	
	private void exit(){
		Intent intent = new Intent(mContext, AddressActivity.class);
		startActivity(intent);
		AddAddressActivity.this.finish();
	}
	
	class DeleteBean {
		private String ret;

		public String getRet() {
			return ret;
		}

		public void setRet(String ret) {
			this.ret = ret;
		}
	}
}
