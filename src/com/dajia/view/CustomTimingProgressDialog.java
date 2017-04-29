package com.dajia.view;

import java.util.Timer;
import java.util.TimerTask;
import net.k76.wzd.R;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
/**
 * 自定义的dialog，用来展示
 * @author Administrator
 *
 */
@SuppressLint("NewApi")
public class CustomTimingProgressDialog extends ProgressDialog {

//	private Context context;
	private TextView dialog_tv_test;
	private CustomTimingCircle dialog_tv_cc;
	private Timer timer;
	public CustomTimingProgressDialog(Context context) {
		super(context);
//		this.context = context;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_timing_progress_dialog);
		dialog_tv_test = (TextView) findViewById(R.id.dialog_tv_test);
		dialog_tv_cc = (CustomTimingCircle) findViewById(R.id.dialog_tv_cc);
		dialog_tv_cc.setMax(100);
		
		timer = new Timer();
		//计时器任务，延迟多少开始数，数的速度
		timer.schedule(timerTask, 0,1000);
		//这里用的是nineold的属性动画向下兼容包
		ValueAnimator animator = ValueAnimator.ofInt(100,0);
		animator.setDuration(70000);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int animatedValue = (Integer) animation.getAnimatedValue();
				dialog_tv_cc.setProgress(animatedValue);
			}
		});
		animator.start();
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
			if (msg.what > 0) {
				dialog_tv_test.setText(msg.what+ "\n秒");
			} else {
				dialogDismiss();
			}
		}
	};
	
	TimerTask timerTask = new TimerTask() {
		int second = 5;
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = second;
			handler.sendMessage(msg);
			second--;
//			Log.e("second", "" + second);
		}
	};
	/**
	 * 强制取消掉dialog，同样的计时器也同时取消掉
	 *  看具体情况来判断是否打开新的界面
	 */
	public void dialogDismiss() {
		this.dismiss();
		timer.cancel();
	}

}
