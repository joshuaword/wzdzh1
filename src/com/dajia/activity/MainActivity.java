package com.dajia.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.k76.wzd.R;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.push.example.Utils;
import com.dajia.VehicleApp;
import com.dajia.Bean.CurrentOrderBean;
import com.dajia.Bean.DriveBean;
import com.dajia.Bean.DriverInfo;
import com.dajia.Bean.MessageBean;
import com.dajia.Bean.UserBean;
import com.dajia.adapter.DriverListAdapter;
import com.dajia.util.Assign_UpDateDialog;
import com.dajia.util.ConfirmDialogListener;
import com.dajia.util.L;
import com.dajia.view.RoundImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.twzs.core.download.Downloadhelper;

/**
 * 主页面
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	// private Fragment menuFragment;
	public static final String DOWNLOADPATH = "download";
	private Button leftBtn;
	private Button rightBtn;
	private LinearLayout localLayout;
	private TextView mapAddress;
	private String typename, servicename;
	private LinearLayout driveListLayout;

	public static final String KEY_INTENT_DATA = "key_intent_data";

	MapView mMapView = null;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	BaiduMap mBaiduMap;
	boolean isFirstLoc = true;// 是否首次定位
	private FrameLayout infoWindowLayout;

	private Boolean mapOrList = true;
	private ListView driveListView;

	private LatLng ll;

	private Button btn_calldriverOther;
	private Button btn_calldriverself;

	List<DriverInfo> list;
	private LatLng[] mLatLng;
	private int position = 0;

	private String x;
	private String y;

	private String mapCon;
	String userid;
	LinearLayout viewLocation;
	LinearLayout mapCalldriverLayout;
	FrameLayout orderContentLayout;

	RelativeLayout mainTop;
	FrameLayout orderTop;

	TextView statusTitle;
	TextView content;
	TextView time;
	TextView status;
	ImageView call;
	TextView btnZhifu;
	EditText edt;
	String dingdanid;
	String money;
	private Button cancelBtn;
	private LinearLayout backLayout;
	private String baseurl;
	private Timer mTimer;
	private TimerTask mTimerTask;
	SharedPreferences settings;
	int i = 0;
	private UserBean userBean;
	public static final String TEMPPATH = "temp";
	public static List<MessageBean> messagelist = new ArrayList<MessageBean>();
	public static String unRead;
	int akBtnId = 0;
	int initBtnId = 0;
	int richBtnId = 0;
	int setTagBtnId = 0;
	int delTagBtnId = 0;
	int clearLogBtnId = 0;
	int showTagBtnId = 0;
	int unbindBtnId = 0;
	int setunDisturbBtnId = 0;
	public static int initialCnt = 0;
	private String ifshowcheliang, ifmapfirst;
	private String phoneString,passwordString;
	private String ifcanpay;
	private String ifdriverull;
	private LinearLayout driverEmptyLayout;

	private BroadcastReceiver cancleRec = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			mapCalldriverLayout.setVisibility(View.VISIBLE);
			orderContentLayout.setVisibility(View.GONE);
			mainTop.setVisibility(View.VISIBLE);
			orderTop.setVisibility(View.GONE);
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			if (mTimerTask != null) {
				mTimerTask.cancel();
				mTimerTask = null;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		setContentView(R.layout.activity_main);

		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(MainActivity.this, "api_key"));
		settings = getSharedPreferences("setting", 0);
		mapCon = settings.getString("mapCon", "16");// 地图缩放倍数
		userid = settings.getString("userid", "");
		baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		ifshowcheliang = settings.getString("ifshowcheliang", "no");
		ifmapfirst = settings.getString("ifmapfirst", "yes");
		typename = settings.getString("selectfuwutype", "");
		servicename = settings.getString("selectfuwulx", "");
		phoneString = settings.getString("sp_phone", "");
		passwordString= settings.getString("sp_password", "");
		Log.e("MainActivity", "地图：" + mapCon + "userid:" + userid);
		registerReceiver(cancleRec, new IntentFilter("cancle"));

		initView();
		initData();
		initMap();

		// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
		// PushManager.enableLbs(getApplicationContext());
		// Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
		// 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
		// 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void initView() {

		TextView titlebar = (TextView) findViewById(R.id.title);
		titlebar.setText("我知道--" + typename);

		leftBtn = (Button) findViewById(R.id.btnLeft);
		leftBtn.setOnClickListener(this);
		rightBtn = (Button) findViewById(R.id.btnRight);
		rightBtn.setOnClickListener(this);

		localLayout = (LinearLayout) findViewById(R.id.request);
		// localLayout.setOnClickListener(this);

		mapAddress = (TextView) findViewById(R.id.mapview_address);

		this.btn_calldriverself = ((Button) findViewById(R.id.btn_calldriverself));
		this.btn_calldriverself.setOnClickListener(this);
		this.btn_calldriverOther = ((Button) findViewById(R.id.btn_calldriverother));
		this.btn_calldriverOther.setOnClickListener(this);

		viewLocation = (LinearLayout) findViewById(R.id.view_location);

		// mTime = new TimeCount(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL);
		mainTop = (RelativeLayout) findViewById(R.id.main_top);
		orderTop = (FrameLayout) findViewById(R.id.order_top);

		mapCalldriverLayout = (LinearLayout) findViewById(R.id.map_calldriver_Layout);
		orderContentLayout = (FrameLayout) findViewById(R.id.order_content_layout);

		btnZhifu = (TextView) findViewById(R.id.zhifu);
		btnZhifu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, PayActivity.class);
				intent.putExtra("Dingdanid", dingdanid);
				System.out.println("dingdaniddingdaniddingdaniddingdanid"
						+ dingdanid);
				startActivity(intent);
			}
		});

		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancelDialog();
			}
		});
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(this);
		statusTitle = (TextView) findViewById(R.id.tv_status_title);
		content = (TextView) findViewById(R.id.tv_status_content);
		time = (TextView) findViewById(R.id.tv_time);
		status = (TextView) findViewById(R.id.tv_status);

		call = (ImageView) findViewById(R.id.img_call_driver);

		// 创建InfoWindow展示的view
		infoWindowLayout = (FrameLayout) LayoutInflater.from(MainActivity.this)
				.inflate(R.layout.overlay_driver, null);

	}

	public void initData() {
		driveListLayout = (LinearLayout) findViewById(R.id.driver_list_layout);
		driveListView = (ListView) findViewById(R.id.driver_list);
		driverEmptyLayout = (LinearLayout) findViewById(R.id.driver_empty_layout);
		TextView emptytv = (TextView) findViewById(R.id.driver_empty_tv);
		if (servicename.contains("上门洗车")) {
			emptytv.setText("附近没有发现洗车工 ");
		} else if (servicename.contains("到店洗车")) {
			emptytv.setText("附近没有发现洗车店 ");
		} else if (servicename.contains("代驾")) {
			emptytv.setText("附近没有发现代驾司机 ");
		} else {
			emptytv.setText("附近没有发现 " + servicename + "员");
		}
	}

	public void initMap() {
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);// 去掉百度地图自带的缩放控件
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(Float
				.valueOf(mapCon)));
		/*
		 * mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(
		 * ll,Float.valueOf(mapCon)));
		 */

		// 定位初始化
		// LocationClient类必须在主线程中声明。需要Context类型的参数。
		// Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context

		mLocClient = new LocationClient(getApplicationContext());// 声明locationClient类(定位的核心类)
		mLocClient.registerLocationListener(myListener);// 注册监听函数
		LocationClientOption option = new LocationClientOption();// 该类用来设置定位SDK的定位方式
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms
		mLocClient.setLocOption(option);
		mLocClient.start();// 开始定位
		// 定位到中间并设置缩放倍数
		localLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll,
						Float.valueOf(mapCon)));
			}
		});
	}

	public void initdirverlist() {
		if (x.length() > 0 && y.length() > 0) {
			try {
				FinalHttp fp = new FinalHttp();

				AjaxParams ap = new AjaxParams();
				ap.put("userid", userid);
				ap.put("x", x);
				ap.put("y", y);
				ap.put("fuwuxiangmu", typename);
				ap.put("act", "postok");
				fp.post(baseurl + "/api/driverlistjson.php", ap,
						new AjaxCallBack<Object>() {
							@Override
							public void onSuccess(Object t) {
								Gson gson = new Gson();

								DriveBean driveBean = gson.fromJson(
										t.toString(), DriveBean.class);
								if ("success".equals(driveBean.getRet())) {
									if (driveBean.getList() != null
											&& driveBean.getList().size() > 0) {
										List<DriverInfo> list = driveBean
												.getList();
										DriverListAdapter driveAdapter = new DriverListAdapter(
												MainActivity.this, list);
										driveListView.setAdapter(driveAdapter);
										driveAdapter.notifyDataSetChanged();
									} else {
										rightBtn.setBackgroundResource(R.drawable.drivermap_btn);
										mMapView.setVisibility(View.GONE);
										driveListLayout
												.setVisibility(View.GONE);
										localLayout.setVisibility(View.GONE);
										driverEmptyLayout
												.setVisibility(View.VISIBLE);
										ifdriverull = "yes";
									}

								}
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								L.e("aaaa", t.toString() + "----------"
										+ errorNo + "------" + strMsg);
							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			rightBtn.setBackgroundResource(R.drawable.drivermap_btn);
			mMapView.setVisibility(View.GONE);
			driveListLayout.setVisibility(View.VISIBLE);
			localLayout.setVisibility(View.GONE);
			mapOrList = false;
		} else {
			Toast.makeText(MainActivity.this, "正在定位中，请稍候再试", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			finish();
			break;
		case R.id.request:
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			// requestLocation();
			break;

		case R.id.btnRight:
			if (mapOrList) {
				initdirverlist();
			} else {
				queryMap();
				rightBtn.setBackgroundResource(R.drawable.driverlist_btn);
				mMapView.setVisibility(View.VISIBLE);
				driveListLayout.setVisibility(View.GONE);
				localLayout.setVisibility(View.VISIBLE);
				driverEmptyLayout.setVisibility(View.GONE);
				mapOrList = true;
			}
			break;

		case R.id.btn_calldriverself:
			// mConfirmOrderView.setVisibility(View.VISIBLE);
			// mTime.start();
			// if (yanzhen()) {
			if (userBean != null && TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(MainActivity.this,
						ChatLoginActivity.class);
				startActivity(intent);
				return;
			}
			if ("yes".equals(ifshowcheliang) && userBean != null
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(MainActivity.this,
						VehicleActivity.class);
				startActivity(intent);
				return;
			}
			if (servicename.contains("到店洗车")) {
				Toast.makeText(MainActivity.this, "请您选择洗车店！", Toast.LENGTH_LONG)
						.show();
			} else {
				Intent intent = new Intent(MainActivity.this,
						CallDrvierForOtherActivity.class);
				intent.putExtra("titleFlag", "one");
				startActivity(intent);
			}
			/*
			 * if (typename.contains("到店") || typename.contains("封釉")) {
			 * Toast.makeText(MainActivity.this,"请您选择洗车店！",
			 * Toast.LENGTH_LONG).show(); } else { Intent intent = new
			 * Intent(MainActivity.this, CallDrvierForOtherActivity.class);
			 * intent.putExtra("titleFlag", "one"); startActivity(intent); }
			 */

			// finish();
			// }
			break;

		case R.id.btn_calldriverother:
			// 一键下单点击事件
			if (userBean != null && TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent1 = new Intent(MainActivity.this,
						ChatLoginActivity.class);// 跳转到登录
				startActivity(intent1);
				return;
			}
			if ("yes".equals(ifshowcheliang) && userBean != null
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent2 = new Intent(MainActivity.this,
						VehicleActivity.class);// 跳转到车辆添加
				startActivity(intent2);
				return;
			}
			if (servicename.contains("到店洗车")) {
				Toast.makeText(MainActivity.this, "请您选择洗车店！", Toast.LENGTH_LONG)
						.show();
			} else {
				Intent intentwo = new Intent(MainActivity.this,
						CallDrvierForOtherActivity.class);// 跳转到下单页面
				intentwo.putExtra("titleFlag", "two");
				Log.e("calltype++++222=", typename);
				intentwo.putExtra("selectfuwutype", typename);
				startActivity(intentwo);
			}
			break;

		case R.id.confirmOrderInfo_btn_confirm:
			// toCurrentOrder();
			// mTime.cancel();
			break;

		case R.id.confirmOrderInfo_btn_cancel:
			// mConfirmOrderView.setVisibility(View.GONE);
			// mTime.cancel();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// 退出时销毁定位
		mLocClient.stop();
		unregisterReceiver(cancleRec);
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		getLoginInfo();
		// 查询是否有订单
		queryNewOrder();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	/**
	 * 手动请求定位的方法
	 */
	public void requestLocation() {
		if (mLocClient != null && mLocClient.isStarted()) {
			mLocClient.requestLocation();
		}
	}

	/**
	 * 定位SDK监听函数 BDLocationListener接口有1个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
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
			ll = new LatLng(location.getLatitude(), location.getLongitude());
			mapAddress.setText(location.getAddrStr());
			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			x = String.valueOf(location.getLongitude());
			Log.e("11111++++x=", x);
			y = String.valueOf(location.getLatitude());
			if (ifmapfirst.equals("yes")) {
				queryMap();
			} else {
				initdirverlist();
			}

			SharedPreferences.Editor editor = settings.edit();
			editor.putString("x", x);
			editor.putString("y", y);
			editor.putString("address", location.getAddrStr());
			editor.putString("ifdriverull", ifdriverull);
			editor.commit();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	public void queryMap() {
		if ((!TextUtils.isEmpty(x)) && (!TextUtils.isEmpty(y))) {
			try {
				FinalHttp fp = new FinalHttp();
				AjaxParams ap = new AjaxParams();
				ap.put("userid", userid);
				ap.put("x", x);
				ap.put("y", y);
				ap.put("fuwuxiangmu", typename);
				ap.put("act", "postok");
				fp.post(baseurl + "/api/driverlistjson.php", ap,
						new AjaxCallBack<Object>() {
							@Override
							public void onSuccess(Object t) {
								Gson gson = new Gson();

								RatingBar rb = (RatingBar) infoWindowLayout
										.findViewById(R.id.driver_location_ratingBar);
								TextView name = (TextView) infoWindowLayout
										.findViewById(R.id.name);
								// TextView number = (TextView)
								// infoWindowLayout.findViewById(R.id.number);
								RoundImageView imageView = (RoundImageView) infoWindowLayout
										.findViewById(R.id.driver_img);

								ImageView imgBack = (ImageView) infoWindowLayout
										.findViewById(R.id.img_back);

								DriveBean driveBean = gson.fromJson(
										t.toString(), DriveBean.class);
								if ("success".equals(driveBean.getRet())) {
									list = driveBean.getList();
									int size = list.size();
									mLatLng = new LatLng[size];
									if (mBaiduMap != null) {
										mBaiduMap.clear();
									}
									if (list.size() > 0 && list != null) {
										ifdriverull = "no";
										for (int i = 0; i < size; i++) {

											DriverInfo info = list.get(i);

											LatLng ll = new LatLng(Double
													.valueOf(info.getY()),
													Double.valueOf(info.getX()));
											mLatLng[i] = ll;

											if ("1".equals(info.getBeijintu())) {
												imgBack.setBackgroundResource(R.drawable.green);
												name.setTextColor(getResources()
														.getColor(R.color.white));
											} else if ("2".equals(info
													.getBeijintu())) {
												imgBack.setBackgroundResource(R.drawable.red);
												name.setTextColor(getResources()
														.getColor(R.color.white));
											} else {
												imgBack.setBackgroundResource(R.drawable.overlay_green);
											}

											name.setText(info.getShowname());
											// number.setText(info.getGonghao());
											rb.setRating(Float.valueOf(info
													.getXinji()));

											FinalBitmap finalBitMap = FinalBitmap
													.create(MainActivity.this);

											finalBitMap
													.configBitmapLoadThreadSize(3);// 定义线程数量

											finalBitMap
													.configDiskCachePath(getExternalFilesDir(
															Environment.DIRECTORY_PICTURES)
															.getAbsolutePath()); // 设置缓存目录；
											finalBitMap
													.configDiskCacheSize(1024 * 1024 * 10); // 设置缓存大小

										/*	Bitmap load = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.default_driver);*/
											if (!TextUtils.isEmpty(info
													.getThefile())) {

												Uri path = Uri.parse(info
														.getThefile());
												Picasso.with(MainActivity.this)
														.load(path)
														.placeholder(
																R.drawable.default_driver)// 加载中显示
														.error(R.drawable.default_driver)// 失败显示
														.into(imageView);
												// imageView.setBackgroundResource(R.drawable.introduce2_2);

												// 构建Marker图标
												BitmapDescriptor bitmap = BitmapDescriptorFactory
														.fromView(infoWindowLayout);
												OverlayOptions option = new MarkerOptions()
														.position(ll).icon(
																bitmap);

												// 在地图上添加Marker，并显示
												mBaiduMap.addOverlay(option);
											}
										}

										mBaiduMap
												.setOnMarkerClickListener(new OnMarkerClickListener() {

													@Override
													public boolean onMarkerClick(
															Marker marker) {
														Intent intent = new Intent();

														if (servicename
																.contains("到店洗车")) {
															intent.setClass(
																	MainActivity.this,
																	StoreDetailsActivity.class);
														} else {
															intent.setClass(
																	MainActivity.this,
																	DriverDetails.class);
														}
														LatLng latLng = marker
																.getPosition();
														int size = mLatLng.length;
														for (int i = 0; i < size; i++) {
															if (mLatLng[i]
																	.equals(latLng)) {
																position = i;
															}
														}

														intent.putExtra(
																"info",
																list.get(position));
														startActivity(intent);

														return false;
													}
												});
									} else {
										ifdriverull = "yes";
									}
								}
							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								L.e("aaaa", t.toString() + "----------"
										+ errorNo + "------" + strMsg);
							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	public void queryMap2() {

		if ((!TextUtils.isEmpty(x)) && (!TextUtils.isEmpty(y))) {
			try {
				FinalHttp fp = new FinalHttp();
				AjaxParams ap = new AjaxParams();
				ap.put("userid", userid);
				ap.put("x", x);
				ap.put("y", y);
				ap.put("fuwuxiangmu", typename);
				ap.put("act", "postok");
				fp.post(baseurl + "/api/driverlistjson.php", ap,
						new AjaxCallBack<Object>() {
							@Override
							public void onSuccess(Object t) {
								Gson gson = new Gson();

								RatingBar rb = (RatingBar) infoWindowLayout
										.findViewById(R.id.driver_location_ratingBar);
								TextView name = (TextView) infoWindowLayout
										.findViewById(R.id.name);
								// TextView number = (TextView)
								// infoWindowLayout.findViewById(R.id.number);
								RoundImageView imageView = (RoundImageView) infoWindowLayout
										.findViewById(R.id.driver_img);
								ImageView imgBack = (ImageView) infoWindowLayout
										.findViewById(R.id.img_back);

								DriveBean driveBean = gson.fromJson(
										t.toString(), DriveBean.class);
								if (driveBean != null
										&& driveBean.getRet() != null) {
									if ("success".equals(driveBean.getRet())) {
										list = driveBean.getList();
										// int size = list.size();
										mLatLng = new LatLng[1];
										if (mBaiduMap != null) {
											mBaiduMap.clear();
										}

										// for (int i = 0; i < size; i++) {
										if (list != null && list.size() > 0) {
											DriverInfo info = list.get(0);

											LatLng ll = new LatLng(
													Double.valueOf(list.get(0)
															.getY()), Double
															.valueOf(list
																	.get(0)
																	.getX()));
											mLatLng[0] = ll;

											if ("1".equals(info.getBeijintu())) {
												imgBack.setBackgroundResource(R.drawable.green);
												name.setTextColor(getResources()
														.getColor(R.color.white));
											} else if ("2".equals(info
													.getBeijintu())) {
												imgBack.setBackgroundResource(R.drawable.red);
												name.setTextColor(getResources()
														.getColor(R.color.white));
											} else {
												imgBack.setBackgroundResource(R.drawable.overlay_green);
											}

											name.setText(info.getShowname());
											// number.setText(info.getGonghao());
											rb.setRating(Float.valueOf(info
													.getXinji()));

											FinalBitmap finalBitMap = FinalBitmap
													.create(MainActivity.this);

											finalBitMap
													.configBitmapLoadThreadSize(3);// 定义线程数量

											finalBitMap
													.configDiskCachePath(getExternalFilesDir(
															Environment.DIRECTORY_PICTURES)
															.getAbsolutePath()); // 设置缓存目录；
											finalBitMap
													.configDiskCacheSize(1024 * 1024 * 10); // 设置缓存大小

										/*	Bitmap load = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.default_driver);*/
											if (!TextUtils.isEmpty(info
													.getThefile())) {
												/*
												 * finalBitMap .display(
												 * imageView, info.getThefile(),
												 * load);
												 */
												Uri path = Uri.parse(info
														.getThefile());
												Picasso.with(MainActivity.this)
														.load(path)
														.placeholder(
																R.drawable.default_driver)// 加载中显示
														.error(R.drawable.default_driver)// 失败显示
														.into(imageView);
												// imageView.setBackgroundResource(R.drawable.introduce2_2);

												// 构建Marker图标
												BitmapDescriptor bitmap = BitmapDescriptorFactory
														.fromView(infoWindowLayout);
												OverlayOptions option = new MarkerOptions()
														.position(ll).icon(
																bitmap);

												// 在地图上添加Marker，并显示
												mBaiduMap.addOverlay(option);

												mBaiduMap
														.setOnMarkerClickListener(new OnMarkerClickListener() {

															@Override
															public boolean onMarkerClick(
																	Marker marker) {
																Intent intent = new Intent();

																if (servicename
																		.contains("到店洗车")) {
																	intent.setClass(
																			MainActivity.this,
																			StoreDetailsActivity.class);
																} else {
																	intent.setClass(
																			MainActivity.this,
																			DriverDetails.class);
																}
																LatLng latLng = marker
																		.getPosition();
																int size = mLatLng.length;
																for (int i = 0; i < size; i++) {
																	if (mLatLng[i]
																			.equals(latLng)) {
																		position = i;
																	}
																}

																intent.putExtra(
																		"info",
																		list.get(position));
																startActivity(intent);

																return false;
															}
														});
											}
										} else {
											ifdriverull = "yes";
										}

									}
								}

							}

							@Override
							public void onFailure(Throwable t, int errorNo,
									String strMsg) {
								L.e("aaaa", t.toString() + "----------"
										+ errorNo + "------" + strMsg);
							}
						});
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}

	/**
	 * 获取订单信息
	 */
	public void queryNewOrder() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		Log.e("MUSIC", "userid=" + userid);
		final String Dingdanid = getIntent().getStringExtra("dingDanId");
		ap.put("dingdanid", Dingdanid);
		ap.put("userid", userid);
		ap.put("act", "postok");
		fp.post(baseurl + "/api/mydaijiaclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);

						Log.i("/api/mydaijiaclient.php", " 8.获取当前订单信息 ");

						Gson gson = new Gson();
						final CurrentOrderBean bean = gson.fromJson(
								t.toString(), CurrentOrderBean.class);
						if (bean != null) {
							if (TextUtils.isEmpty(Dingdanid)) {
								dingdanid = bean.getDingdanid();
							} else {
								dingdanid = Dingdanid;
							}

							ifcanpay = bean.getIfcanpay();
							if ("yes".equals(ifcanpay)) {
								btnZhifu.setVisibility(View.VISIBLE);
							} else {
								btnZhifu.setVisibility(View.GONE);
							}
							if (bean.getState().equals("未指派")
									|| bean.getState().equals("执行中")) {
								orderContentLayout
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												// TODO Auto-generated method
												// stub

												Intent intent = new Intent(
														MainActivity.this,
														PaiDanActivity.class);
												intent.putExtra("Dingdanid",
														dingdanid);
												System.out
														.println("dingdaniddingdaniddingdaniddingdanid"
																+ dingdanid);
												startActivity(intent);

											}
										});
							} else {
								orderContentLayout
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												Intent intent = new Intent(
														MainActivity.this,
														DaijiaActivity.class);
												intent.putExtra("Dingdanid",
														dingdanid);
												System.out
														.println("dingdaniddingdaniddingdaniddingdanid"
																+ dingdanid);
												startActivity(intent);
											}
										});

							}
						}
						if ("yes".equals(bean.getIfhavedingdan())) {
							statusTitle.setText(bean.getStr3());
							content.setText(bean.getStr4());
							time.setText(bean.getStr1());
							status.setText(bean.getStr2());
							dingdanid = bean.getDingdanid();
							if (bean.getStr4().equals("正在为您代驾")) {
								content.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {

										Intent intent = new Intent(
												MainActivity.this,
												DaijiaActivity.class);
										intent.putExtra("Dingdanid", dingdanid);
										System.out
												.println("dingdaniddingdaniddingdaniddingdanid"
														+ dingdanid);
										startActivity(intent);

									}
								});
							} else if (bean.getStr4().equals("订单已经完成,点击评价")) {
								content.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {

										Intent intent = new Intent(
												MainActivity.this,
												PinglunActivity.class);
										intent.putExtra("dingDanId", dingdanid);
										System.out
												.println("dingdaniddingdaniddingdaniddingdanid"
														+ dingdanid);
										startActivity(intent);

									}
								});
							}

							if ("no".equals(bean.getIfquxiao())) {
								cancelBtn.setVisibility(View.GONE);
							}

							if ("".equals(bean.getTelphone())
									|| null == bean.getTelphone()) {
								call.setVisibility(View.GONE);
							} else {

							}

							mapCalldriverLayout.setVisibility(View.GONE);
							orderContentLayout.setVisibility(View.VISIBLE);
							mainTop.setVisibility(View.GONE);
							orderTop.setVisibility(View.VISIBLE);

							// if (mTimer != null) {
							mTimer = new Timer();
							mTimerTask = new TimerTask() {
								public void run() {
									queryNewOrder2();
									queryMap2();
								}
							};

							mTimer.schedule(mTimerTask, 5000, 5000);
							// }

						} else {
							// queryMap(x, y);
							mapCalldriverLayout.setVisibility(View.VISIBLE);
							orderContentLayout.setVisibility(View.GONE);
							mainTop.setVisibility(View.VISIBLE);
							orderTop.setVisibility(View.GONE);
							if (mTimer != null) {
								mTimer.cancel();
								mTimer = null;
							}

							if (mTimerTask != null) {
								mTimerTask.cancel();
								mTimerTask = null;
							}
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);

					}
				});

	}

	public void queryNewOrder2() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		final String Dingdanid = getIntent().getStringExtra("dingDanId");
		ap.put("dingdanid", Dingdanid);
		ap.put("userid", userid);
		ap.put("act", "postok");
		fp.post(baseurl + "/api/mydaijiaclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						final CurrentOrderBean bean = gson.fromJson(
								t.toString(), CurrentOrderBean.class);
						if (bean != null) {
							if (TextUtils.isEmpty(Dingdanid)) {
								dingdanid = bean.getDingdanid();
							} else {
								dingdanid = Dingdanid;
							}
							ifcanpay = bean.getIfcanpay();
							if ("yes".equals(ifcanpay)) {
								btnZhifu.setVisibility(View.VISIBLE);
							} else {
								btnZhifu.setVisibility(View.GONE);
							}
							if (bean.getState().equals("未指派")
									|| bean.getState().equals("执行中")) {
								orderContentLayout
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {

												Intent intent = new Intent(
														MainActivity.this,
														PaiDanActivity.class);
												intent.putExtra("Dingdanid",
														dingdanid);
												System.out
														.println("dingdaniddingdaniddingdaniddingdanid"
																+ dingdanid);
												startActivity(intent);

											}
										});
							} else {
								orderContentLayout
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View arg0) {
												Intent intent = new Intent(
														MainActivity.this,
														DaijiaActivity.class);
												intent.putExtra("Dingdanid",
														dingdanid);
												System.out
														.println("dingdaniddingdaniddingdaniddingdanid"
																+ dingdanid);
												startActivity(intent);
											}
										});
							}
						}
						String thestr = "";
						if (bean == null) {
							thestr = "";
						} else {
							thestr = bean.getIfhavedingdan();
						}

						if (thestr == null || thestr.trim().length() == 0) {
							thestr = "no";
						}

						if ("yes".equals(thestr)) {
							statusTitle.setText(bean.getStr3());
							content.setText(bean.getStr4());
							time.setText(bean.getStr1());
							status.setText(bean.getStr2());
							dingdanid = bean.getDingdanid();
							if (bean.getStr4().equals("正在为您代驾")) {
								content.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {

										Intent intent = new Intent(
												MainActivity.this,
												DaijiaActivity.class);
										intent.putExtra("Dingdanid", dingdanid);
										System.out
												.println("dingdaniddingdaniddingdaniddingdanid"
														+ dingdanid);
										startActivity(intent);

									}
								});
							} else if (bean.getStr4().equals("订单已经完成,点击评价")) {
								content.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View arg0) {

										Intent intent = new Intent(
												MainActivity.this,
												PinglunActivity.class);
										intent.putExtra("dingDanId", dingdanid);
										System.out
												.println("dingdaniddingdaniddingdaniddingdanid"
														+ dingdanid);
										startActivity(intent);

									}
								});
							}

							if ("no".equals(bean.getIfquxiao())) {
								cancelBtn.setVisibility(View.GONE);
							}

							if ("".equals(bean.getTelphone())
									|| null == bean.getTelphone()) {
								call.setVisibility(View.GONE);
							} else {

							}

							mapCalldriverLayout.setVisibility(View.GONE);
							orderContentLayout.setVisibility(View.VISIBLE);
							mainTop.setVisibility(View.GONE);
							orderTop.setVisibility(View.VISIBLE);

						} else {
							mapCalldriverLayout.setVisibility(View.VISIBLE);
							orderContentLayout.setVisibility(View.GONE);
							mainTop.setVisibility(View.VISIBLE);
							orderTop.setVisibility(View.GONE);
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);

					}
				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}

			if (mTimerTask != null) {
				mTimerTask.cancel();
				mTimerTask = null;
			}
			VehicleApp.getInstance().exit();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			String content = data.getStringExtra("content");
			mapAddress.setText(content);

			SharedPreferences.Editor editor = settings.edit();
			editor.putString("address", content);
			editor.commit();
		}
	}

	private void cancelDialog() {

		String title = settings.getString("quxiao", "取消原因");
		final String yuanyin1 = settings.getString("quxiaoyuanyin1", "代驾员态度不好");
		final String yuanyin2 = settings.getString("quxiaoyuanyin2", "收费有点贵");
		final String yuanyin3 = settings.getString("quxiaoyuanyin3", "其他原因");

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(title);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_view, null);
		edt = (EditText) layout.findViewById(R.id.edt_input);
		TextView tv1 = (TextView) layout.findViewById(R.id.yuanyin1);
		tv1.setText(yuanyin1);
		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin1);
			}
		});
		TextView tv2 = (TextView) layout.findViewById(R.id.yuanyin2);
		tv2.setText(yuanyin2);
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin2);
			}
		});
		TextView tv3 = (TextView) layout.findViewById(R.id.yuanyin3);
		tv3.setText(yuanyin3);
		tv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				edt.setText(yuanyin3);
			}
		});
		builder.setView(layout);
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
						fp.post(baseurl + "/api/quxiaoclient.php", ap,
								new AjaxCallBack<Object>() {
									@Override
									public void onSuccess(Object t) {
										super.onSuccess(t);
										mapCalldriverLayout
												.setVisibility(View.VISIBLE);
										orderContentLayout
												.setVisibility(View.GONE);
										mainTop.setVisibility(View.VISIBLE);
										orderTop.setVisibility(View.GONE);

										if (mTimer != null) {
											mTimer.cancel();
											mTimer = null;
										}

										if (mTimerTask != null) {
											mTimerTask.cancel();
											mTimerTask = null;
										}
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

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			}
		}
	}

	public Boolean yanzhen() {
		SharedPreferences sp = getSharedPreferences("setting", 0);
		//String phone = sp.getString("phone", "");
		String yanzhenma = sp.getString("ifyanzhenma", "no");
		if ("yes".equals(yanzhenma)) {
			if (TextUtils.isEmpty(phoneString)) {
				Intent intent = new Intent(MainActivity.this,
						ChatLoginActivity.class);
				startActivity(intent);
				return false;
			}
		}
		return true;
	}

	public static final String getAppDownloadSDPath() {
		File file = new File(getAppSDPath(), DOWNLOADPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}

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
	public void showUpdate_Dialog(String newVersion, final String downUrl,
			final String isAPK) {
		String newverStr = "有新版本更新啦！";
		final String version_downloading = this.getResources().getString(
				R.string.version_downloading, newVersion);
		final String installapkfile = getAppTmpSDPath() + "/"
				+ getFileNameFromUrl(downUrl);
		Assign_UpDateDialog.showUpdateDialog(this, newverStr, "增加新功能，优化性能！",
				"立即尝鲜", "残忍拒绝", new ConfirmDialogListener() {
					@Override
					public void onPositive(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if (isAPK.equals("yes")) {
							Downloadhelper dm = new Downloadhelper(
									MainActivity.this, true);
							dm.downLoadFile(version_downloading, downUrl,
									installapkfile);
							dialog.cancel();
						} else {
							if (downUrl != null && !downUrl.equals("")) {

								Intent intent = new Intent(MainActivity.this,
										CheckHtmlActivity.class);
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

	private void getLoginInfo() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String dengluhao = sp.getString("dengluhao", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("telphone", phoneString);
		params.put("password", passwordString);
		params.put("qquid", settings.getString("qquid", ""));
		params.put("weixinuid", settings.getString("weixinuid", ""));
		params.put("act", "postok");
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {
							SharedPreferences sp = getSharedPreferences(
									"setting", Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sp.edit();
							editor.putString("userid", userBean.getUserid());
							editor.commit();
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
}
