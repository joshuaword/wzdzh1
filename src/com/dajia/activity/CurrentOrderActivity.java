/*
 * Copyright (C), 2014-2014, 联创车盟汽车服务有限公司
 * FileName: CurrentOrderActivity.java
 * Author:   Administrator
 * Date:     2014年11月30日 下午10:27:50
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.dajia.activity;

import java.util.Timer;
import java.util.TimerTask;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dajia.Bean.CurrentOrderBean;
import com.google.gson.Gson;

public class CurrentOrderActivity extends BaseActivity implements
		android.view.View.OnClickListener {

	private TextView title;
	private Button cancelBtn;
	private LinearLayout backLayout;

	MapView mMapView = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位

	TextView statusTitle;
	TextView content, btnpay;
	TextView time;
	TextView status;
	ImageView call;
	TextView btnRight;
	EditText edt;
	String dingdanid;
	String userid;
	String money;

	private Timer mTimer = new Timer(true);
	private TimerTask mTimerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_current_order);

		initView();
		initMap();

		SharedPreferences settings = getSharedPreferences("setting", 0);

		userid = settings.getString("userid", "");

		money = getIntent().getStringExtra("money");

		mTimerTask = new TimerTask() {
			public void run() {
				query();
			}
		};

		mTimer.schedule(mTimerTask, 5000, 5000); // 在1秒后每5秒执行一次定时器中的方法，比如本文为调用log.v打印输出。

		query();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title);
		title.setText("当前订单");
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(this);
		ImageView backimg=(ImageView) findViewById(R.id.back_img);
		backimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CurrentOrderActivity.this.finish();
			}
		});

		btnRight = (TextView) findViewById(R.id.btnRight);
		btnRight.setText("支付");
		btnRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CurrentOrderActivity.this,
						PayActivity.class);
				intent.putExtra("Dingdanid", dingdanid);
				startActivity(intent);
			}
		});

		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(this);

		statusTitle = (TextView) findViewById(R.id.tv_status_title);
		btnpay = (TextView) findViewById(R.id.btnpay);
		content = (TextView) findViewById(R.id.tv_status_content);
		time = (TextView) findViewById(R.id.tv_time);
		status = (TextView) findViewById(R.id.tv_status);

		call = (ImageView) findViewById(R.id.img_call_driver);
	}

	private void cancelDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定取消当前订单?");
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_view, null);
		builder.setView(layout);
		edt = (EditText) layout.findViewById(R.id.edt_input);
		builder.setPositiveButton(getResources().getString(R.string.ensure),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						FinalHttp fp = new FinalHttp();
						AjaxParams ap = new AjaxParams();
						ap.put("userid", userid);
						ap.put("act", "postok");
						ap.put("dingdanid", dingdanid);
						ap.put("quxiaoyuanyin", edt.getEditableText()
								.toString().trim());

						final SharedPreferences settings = getSharedPreferences(
								"setting", 0);
						String baseurl = settings.getString("baseurl",
								"http://wzd.k76.net");

						fp.post(baseurl + "/api/quxiaoclient.php", ap,
								new AjaxCallBack<Object>() {
									@Override
									public void onSuccess(Object t) {
										super.onSuccess(t);
										finish();
									}
								});

					}

				});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				});

		builder.create().show();

	}

	public void initMap() {
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			cancelDialog();
			break;
		case R.id.back_layout:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void query() {

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("act", "postok");

		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		fp.post(baseurl + "/api/mydaijiaclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Log.i("/api/mydaijiaclient.php", " 8.获取当前订单信息 ");
						Gson gson = new Gson();
						final CurrentOrderBean bean = gson.fromJson(
								t.toString(), CurrentOrderBean.class);

						if ("no".equals(bean.getIfhavedingdan())) {
							finish();
						} else {
							statusTitle.setText(bean.getStr3());
							content.setText(bean.getStr4());
							Log.i("CurrentOrderActivity",
									"************" + bean.getStr4());
							if (bean.getStr4().equals("正在为您代驾")) {
								content.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(
												CurrentOrderActivity.this,
												PayActivity.class);
										intent.putExtra("Dingdanid", dingdanid);
										startActivity(intent);
									}
								});
							}

							if (bean.getStr4().equals("正在为您代驾")) {
								btnpay.setVisibility(View.VISIBLE);
								btnpay.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(
												CurrentOrderActivity.this,
												PayActivity.class);
										intent.putExtra("Dingdanid", dingdanid);
										startActivity(intent);
									}
								});
							} else {
								btnpay.setVisibility(View.GONE);
							}
							time.setText(bean.getStr1());
							status.setText(bean.getStr2());
							dingdanid = bean.getDingdanid();

							call.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent(
											Intent.ACTION_DIAL,
											Uri.parse("tel:"
													+ bean.getTelphone()));
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
								}
							});
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);

					}
				});
	}

}
