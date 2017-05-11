package com.daijia.chat;

import java.util.ArrayList;
import java.util.List;

import com.dajia.ImageGesActivity;
import com.dajia.VehicleApp;
import com.dajia.Bean.ChatListBean;
import com.dajia.activity.DriverDetails;
import com.dajia.activity.ScaleimgActivity;
import com.dajia.activity.WebViewActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import net.k76.wzd.R;

public class ChatMsgViewAdapter extends BaseAdapter {
	private static ImageLoader imageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options;
	private ArrayList<String> imglist =new ArrayList<String>();
	public static void setCacheImageLoad(Context context, int drawId,
			int Rounded, ImageView imageView, String imageUrl) {
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder().showStubImage(drawId)
				.showImageForEmptyUri(drawId).showImageOnFail(drawId)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(Rounded)).build();
		imageLoader.displayImage(imageUrl, imageView, options);
	}
	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatListBean> coll;

	private Context ctx;

	private LayoutInflater mInflater;
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	// 语音合成对象
		private SpeechSynthesizer mTts;

		// 默认发音人
		private String voicer = "xiaoyan";
		// 缓冲进度
		private int mPercentForBuffering = 0;
		// 播放进度
		private int mPercentForPlaying = 0;
		
		// 引擎类型
		private String mEngineType = SpeechConstant.TYPE_CLOUD;
		
	
	public ChatMsgViewAdapter(Context context, List<ChatListBean> coll) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
		initSpeak();
		
	}
	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			//设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, "50");
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, "50");
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, "50");
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}
	private void initSpeak(){
		// 初始化合成对象
				mTts = SpeechSynthesizer.createSynthesizer(ctx, mTtsInitListener);
				// 初始化识别无UI识别对象
				// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
						
	}
	
	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
			mPercentForBuffering = percent;
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
			} else if (error != null) {
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};
	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatListBean entity = coll.get(position);
		String  isComMsg = entity.getMsgtype();
		Log.d("getMsgtype", entity.getMsgtype());
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isComMsg.equals("0")) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
				
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.iv_userhead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			viewHolder.img_chatcontent= (ImageView) convertView
					.findViewById(R.id.img_chatcontent);
			viewHolder.url_layout=convertView
					.findViewById(R.id.url_layout);
			viewHolder.biaoti = (TextView) convertView
					.findViewById(R.id.biaoti);
			viewHolder.jianjie = (TextView) convertView
					.findViewById(R.id.jianjie);
			viewHolder.img_title = (ImageView) convertView
					.findViewById(R.id.img_title);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.getCreatetime());
		
		if (!entity.getThefile().equals("")&&entity.getLeixing().equals("xunfei") ) {
			viewHolder.img_chatcontent.setVisibility(View.GONE);
			viewHolder.tvContent.setVisibility(View.VISIBLE);
			viewHolder.url_layout.setVisibility(View.GONE);
			viewHolder.tvContent.setText("");
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
			viewHolder.tvTime.setText(entity.getMp3time());
		}else if(entity.getLeixing().equals("url")){
			viewHolder.url_layout.setVisibility(View.VISIBLE);
			viewHolder.tvContent.setVisibility(View.GONE);
			viewHolder.biaoti.setText(entity.getTitle());
			viewHolder.jianjie.setText(entity.getJianjie());
			setCacheImageLoad(ctx, 0, 0, viewHolder.img_title, entity.getIcon());
		} else if(!entity.getThefile().equals("")&&entity.getLeixing().equals("img")){
			viewHolder.img_chatcontent.setVisibility(View.VISIBLE);
			viewHolder.tvContent.setVisibility(View.GONE);
			viewHolder.url_layout.setVisibility(View.GONE);
			Log.d("entity.getThefile()", entity.getThefile());
			setCacheImageLoad(ctx, 0, 0, viewHolder.img_chatcontent, entity.getThefile());
		}else {
			viewHolder.img_chatcontent.setVisibility(View.GONE);
			viewHolder.url_layout.setVisibility(View.GONE);
			viewHolder.tvContent.setVisibility(View.VISIBLE);
			viewHolder.tvContent.setText(entity.getLeirong());			
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			viewHolder.tvTime.setText("");
		}
		viewHolder.img_chatcontent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imglist.clear();
				imglist.add(entity.getThefile());
				Intent intent = new Intent(ctx,
						ImageGesActivity.class);
				intent.putExtra("selectposition", 0);
				intent.putStringArrayListExtra("imglist", imglist);
				ctx.startActivity(intent);
			}
		});
		viewHolder.url_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				   Intent intent = new Intent(ctx, WebViewActivity.class);
				   intent.putExtra("url", entity.getLeirong());
				   intent.putExtra("title",entity.getTitle());
				   ctx.startActivity(intent);
			}
		});
		 
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (entity.getLeixing().equals("mp3")) {
					playMusic(entity.getThefile()) ;
				}
				else if(VehicleApp.getInstance().getSetBean().getVoice().equals("xunfei")&&!entity.getLeixing().equals("url")){
					  // 设置参数
				    setParam();
				    mTts.startSpeaking(entity.getLeirong(), mTtsListener);
				}else if(entity.getLeixing().equals("xunfei")){
					 setParam();
					    mTts.startSpeaking(entity.getLeirong(), mTtsListener);
				}
				
			}
		});
		viewHolder.tvUserName.setText(entity.getNickname());
		setCacheImageLoad(ctx, -1, 0, viewHolder.iv_userhead, entity.getHeadimg());
		return convertView;
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
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			Log.d(TAG, "返回音频数据："+data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
	};

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public View url_layout;
		public TextView biaoti;
		public TextView jianjie;
		public ImageView iv_userhead,img_chatcontent,img_title;
	}

	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
