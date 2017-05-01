package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;

import com.daijia.chat.SearchChatActivity;
import com.dajia.Bean.ChatBean;
import com.dajia.Bean.MessageBean;
import com.dajia.fragment.ALLChatListAdapter;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class SearchResultListActivity extends BaseActivity implements
		OnClickListener {
	private ListView listView;
	private ALLChatListAdapter allmessagelistadapter;
	private ChatBean messagelisybean;
	public  List<MessageBean> messagelist = new ArrayList<MessageBean>() ;
	public TextView title,result;
	private View back_layout;
	private String leirong;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order);
		leirong = getIntent().getStringExtra("keyword");
		initView();
		initData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.history_order_listView);
		result = (TextView)findViewById(R.id.no_dingdan);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postition, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchResultListActivity.this, SearchChatActivity.class);
				intent.putExtra("chatid", messagelisybean.getChatlist().get(postition).getChatid());
				intent.putExtra("keyword",  messagelisybean.getChatlist().get(postition).getLeirong());
				startActivity(intent);
			}
		});
		title = (TextView)findViewById(R.id.title);
		title.setText("搜索结果");
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
	        params.put("leirong", leirong);
	        params.put("act", "postok");
	        fp.post(baseurl + "/api/chatsearch.php", params, new AjaxCallBack<Object>() {
	        	@Override
	        	public void onSuccess(Object t) {
	        		Gson gson = new Gson();
	        		Log.v("aaaa", t.toString());
	        		messagelisybean = gson.fromJson(t.toString(), ChatBean.class);
					if (messagelisybean != null) {
					        if(messagelisybean.getChatlist()!=null&&messagelisybean.getChatlist().size()>0){
					        	result.setVisibility(View.GONE);
					        	allmessagelistadapter = new ALLChatListAdapter(SearchResultListActivity.this, messagelisybean.getChatlist(), "");
					        	listView.setAdapter(allmessagelistadapter);
					        	allmessagelistadapter.notifyDataSetChanged();
					        }else{
					        	result.setVisibility(View.VISIBLE);
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
