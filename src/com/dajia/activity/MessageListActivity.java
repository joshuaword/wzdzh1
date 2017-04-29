package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dajia.Bean.MessageBean;
import com.dajia.Bean.MessageLisyBean;
import com.dajia.fragment.ALLMessageListAdapter;
import com.google.gson.Gson;

public class MessageListActivity extends BaseActivity implements
		OnClickListener {
	private ListView listView;
	private ALLMessageListAdapter allmessagelistadapter;
	private MessageLisyBean messagelisybean;
	public  List<MessageBean> messagelist = new ArrayList<MessageBean>() ;
	public TextView title;
	private View back_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order);
		initView();
		initData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.history_order_listView);
		title = (TextView)findViewById(R.id.title);
		title.setText("消息");
		back_layout=  findViewById(R.id.back_img);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	getMessage();
	super.onResume();
}
	public void initData() {
		getMessage();
	}
	private void getMessage(){
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		 FinalHttp fp = new FinalHttp();
	        AjaxParams params = new AjaxParams();
	        params.put("userid", userid);
	        params.put("act", "postok");
	        fp.post(baseurl + "/api/tongzhiclient.php", params, new AjaxCallBack<Object>() {
	        	@Override
	        	public void onSuccess(Object t) {
	        		Gson gson = new Gson();
	        		Log.v("aaaa", t.toString());
	        		messagelisybean = gson.fromJson(t.toString(), MessageLisyBean.class);
					if (messagelisybean != null) {
					        if(messagelisybean.getTongzhilist()!=null&&messagelisybean.getTongzhilist().size()>0){
					        	allmessagelistadapter = new ALLMessageListAdapter(MessageListActivity.this, messagelisybean.getTongzhilist(), "");
					        	listView.setAdapter(allmessagelistadapter);
					        	allmessagelistadapter.notifyDataSetChanged();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
