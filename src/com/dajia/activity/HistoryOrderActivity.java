package com.dajia.activity;

import java.util.ArrayList;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dajia.Bean.HistoryOrderBean;
import com.dajia.Bean.HistoryOrderInfo;
import com.dajia.adapter.OrderHistoryAdapter;
import com.google.gson.Gson;
/**
 * 我的订单（之前叫历史订单）
 * @author Administrator
 *
 */
public class HistoryOrderActivity extends BaseActivity 
		 {

	private ListView mHistroyListView;
	private TextView inComeTextView;
	private TextView startLocation;
	private TextView orderTime;
	private RatingBar ratingBar;
	private TextView comment;
	private  TextView  no_dingdan;
	private ArrayList<HistoryOrderInfo> mInfoList = new ArrayList<HistoryOrderInfo>();
	private OrderHistoryAdapter mAdapter;

	private TextView title;
	private TextView rightBtn;
	private TextView leftBtn;
	private RelativeLayout delLayout;
	private   ImageView  back_img;
	private Boolean delFlag = false;
	
	private ArrayList<HistoryOrderInfo> delList = new ArrayList<HistoryOrderInfo>();
	
	private FrameLayout emptyLayout;
	
	String userid;
	int second = 0;
	private LinearLayout backLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_order);

		initView();
		initData();
		mHistroyListView.setOnItemClickListener(new  OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				HistoryOrderInfo   hoi=mInfoList.get(arg2);//获取当前item的所有信息
				String  state=hoi.getState();
				String  dingDanId=hoi.getDingdanid();//获取当前item里的订单id
				String arrivead=hoi.getLine4();//目的地
				String servicedirverinfo=hoi.getXiangqingline8();//"代驾31次 驾龄11年 籍贯江苏"
				Intent intent = new Intent();
				// 执行中 跳转到 地图界面
				if ("执行中".equals(state)) {
				//	intent.putExtra("info", localHistoryOrderInfo);
					intent.setClass(HistoryOrderActivity.this, MainActivity.class);
				}
				// 未确认 跳转到 确认界面
				if ("未确认".equals(state)) {
					intent.putExtra("dingDanId", dingDanId);
					intent.setClass(HistoryOrderActivity.this, ConfirmOrderActivity.class);
				}
				// 未评价 跳转到 评价界面
				if ("未评论".equals(state)) {
					intent.putExtra("dingDanId", dingDanId);
					intent.setClass(HistoryOrderActivity.this, PinglunActivity.class);
				}
				// 已评价 点击跳转到已完成订单详情界面
				if ("已评论".equals(state)) {
					intent.putExtra("dingDanId", dingDanId);
					intent.putExtra("arrivead", arrivead);
					intent.putExtra("servicedirverinfo", servicedirverinfo);
					intent.setClass(HistoryOrderActivity.this, CompleteOrderActivity.class);
				}
				startActivity(intent);
			}
		});
	}

	private void initView() {

		title = (TextView) findViewById(R.id.title);
		title.setText("我的订单");
		
		no_dingdan=(TextView) findViewById(R.id.no_dingdan);

		/*rightBtn = (TextView) findViewById(R.id.btnRight);
		rightBtn.setText("编辑");
		rightBtn.setVisibility(View.GONE);
		rightBtn.setOnClickListener(this);*/
		//返回
		back_img =  (ImageView) findViewById(R.id.back_img);
		back_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mHistroyListView = (ListView) findViewById(R.id.history_order_listView);
		userid = getIntent().getStringExtra("userid");//18956049168 谭总
		
	}

	public void initData() {
		
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid",userid); 
		params.put("act", "postok");
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		
		fp.post(baseurl + "/api/lishiclient.php", params,new AjaxCallBack<Object>() {
			
			@Override
			public void onSuccess(Object t) {
				super.onSuccess(t);
				Gson gson = new Gson();
				//后台返回一个ret和一个list，通过gson解析后 在HistoryOrderBean接收
				HistoryOrderBean bean = gson.fromJson(t.toString(), HistoryOrderBean.class);
				
				if (bean != null) {
					if ("success".equals(bean.getRet())) {
						if (bean.getList() != null && bean.getList().size() > 0) {
							mInfoList.addAll(bean.getList());//数据最终被保存在HistoryOrderInfo这个实体类里
							//把数据传递到adapter里进行处理
							mAdapter = new OrderHistoryAdapter(HistoryOrderActivity.this, mInfoList);
							//监听
							/*mAdapter.setmDelListener(new onDelItemClickListener() {

								@Override
								public void onDelItemClick(View v, int position) {
									// TODO Auto-generated method stub
									delList.add(mInfoList.get(position));
								}
							});
							mAdapter.setmNoDelListener(new onNoDelItemClickListener() {

								@Override
								public void onDelItemClick(View v, int position) {
									// TODO Auto-generated method stub
									delList.remove(mInfoList.get(position));
								}
							});*/
							mHistroyListView.setAdapter(mAdapter);//adapter和listview对接
							//rightBtn.setVisibility(View.VISIBLE);
						}else {
							mHistroyListView.setVisibility(View.INVISIBLE);
							no_dingdan.setVisibility(View.VISIBLE);
							no_dingdan.setText("没有订单信息");
							//no_dingdan.setTextColor(getResources().getColor(R.color.blue));
							
						//	emptyLayout.setVisibility(View.VISIBLE);
						//	rightBtn.setVisibility(View.GONE);
						}
					}
				}
			}
			
		});
		
	}

//public void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.btnRight:

			if (delFlag) {
				rightBtn.setText("编辑");
				leftBtn.setVisibility(View.GONE);
				mAdapter.setIsEditStyle(false);
				mAdapter.notifyDataSetChanged();
				delLayout.setVisibility(View.GONE);
				backLayout.setVisibility(View.VISIBLE);
				delFlag = false;
			} else {
				rightBtn.setText("完成");
				leftBtn.setVisibility(View.VISIBLE);
				backLayout.setVisibility(View.GONE);
				mAdapter.setIsEditStyle(true);
				mAdapter.notifyDataSetChanged();
				delLayout.setVisibility(View.VISIBLE);
				delFlag = true;
			}

			break;
			
		case R.id.btnLeft:
			
			mInfoList.clear();
			mAdapter.notifyDataSetChanged();
			emptyLayout.setVisibility(View.VISIBLE);
			leftBtn.setVisibility(View.GONE);
			rightBtn.setVisibility(View.GONE);
			break;

		case R.id.del_layout:

			mInfoList.removeAll(delList);
			mHistroyListView.setAdapter(mAdapter);
			if (mInfoList.size() == 0) {
				emptyLayout.setVisibility(View.VISIBLE);
				leftBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}*/
//	}

@Override
	 protected void onResume() {
		
	 super.onResume();
			 if(second == 1)
			 {
					 mInfoList.clear();
					 mAdapter.notifyDataSetChanged();
					 initData();
			 }else
			 {
				     second ++;
			 }
	 }
	 
}
