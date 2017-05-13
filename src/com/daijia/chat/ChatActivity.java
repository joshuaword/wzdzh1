package com.daijia.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.dajia.VehicleApp;
import com.dajia.Bean.ChatBean;
import com.dajia.Bean.ChatListBean;
import com.dajia.Bean.ChatXunfeiBean;
import com.dajia.Bean.DictationResult;
import com.dajia.Bean.UpDateInfo;
import com.dajia.activity.ChatLoginActivity;
import com.dajia.activity.CheckHtmlActivity;
import com.dajia.constant.Constant;
import com.dajia.fragment.MenuFragment;
import com.dajia.util.Assign_UpDateDialog;
import com.dajia.util.ConfirmDialogListener;
import com.dajia.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.twzs.core.download.Downloadhelper;
import com.twzs.zouyizou.photo.activity.AlbumActivity;
import com.twzs.zouyizou.photo.util.Bimp;
import com.twzs.zouyizou.photo.util.ImageItem;
import com.zms.wechatrecorder.MediaManager;
import com.zms.wechatrecorder.view.AudioRecordButton;
import com.zms.wechatrecorder.view.AudioRecordButton.AudioRecordFinishListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class ChatActivity extends SlidingFragmentActivity implements OnClickListener {
	public static List<String> all_photoList = new ArrayList<String>();
	private ChatBean chatbean;
	private Button mBtnSend;
	Dialog progressDialog;
	private AudioRecordButton mBtnRcd;
	Button btn_normal_rcd;
	private ImageView mBtnBack;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatListBean> mDataArrays = new ArrayList<ChatListBean>();
	private boolean isShosrt = false;
	public static final String TEMPPATH = "temp";
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding, voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private static final int VIDEO_CAPTURE0 = 10;
	private LinearLayout del_re;
	private PopupWindow choosePopupWindow;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = true;
	private int flag = 1;
	private String url;
	private Handler mHandler = new Handler();
	private String voiceName, leixing;
	private long startVoiceT, endVoiceT;
	private SharedPreferences settings;
	String mp3time;
	private Fragment menuFragment;
	private String leirong;
	private PopupWindow headimgpop;
	private ImageView add;
	File imgfile;

	/**
	 * 拍照的照片存储位置
	 */

	private String mFileName;
	Bitmap bitmap;
	private float roatdp;
	/**
	 * 照相机拍照得到的图片
	 */
	private static final int TAKE_PICTURE = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	// private static final int CUT_PHOTO_REQUEST_CODE = 2;
	private Uri photoUri;
	File file;
	private String dictationResultStr = "[";
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				handler.postDelayed(this, 10000);
				getMessage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// 语音合成对象
	private SpeechSynthesizer mTts;

	// 默认发音人
	private String voicer = "xiaoyan";
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;

	private SpeechRecognizer mIat;
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d("", "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
			}
		}
	};

	/***
	 * @return
	 */
	public static final String getAppSDPath() {
		File file = new File(Environment.getExternalStorageDirectory(), "apk");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static final String getAppTmpSDPath() {
		File file = new File(getAppSDPath(), TEMPPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

	public static String getFileNameFromUrl(String s) {
		int i = s.lastIndexOf("\\");
		if (i < 0 || i >= s.length() - 1) {
			i = s.lastIndexOf("/");
			if (i < 0 || i >= s.length() - 1)
				return s;
		}
		return s.substring(i + 1);
	}

	/**
	 * 显示升级对话框
	 */
	public void showUpdate_Dialog(String newVersion, final String downUrl, final String isAPK) {
		String newverStr = "有新版本更新啦！";
		final String version_downloading = this.getResources().getString(R.string.version_downloading, newVersion);
		final String installapkfile = getAppTmpSDPath() + "/" + getFileNameFromUrl(downUrl);
		Assign_UpDateDialog.showUpdateDialog(this, newverStr, "增加新功能，优化性能！", "立即尝鲜", "残忍拒绝",
				new ConfirmDialogListener() {
					@Override
					public void onPositive(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (isAPK.equals("yes")) {
							Downloadhelper dm = new Downloadhelper(ChatActivity.this, true);
							dm.downLoadFile(version_downloading, downUrl, installapkfile);
							dialog.cancel();
						} else {
							if (downUrl != null && !downUrl.equals("")) {

								Intent intent = new Intent(ChatActivity.this, CheckHtmlActivity.class);
								intent.putExtra("baseurl", downUrl);
								startActivity(intent);
							}
						}

					}

					@Override
					public void onNegative(DialogInterface dialog) {
						dialog.cancel();
					}
				});

	}

	private void checkUPDATE() {

		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		params.put("appname", "android");
		fp.post(baseurl + "/api/shengjiclient.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				Gson gson = new Gson();
				UpDateInfo upDateBean = gson.fromJson(t.toString(), UpDateInfo.class);
				if (upDateBean != null) {
					if (!upDateBean.getVer().equals("7.4.20170419")) {
						showUpdate_Dialog(upDateBean.getVer(), upDateBean.getUrl(), upDateBean.getApk());
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
			}
		});

	}
	private BroadcastReceiver reciverrefresh = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			getMessage();
		}
	};

	private BroadcastReceiver reciver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			url = intent.getStringExtra("url_data");
			leixing = "video";
			progressDialog = ProgressDialog.show(ChatActivity.this, null, "正在发送视频...");
			progressDialog.show();
			SendMessage("video");
			url = "";
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);
		// 去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		roatdp = getResources().getDimension(R.dimen.introduce3_text_top);
		registerReceiver(reciver, new IntentFilter(Constant.RECEIVEACTION));
		registerReceiver(reciverrefresh, new IntentFilter(Constant.RECEIVEREFRESH));
		
		initView();
		initData();
		handler.postDelayed(runnable, 10000); // 每隔1s执行// 初始化SlideMenus
		initMenu();
		Button leftBtn = (Button) findViewById(R.id.homebtnLeft);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSlidingMenu().showMenu();
			}
		});
		SpeechUtility.createUtility(ChatActivity.this, SpeechConstant.APPID + "55d3f58b");
		mIat = SpeechRecognizer.createRecognizer(ChatActivity.this, mInitListener);
		mTts = SpeechSynthesizer.createSynthesizer(ChatActivity.this, null);
		settings = getSharedPreferences("setting", 0);
		checkUPDATE();
		initchoosePopuWindow();
	}

	// 左侧slidingMenu
	private void initMenu() {
		menuFragment = new MenuFragment();
		setBehindContentView(R.layout.menu_layout);
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_layout, menuFragment).commit();
		SlidingMenu menu = getSlidingMenu();
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		menu.setBehindScrollScale(0.0F);
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	// public void setParam() {
	// // 清空参数
	// mIat.setParameter(SpeechConstant.PARAMS, null);
	//
	// // 设置听写引擎
	// mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
	// // 设置返回结果格式
	// mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
	//
	// // 设置语言
	// mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
	// // 设置语言区域
	// mIat.setParameter(SpeechConstant.ACCENT," mandarin");
	//
	// // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
	// mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
	//
	// // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
	// mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
	//
	// // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
	// mIat.setParameter(SpeechConstant.ASR_PTT, "1");
	//
	// // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
	// // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
	// mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
	// mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
	// Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	// }

	private void showDialog(final View v) {
		final String[] items = new String[] { "拍照", "视频" };
		android.app.AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
		builder.setTitle("选择拍摄类型");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (items[which].equals("拍照")) {
					showHeadimgpop(v);
				} else if (items[which].equals("视频")) {
					showChoosePhotePopWin(v);
				}
			}
		});
		builder.create().show();
	}

	public void initView() {
		add = (ImageView) findViewById(R.id.img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(v);
			}
		});
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				// 移动数据分析，收集开始合成事件

			}
		});
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnRcd = (AudioRecordButton) findViewById(R.id.btn_rcd);
		btn_normal_rcd = (Button) findViewById(R.id.btn_normal_rcd);
		mBtnRcd.setAudioRecordFinishListener(new MyAudioRecordFinishListener());
		mBtnSend.setOnClickListener(this);
		mBtnBack = (ImageView) findViewById(R.id.currentcity_txt);
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
		mBtnBack.setOnClickListener(this);
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);
		volume = (ImageView) this.findViewById(R.id.volume);
		rcChat_popup = this.findViewById(R.id.rcChat_popup);
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_tooshort);
		mSensor = new SoundMeter();
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (btn_vocie) {
					if (VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")) {
						btn_normal_rcd.setVisibility(View.GONE);
					} else {
						mBtnRcd.setVisibility(View.GONE);
					}
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);
					file = null;
					leixing = "txt";
				} else {
					if (VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")) {
						leixing = "txt";
						btn_normal_rcd.setVisibility(View.VISIBLE);
						btn_normal_rcd.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (null == mIat) {
									Toast.makeText(ChatActivity.this,
											"创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								dictationResultStr = "[";
								RecognizerDialog iatDialog = new RecognizerDialog(ChatActivity.this, null);
								mIat.setParameter(SpeechConstant.DOMAIN, "iat"); // domain:域名
								mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
								mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); // mandarin:普通话

								iatDialog.setListener(new RecognizerDialogListener() {
									@Override
									public void onResult(RecognizerResult results, boolean isLast) {
										if (!isLast) {
											dictationResultStr += results.getResultString() + ",";
										} else {
											dictationResultStr += results.getResultString() + "]";
										}
										if (isLast) {
											Gson gson = new Gson();
											List<DictationResult> dictationResultList = gson.fromJson(
													dictationResultStr, new TypeToken<List<DictationResult>>() {
											}.getType());
											String finalResult = "";
											for (int i = 0; i < dictationResultList.size() - 1; i++) {
												finalResult += dictationResultList.get(i).toString();
											}
											mEditTextContent.setText(finalResult);
											SendMessage("txt");
											Log.d("From reall phone", finalResult);
										}
									}

									@Override
									public void onError(SpeechError error) {
										// TODO 自动生成的方法存根
										error.getPlainDescription(true);
									}
								});

								// 开始听写
								iatDialog.show();
							}
						});
					} else {
						mBtnRcd.setVisibility(View.VISIBLE);
						leixing = "mp3";
					}

					mBottom.setVisibility(View.GONE);
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;

				}
			}
		});

		mBottom.setVisibility(View.GONE);
		if (VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")) {
			leixing = "txt";
			btn_normal_rcd.setVisibility(View.VISIBLE);
			btn_normal_rcd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (null == mIat) {
						Toast.makeText(ChatActivity.this, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化",
								Toast.LENGTH_SHORT).show();
						return;
					}
					dictationResultStr = "[";
					RecognizerDialog iatDialog = new RecognizerDialog(ChatActivity.this, null);
					mIat.setParameter(SpeechConstant.DOMAIN, "iat"); // domain:域名
					mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
					mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); // mandarin:普通话

					iatDialog.setListener(new RecognizerDialogListener() {
						@Override
						public void onResult(RecognizerResult results, boolean isLast) {
							if (!isLast) {
								dictationResultStr += results.getResultString() + ",";
							} else {
								dictationResultStr += results.getResultString() + "]";
							}
							if (isLast) {
								Gson gson = new Gson();
								List<DictationResult> dictationResultList = gson.fromJson(dictationResultStr,
										new TypeToken<List<DictationResult>>() {
								}.getType());
								String finalResult = "";
								for (int i = 0; i < dictationResultList.size() - 1; i++) {
									finalResult += dictationResultList.get(i).toString();
								}
								mEditTextContent.setText(finalResult);
								SendMessage("txt");
								Log.d("From reall phone", finalResult);
							}
						}

						@Override
						public void onError(SpeechError error) {
							// TODO 自动生成的方法存根
							error.getPlainDescription(true);
						}
					});

					// 开始听写
					iatDialog.show();
				}
			});
		} else {
			mBtnRcd.setVisibility(View.VISIBLE);
			leixing = "mp3";
		}
		chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
		btn_vocie = true;
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

	}

	private final static int COUNT = 6;

	public void initData() {
		leixing = "txt";
		leirong = "hello";
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String userid = settings.getString("userid", "");
		if (userid.equals("") || userid == null) {
			Intent intent = new Intent(ChatActivity.this, ChatLoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		getMessage();
		SendMessage("txt");

	}

	private void getMessage() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/chatclient.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				Gson gson = new Gson();
				Log.v("aaaa", t.toString());
				chatbean = gson.fromJson(t.toString(), ChatBean.class);
				Log.d("chatbean", chatbean + "");
				if (chatbean != null) {
					if (chatbean.getChatlist() != null && chatbean.getChatlist().size() > 0) {
						mAdapter = new ChatMsgViewAdapter(ChatActivity.this, chatbean.getChatlist());
						mListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
			}
		});

	}

	private void sendPhoto() {
		if (all_photoList != null && all_photoList.size() > 0) {
			String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
			String userid = settings.getString("userid", "");
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("userid", userid);
			parameterMap.put("act", "postok");
			parameterMap.put("leixing", "img");
			List<String> paramList = new ArrayList<String>();
			for (int i = 1; i < all_photoList.size(); i++) {
				paramList.add(all_photoList.get(i));
			}
			progressDialog = ProgressDialog.show(ChatActivity.this, null, "正在发送图片...");
			progressDialog.show();
			if(headimgpop.isShowing()){
				headimgpop.dismiss();
			}
			new AsyncFilesUploadTask(this, baseurl + "/api/chataddclient.php", parameterMap, paramList,progressDialog).execute();
		}

	}

	private void SendMessage(String leixing) {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		if (!leirong.equals("")) {
			params.put("leirong", leirong);
		} else {
			params.put("leirong", mEditTextContent.getText().toString());
		}
		params.put("leixing", leixing);
		if (mp3time != null && !mp3time.equals("")) {
			params.put("mp3time", mp3time);
		}
		if (file != null) {
			try {
				params.put("thefile", file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if (imgfile != null) {
				params.put("thefile", imgfile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (url != null) {
				File videofile = new File(url);
				params.put("thefile", videofile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("paramsparamsparams", baseurl + "/api/chataddclient.php" + params.toString());
		fp.post(baseurl + "/api/chataddclient.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Gson gson = new Gson();
				ChatXunfeiBean bean = gson.fromJson(t.toString(), ChatXunfeiBean.class);
				if (bean != null) {
					if (bean.getRet().equals("success")) {
						if (!bean.getTxt().equals("") && bean.getLeixing().equals("xunfei")) {
							// 设置参数
							setParam();
							mTts.startSpeaking(bean.getTxt(), null);
						}
						leirong = "";
						mEditTextContent.setText("");
						getMessage();
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.cancel();
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
			}
		});

	}

	private void setParam() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			// 设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, "50");
			// 设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, "50");
			// 设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, "50");
		} else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 强制隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			send();
			break;
		case R.id.currentcity_txt:
			Intent intent = new Intent(ChatActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			SendMessage("txt");
		}
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

		return sbBuffer.toString();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "没有内存空间不能录音!", Toast.LENGTH_LONG).show();
			return false;
		}

		return super.onTouchEvent(event);
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	public void head_xiaohei(View v) {

	}

	class MyAudioRecordFinishListener implements AudioRecordFinishListener {

		@Override
		public void onFinish(float second, String filePath) {
			mp3time = second + "";
			file = new File(filePath);
			SendMessage("mp3");
			file = null;
			
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != mIat) {
			// 退出时释放连接
			mIat.cancel();
			mIat.destroy();
		}
		MediaManager.release();
		unregisterReceiver(reciver);
		unregisterReceiver(reciverrefresh);
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MediaManager.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MediaManager.resume();
	}

	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
		}

		@Override
		public void onError(SpeechError error) {
		}

		@Override
		public void onEndOfSpeech() {
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		mEditTextContent.setText(resultBuffer.toString());
		SendMessage("txt");
	}

	public void showHeadimgpop(View v) {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.headimg_popview, null);
		headimgpop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		headimgpop.setBackgroundDrawable(null);
		headimgpop.setOutsideTouchable(true);
		headimgpop.setFocusable(true);
		headimgpop.setTouchable(true);
		headimgpop.showAtLocation(v, Gravity.BOTTOM, 0, 0);

		Button btn_make_photo = (Button) view.findViewById(R.id.btn_camera);
		Button btn_take_photo = (Button) view.findViewById(R.id.btn_selectalbum);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancelload);

		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (headimgpop.isShowing()) {
						headimgpop.dismiss();
					}
					return true;
				}
				return false;
			}
		});

		btn_make_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openCamera();
				headimgpop.dismiss();
			}
		});

		btn_take_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// openAlbum();
				all_photoList.clear();
				Intent intent = new Intent(ChatActivity.this, AlbumActivity.class);
				// startActivity(intent);
				// headimgpop.dismiss();
				startActivityForResult(intent, RESULT_LOAD_IMAGE);
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				headimgpop.dismiss();
			}
		});
	}

	/**
	 * 打开相机
	 */
	@SuppressLint("ShowToast")
	private void openCamera() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakephoto();
		} else {
			Toast.makeText(ChatActivity.this, "没有可用的存储卡", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 打开相册
	 */
	private void openAlbum() {
		try {
			Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(ChatActivity.this, "没有找到照片", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 拍照获取图片
	 */
	public void doTakephoto() {
		try {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
			}
			if (file != null) {
				photoUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null,
					null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	@SuppressLint({ "SdCardPath", "SimpleDateFormat" })
	private void startPhotoZoom(Uri uri) {

		try {
			// 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			String address = sDateFormat.format(new java.util.Date());
			if (!FileUtils.isFileExist("")) {
				FileUtils.createSDDir("");
			}
			mFileName = FileUtils.SDPATH + address + ".JPEG";
			if (uri != null) {
				String img_url = getRealFilePath(this, uri);
				imgfile = new File(img_url);
				leixing = "img";
				progressDialog = ProgressDialog.show(ChatActivity.this, null, "正在发送图片...");
				progressDialog.show();
				if (headimgpop.isShowing()) {
					headimgpop.dismiss();
				}
				SendMessage("img");
				imgfile=null;
				
			}
			// final Intent intent = new
			// Intent("com.android.camera.action.CROP");
			//
			// // 照片URL地址
			// intent.setDataAndType(uri, "image/*");
			//
			// intent.putExtra("crop", "true");
			// // intent.putExtra("aspectX", 1);
			// // intent.putExtra("aspectY", 1);
			// intent.putExtra("outputX", 1920);
			// intent.putExtra("outputY", 1920);
			// // 输出路径
			// intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			// // 输出格式
			// intent.putExtra("outputFormat",
			// Bitmap.CompressFormat.JPEG.toString());
			// // 不启用人脸识别
			// intent.putExtra("noFaceDetection", false);
			// intent.putExtra("return-data", false);
			// startActivityForResult(intent, RESULT_LOAD_IMAGE);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case TAKE_PICTURE:// 当选择拍照时调用
					startPhotoZoom(photoUri);

			break;
		case RESULT_LOAD_IMAGE:
			// if (data != null) {// 当选择从本地获取图片时
			// Uri uri = data.getData();
			// if (uri != null) {
			// startPhotoZoom(uri);
			// }
			// }

			for (ImageItem result : Bimp.tempSelectBitmap) {
				all_photoList.add(result.getImagePath());
			}
			List<String> paramList = new ArrayList<String>();
			List<Map<String, File>> fileList = new ArrayList<Map<String, File>>();
			for (int i = 0; i < all_photoList.size(); i++) {
				paramList.add(all_photoList.get(i));
			}
			for (int i = 0; i < paramList.size(); i++) {
				Map<String, File> fileMap = new HashMap<String, File>();
				File file = new File(paramList.get(i));
				fileMap.put(".jpg", file);
				fileList.add(fileMap);
			}
			sendPhoto();
			Log.d("***", all_photoList.toString());
			break;
		// case CUT_PHOTO_REQUEST_CODE:
		// if (data != null) {// 当选择从本地获取图片时
		// Uri uri = data.getData();
		// if (uri != null) {
		// startPhotoZoom(uri);
		// }
		// }
		// break;
		// case CUT_PHOTO_REQUEST_CODE:
		// if (resultCode == RESULT_OK && null != data) {// 裁剪返回
		// leixing = "img";
		// SendMessage();
		// }
		// break;

		case VIDEO_CAPTURE0:
			if (resultCode == RESULT_OK && null != data) {// 裁剪返回
				Uri uri = data.getData();
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();
				url = actualimagecursor.getString(actual_image_column_index);
				leixing = "video";
				progressDialog = ProgressDialog.show(ChatActivity.this, null, "正在发送视频...");
				progressDialog.show();
				SendMessage("video");
				url="";
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 弹出框
	 * 
	 * @param v
	 */
	private void showChoosePhotePopWin(View v) {
		if (!choosePopupWindow.isShowing()) {
			choosePopupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}

	}

	// 初始弹窗；
	private void initchoosePopuWindow() {
		choosePopupWindow = new PopupWindow(this);
		choosePopupWindow.setBackgroundDrawable(null);
		choosePopupWindow.setWidth(LayoutParams.MATCH_PARENT);
		choosePopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		choosePopupWindow.setOutsideTouchable(true);
		View contentView = LayoutInflater.from(this).inflate(R.layout.choose_photo_popup_window, null);
		Button btn_take_photo = (Button) contentView.findViewById(R.id.btn_camera);
		Button btn_pic_photo = (Button) contentView.findViewById(R.id.btn_album);
		Button btn_cancel = (Button) contentView.findViewById(R.id.btn_cancel);
		btn_take_photo.setOnClickListener(new OnPopupClickedListener());
		btn_pic_photo.setOnClickListener(new OnPopupClickedListener());
		btn_cancel.setOnClickListener(new OnPopupClickedListener());

		contentView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (choosePopupWindow.isShowing()) {
						choosePopupWindow.dismiss();
					}
					return true;
				}
				return false;
			}
		});
		choosePopupWindow.setContentView(contentView);
	}

	class OnPopupClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_camera:
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, path);
				intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
				startActivityForResult(intent, VIDEO_CAPTURE0);
				choosePopupWindow.dismiss();
				break;
			case R.id.btn_album:
				Intent intent1 = new Intent(ChatActivity.this, VideoUploadActivity.class);
				startActivity(intent1);
				choosePopupWindow.dismiss();
				break;
			case R.id.btn_cancel:
				choosePopupWindow.dismiss();
				break;
			default:
				break;
			}
		}

	}

}