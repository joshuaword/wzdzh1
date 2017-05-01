package com.daijia.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dajia.VehicleApp;
import com.dajia.Bean.ChatBean;
import com.dajia.Bean.ChatListBean;
import com.dajia.Bean.DictationResult;
import com.dajia.activity.ChatLoginActivity;
import com.dajia.activity.SearchResultListActivity;
import com.dajia.fragment.MenuFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zms.wechatrecorder.MediaManager;
import com.zms.wechatrecorder.view.AudioRecordButton;
import com.zms.wechatrecorder.view.AudioRecordButton.AudioRecordFinishListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class SearchChatActivity extends SlidingFragmentActivity implements OnClickListener {
	/** Called when the activity is first created. */
	private ChatBean chatbean;
	private Button mBtnSend;
	private AudioRecordButton mBtnRcd;
	Button btn_normal_rcd;
	private ImageView mBtnBack;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatListBean> mDataArrays = new ArrayList<ChatListBean>();
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding, voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private String voiceName, leixing;
	private long startVoiceT, endVoiceT;
	String mp3time,chatid;
	private Fragment menuFragment;
	TextView title;
	File file;
    private String dictationResultStr = "[";
		private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
		String keyword;

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
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_search);
		chatid = getIntent().getStringExtra("chatid");
		keyword = getIntent().getStringExtra("keyword");
		title = (TextView)findViewById(R.id.title);
		title.setText("搜索"+keyword);
		// 去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		initData();
		initMenu();
		Button leftBtn = (Button) findViewById(R.id.homebtnLeft);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		SpeechUtility.createUtility(SearchChatActivity.this, SpeechConstant.APPID +"55d3f58b");   
	    mIat = SpeechRecognizer.createRecognizer(SearchChatActivity.this, mInitListener);
	}
	// 左侧slidingMenu
		private void initMenu() {
			menuFragment = new MenuFragment();
			setBehindContentView(R.layout.menu_layout);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_layout, menuFragment).commit();
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
//		public void setParam() {
//			// 清空参数
//			mIat.setParameter(SpeechConstant.PARAMS, null);
//
//			// 设置听写引擎
//			mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
//			// 设置返回结果格式
//			mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//				// 设置语言
//				mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//				// 设置语言区域
//				mIat.setParameter(SpeechConstant.ACCENT," mandarin");
//
//			// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//			mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
//			
//			// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//			mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//			
//			// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//			mIat.setParameter(SpeechConstant.ASR_PTT, "1");
//			
//			// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//			// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//			mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//			mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
//		}
	public void initView() {
	 Log.d("***Voice****", VehicleApp.getInstance().getSetBean().getVoice())   ;
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
		btn_normal_rcd = (Button)findViewById(R.id.btn_normal_rcd);
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
					if(VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")){
						btn_normal_rcd.setVisibility(View.GONE);
					}else{
						mBtnRcd.setVisibility(View.GONE);
					}
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);
					file = null;
					leixing = "txt";
				} else {
					if(VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")){
						leixing = "txt";
						btn_normal_rcd.setVisibility(View.VISIBLE);
						btn_normal_rcd.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if( null == mIat ){
									Toast.makeText(SearchChatActivity.this, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化", Toast.LENGTH_SHORT).show();
									return;
								}
							    dictationResultStr = "[";
					            RecognizerDialog iatDialog = new RecognizerDialog(
					                    SearchChatActivity.this, null);
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
					                        List<DictationResult> dictationResultList = gson
					                                .fromJson(dictationResultStr,
					                                        new TypeToken<List<DictationResult>>() {
					                                        }.getType());
					                        String finalResult = "";
					                        for (int i = 0; i < dictationResultList.size() - 1; i++) {
					                            finalResult += dictationResultList.get(i)
					                                    .toString();
					                        }
					                        mEditTextContent.setText(finalResult);
					                		SendMessage();
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
					}else{
						mBtnRcd.setVisibility(View.VISIBLE);
						leixing = "mp3";
					}
					
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;
					
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});

	}

	private final static int COUNT = 6;

	public void initData() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String userid = settings.getString("userid", "");
		if (userid.equals("") || userid == null) {
			Intent intent = new Intent(SearchChatActivity.this, ChatLoginActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		getMessage();

	}

	private void getMessage() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("chatid", chatid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/chatsearch.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				Gson gson = new Gson();
				Log.v("aaaa", t.toString());
				chatbean = gson.fromJson(t.toString(), ChatBean.class);
				Log.d("chatbean", chatbean + "");
				if (chatbean != null) {
					if (chatbean.getChatlist() != null && chatbean.getChatlist().size() > 0) {
						mAdapter = new ChatMsgViewAdapter(SearchChatActivity.this, chatbean.getChatlist());
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

	private void SendMessage() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		params.put("leirong", mEditTextContent.getText().toString());
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

		fp.post(baseurl + "/api/chataddclient.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				mEditTextContent.setText("");
				getMessage();
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
			}
		});

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
			Intent intent = new Intent(SearchChatActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void send() {
			String contString = mEditTextContent.getText().toString();
			if (contString.length() > 0) {
				SendMessage();
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
			SendMessage();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if( null != mIat ){
			// 退出时释放连接
			mIat.cancel();
			mIat.destroy();
		}
		MediaManager.release();
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
		SendMessage();
	}
}