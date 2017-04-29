package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.MessageBean;
import com.dajia.Bean.MessageLisyBean;
import com.dajia.fragment.ALLCheliangListAdapter;
import com.google.gson.Gson;

public class CheLiangguanliListActivity extends BaseActivity implements
		OnClickListener {
	private ListView listView;
	private ALLCheliangListAdapter allchelianglistadapter;
	private MessageLisyBean messagelisybean;
	public List<MessageBean> messagelist = new ArrayList<MessageBean>();
	public TextView title, btnRight;
	private View back_layout;
private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheliangguanli_list);
		from = getIntent().getStringExtra("from");
		initView();
		initData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.history_order_listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(from!=null){
					if(from.equals("order")){
						Intent intent = new Intent("finish");
						intent.putExtra("chepai", messagelisybean.getChepai()
								.get(arg2).getChepaihao());
						intent.putExtra("chexing", messagelisybean.getChepai()
								.get(arg2).getChexing());
						intent.putExtra("from", "from");
						sendBroadcast(intent);
						SharedPreferences settings = getSharedPreferences("setting", 0);
						Editor editor=settings.edit();
						editor.putString("chepai", messagelisybean.getChepai()
								.get(arg2).getChepaihao());
						editor.putString("chexing", messagelisybean.getChepai()
								.get(arg2).getChexing());
						editor.commit();
						finish();
					}else if(from.equals("account")){
//						Intent intent = new Intent("acountfinish");
//						intent.putExtra("chepai", messagelisybean.getChepai()
//								.get(arg2).getChepaihao());
//						intent.putExtra("chexing", messagelisybean.getChepai()
//								.get(arg2).getChexing());
//						intent.putExtra("from", "from");
//						sendBroadcast(intent);
					String 	userid = getIntent().getStringExtra("userid");
						FinalHttp fp = new FinalHttp();
						AjaxParams ap = new AjaxParams();
						ap.put("chepaihao" , messagelisybean.getChepai().get(arg2).getChepaihao());
						ap.put("chexing" , messagelisybean.getChepai().get(arg2).getChexing());
						ap.put("act", "postok");
						ap.put("userid", userid);
						final SharedPreferences settings = getSharedPreferences("setting", 0);
						String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
						fp.post(baseurl + "/api/registerclient.php", ap,new AjaxCallBack<Object>() {
							
							@Override
							public void onSuccess(Object t) {
								super.onSuccess(t);
								Toast.makeText(CheLiangguanliListActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
								finish();
							}
							
						});
					}
				}

			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				//AlertDialog.Builder normalDialog=new AlertDialog.Builder(getApplicationContext());  
		        AlertDialog.Builder normalDia=new AlertDialog.Builder(CheLiangguanliListActivity.this);  
		        normalDia.setIcon(R.drawable.ic_launcher);  
		        normalDia.setTitle("提示");  
		        normalDia.setMessage(Html.fromHtml("是否删除<font color=blue>"+messagelisybean.getChepai().get(arg2).getChexing()+" "+messagelisybean.getChepai()
						.get(arg2).getChepaihao()+"</font>车辆?"));
		          
		        normalDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub  
		            	dialog.dismiss();
		            }  
		        });  
		        normalDia.setNegativeButton("确定", new DialogInterface.OnClickListener() {  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub 
		            	if (messagelisybean != null) {
							delcheliang(messagelisybean.getChepai().get(arg2)
									.getId());
						}
		            	
		            }  
		        });  
		        normalDia.create().show();  
				
				return false;
			}
		});
		title = (TextView) findViewById(R.id.title);
		btnRight = (TextView) findViewById(R.id.btnRight);
		title.setText("车辆管理");
		back_layout = findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CheLiangguanliListActivity.this,
						VehicleActivity.class);
				intent.putExtra("from", "addMYCL");
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getcheliang();
		super.onResume();
	}

	public void initData() {
	}

	private void getcheliang() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/addcheliangclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						messagelisybean = gson.fromJson(t.toString(),
								MessageLisyBean.class);
						if (messagelisybean != null) {
							if (messagelisybean.getChepai() != null
									&& messagelisybean.getChepai().size() > 0) {
								allchelianglistadapter = new ALLCheliangListAdapter(
										CheLiangguanliListActivity.this,
										messagelisybean.getChepai(), "");
								listView.setAdapter(allchelianglistadapter);
								allchelianglistadapter.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});

	}

	private void delcheliang(String delcheliangid) {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("delcheliangid", delcheliangid);
		params.put("act", "postok");
		fp.post(baseurl + "/api/addcheliangclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Log.v("aaaa", t.toString());
						getcheliang();
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
