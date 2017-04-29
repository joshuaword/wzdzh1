package com.dajia.activity;

import net.k76.wzd.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class SelectAddressActivity extends BaseActivity implements
		OnGetGeoCoderResultListener , OnMapLoadedCallback{
	public final static String KEY_CURRENT_ADDRESS = "key_current_address";
	private final static String TAG = "SelectAddressActivity";
	private Context mContext;
	private TextView mAdd;
	private GeoCoder mSearch = null;

	private MapView mMapView = null;
	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BaiduMap mBaiduMap;
	private LatLng ll;
	private String mapCon;
	private TextView mapAddress;
	boolean isFirstLoc = true;// 是否首次定位
	private String x;
	private String y;
	private SharedPreferences settings;
	private boolean mHasAddress = false;

	private Button mButton;
	private Point mPoint;
	private Projection mProjection;
	private int mWindowWidth, mWindowHeight;
	private int mDpi;
	private Rect mCenter;
	private LatLng mFinishLng;
	OnMapStatusChangeListener onMapStatusChangeListener = new OnMapStatusChangeListener() {
		LatLng startLng;

		@Override
		public void onMapStatusChangeStart(MapStatus mapStatus) {
			startLng = mapStatus.target;
		}

		@Override
		public void onMapStatusChangeFinish(MapStatus mapStatus) {
			Log.d(TAG, "onMapStatusChangeFinish");
			if(mPoint != null && mProjection != null) {
				mFinishLng = mProjection.fromScreenLocation(mPoint);
				Log.d(TAG, "onMapStatusChangeFinish current lng:" + mFinishLng);
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mFinishLng));
			}

		}

		@Override
		public void onMapStatusChange(MapStatus mapStatus) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mButton = new Button(this);
		mButton.setText(R.string.add_new_address);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addAddress();
			}
		});

		setContentView(R.layout.activity_select_address);
		settings = getSharedPreferences("setting", 0);
		mapCon = settings.getString(mapCon, "14");
		((TextView) findViewById(R.id.title))
				.setText(getString(R.string.select_address));
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mWindowWidth = dm.widthPixels;
		mWindowHeight = dm.heightPixels;
		mDpi = dm.densityDpi/160;
	    Log.d(TAG, "screen metrics width:" + mWindowWidth + " heitht:" + mWindowHeight + " densityDpi/160:" + mDpi);
	    mCenter = new Rect(mWindowWidth/2 - 30*mDpi, mWindowHeight/2 - 68*mDpi, mWindowWidth/2 + 30*mDpi, mWindowHeight/2 - 8*mDpi);
	    Log.d(TAG, "mCenter:" + mCenter);
		mAdd = (TextView) findViewById(R.id.btnpay);
		mAdd.setText(R.string.ensure);
		mAdd.setVisibility(View.VISIBLE);
		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addAddress();
			}
		});
		mapAddress = (TextView) findViewById(R.id.mapview_address);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		LinearLayout localLayout = (LinearLayout) findViewById(R.id.request);
		localLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				if (mBaiduMap != null) {
					mBaiduMap.animateMapStatus(u);
				}
			}
		});
		initMap();
	}

	public void initMap() {
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		
		mBaiduMap.setOnMapLoadedCallback(this);
		
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(Float
				.valueOf(mapCon)));

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

		mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

	}

	private void addAddress() {
		if (mHasAddress) {
			x = String.valueOf(mFinishLng.longitude);
			y = String.valueOf(mFinishLng.latitude);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("x", x);
			editor.putString("y", y);
			editor.putString("address", mapAddress.getText().toString());
			editor.commit();
			
			Intent intent = new Intent(mContext, AddAddressActivity.class);
			intent.putExtra(KEY_CURRENT_ADDRESS, mapAddress.getText());
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(mContext, "请在地图上选择一个可用地址", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			ll = new LatLng(location.getLatitude(), location.getLongitude());
			if(mPoint == null && mProjection != null){
				Point p = mProjection.toScreenLocation(ll);
				Log.d(TAG,"onReceiveLocation point:" + p);
				if(mCenter.contains(p.x, p.y)) {
					mPoint = p;
					mFinishLng = ll;
				    Log.d(TAG,"onReceiveLocation get correct point:" + mPoint);
				    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
				}
			}
			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				mapAddress.setText(location.getAddrStr());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			mapAddress.setText("暂时未能找到目标地址");
			mHasAddress = false;
			return;
		}
		Log.d(TAG, "onGetReverseGeoCodeResult:" + result);
		mHasAddress = true;
		mapAddress.setText(result.getAddress());

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
	}

	@Override
	public void onMapLoaded() {
		mProjection = mBaiduMap.getProjection();
	}
}
