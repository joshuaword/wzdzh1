package com.dajia.fragment;

import java.util.List;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.dajia.Bean.MessageBean;
import com.dajia.activity.MainActivity;

public class TimeTaskScroll extends TimerTask {
	
	private ListView listView;
	private String unRead;
	
	public TimeTaskScroll(Context context, ListView listView, List<MessageBean> list,String unRead){
		this.listView = listView;
		this.unRead =unRead;
		listView.setAdapter(new MessageListAdapter(context, list,unRead));
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			listView.smoothScrollBy(1, 0);
		};
	};

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		handler.sendMessageAtTime(msg, 10000);
	}

}
