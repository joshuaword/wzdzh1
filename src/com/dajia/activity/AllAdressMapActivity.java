package com.dajia.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.dajia.Bean.GuiJiInfo;
import com.dajia.Bean.Orderbean;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 
 * @author fucheng 2014年10月8日
 */
public class AllAdressMapActivity extends Activity {
	// 地图相关
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor mCurrentMarker;
	private InfoWindow mInfoWindow = null;
	private String orderId;
	private GuiJiInfo guiJiInfo;
	private Marker marker1, marker2, marker3;
	private View back_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.all_address_map);
		orderId = getIntent().getStringExtra("orderId");
		back_layout = findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		init_View();
		init_Data();
	}

	protected void init_View() {
		mMapView = (MapView) findViewById(R.id.all_mapview);
		mBaiduMap = mMapView.getMap();
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.ico_location_marker);
		getAddress();
	}

	protected void init_Data() {
		// TODO Auto-generated method stub
		initMapClick();
	}

	private void initMapClick() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				if (marker == marker1) {
					// 生成一个TextView用户在地图中显示InfoWindow
					TextView location = new TextView(getApplicationContext());
					location.setBackgroundResource(R.drawable.map_tip);
					location.setPadding(30, 20, 30, 50);
					location.setText(guiJiInfo.getChufa().getStr() + "\n"
							+ guiJiInfo.getChufa().getThetime());
					// 将marker所在的经纬度的信息转化成屏幕上的坐标
					final LatLng ll = marker.getPosition();
					Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					p.y -= 47;
					LatLng llInfo = mBaiduMap.getProjection()
							.fromScreenLocation(p);
					// 显示InfoWindow
					OnInfoWindowClickListener listener = null;
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							mBaiduMap.hideInfoWindow();
						}
					};
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(location), llInfo, 0, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == marker2) {
					// 生成一个TextView用户在地图中显示InfoWindow
					TextView location = new TextView(getApplicationContext());
					location.setBackgroundResource(R.drawable.map_tip);
					location.setPadding(30, 20, 30, 50);
					location.setText(guiJiInfo.getWancheng().getStr() + "\n"
							+ guiJiInfo.getWancheng().getThetime());
					// 将marker所在的经纬度的信息转化成屏幕上的坐标
					final LatLng ll = marker.getPosition();
					Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					p.y -= 47;
					LatLng llInfo = mBaiduMap.getProjection()
							.fromScreenLocation(p);
					// 显示InfoWindow
					OnInfoWindowClickListener listener = null;
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							mBaiduMap.hideInfoWindow();
						}
					};
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(location), llInfo, 0, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == marker3) {
					// 生成一个TextView用户在地图中显示InfoWindow
					TextView location = new TextView(getApplicationContext());
					location.setBackgroundResource(R.drawable.map_tip);
					location.setPadding(30, 20, 30, 50);
					location.setText(guiJiInfo.getJiedan().getStr() + "\n"
							+ guiJiInfo.getJiedan().getThetime());
					// 将marker所在的经纬度的信息转化成屏幕上的坐标
					final LatLng ll = marker.getPosition();
					Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					p.y -= 47;
					LatLng llInfo = mBaiduMap.getProjection()
							.fromScreenLocation(p);
					// 显示InfoWindow
					OnInfoWindowClickListener listener = null;
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							mBaiduMap.hideInfoWindow();
						}
					};
					mInfoWindow = new InfoWindow(BitmapDescriptorFactory
							.fromView(location), llInfo, 0, listener);
					mBaiduMap.showInfoWindow(mInfoWindow);
				}

				return true;
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();

			}
		});
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay(List<Orderbean> signitems) {
		mBaiduMap.clear();
		if (signitems.size() == 1) {
			addMarker(signitems);
		} else if (signitems.size() >= 2) {
			addPolylineOptions(signitems);
		}
		if (guiJiInfo != null & guiJiInfo.getBegindaijia().size() > 0) {
			addInfosOverlay2(guiJiInfo.getBegindaijia());
		}
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay2(List<Orderbean> signitems) {
		if (signitems.size() == 1) {
			addMarker(signitems);
		} else if (signitems.size() >= 2) {
			addPolylineOptions2(signitems);
			addBeginMarker();
		}
	}

	/**
	 * 加入折线
	 * 
	 * @param signitems
	 */
	private void addPolylineOptions(List<Orderbean> signitems) {
		LatLng polyii = null;
		ArrayList<LatLng> polyiiArry = new ArrayList<LatLng>();
		for (int i = 0; i < signitems.size(); i++) {
			polyii = new LatLng(signitems.get(i).getY(), signitems.get(i)
					.getX());
			polyiiArry.add(polyii);
		}
		List<LatLng> points = new ArrayList<LatLng>();
		points.addAll(polyiiArry);
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(Color.parseColor("#008000")).points(points);
		mBaiduMap.addOverlay(ooPolyline);
	}

	/**
	 * 加入折线
	 * 
	 * @param signitems
	 */
	private void addPolylineOptions2(List<Orderbean> signitems) {
		LatLng polyii = null;
		ArrayList<LatLng> polyiiArry = new ArrayList<LatLng>();
		for (int i = 0; i < signitems.size(); i++) {
			polyii = new LatLng(signitems.get(i).getY(), signitems.get(i)
					.getX());
			polyiiArry.add(polyii);
		}
		List<LatLng> points = new ArrayList<LatLng>();
		points.addAll(polyiiArry);
		OverlayOptions ooPolyline = new PolylineOptions().width(10)
				.color(Color.parseColor("#FF0000")).points(points);
		mBaiduMap.addOverlay(ooPolyline);
	}

	/**
	 * 加入覆盖物
	 * 
	 * @param signitems
	 */
	private void addBeginMarker() {
		if (guiJiInfo != null && guiJiInfo.getChufa() != null
				&& guiJiInfo.getChufa().getY() != null
				&& guiJiInfo.getChufa().getX() != null) {
			OverlayOptions overlayOptions = null;
			marker1 = null;
			// 图标
			overlayOptions = new MarkerOptions()
					.position(
							new LatLng(guiJiInfo.getChufa().getY(), guiJiInfo
									.getChufa().getX())).icon(mCurrentMarker)
					.zIndex(5);
			marker1 = (Marker) (mBaiduMap.addOverlay(overlayOptions));
		}

		if (guiJiInfo != null && guiJiInfo.getWancheng() != null
				&& guiJiInfo.getWancheng().getY() != null
				&& guiJiInfo.getWancheng().getX() != null) {
			OverlayOptions overlayOptions1 = null;
			marker2 = null;
			// 图标
			overlayOptions1 = new MarkerOptions()
					.position(
							new LatLng(guiJiInfo.getWancheng().getY(),
									guiJiInfo.getWancheng().getX()))
					.icon(mCurrentMarker).zIndex(5);
			marker2 = (Marker) (mBaiduMap.addOverlay(overlayOptions1));
		}

		if (guiJiInfo != null && guiJiInfo.getJiedan() != null
				&& guiJiInfo.getJiedan().getY() != null
				&& guiJiInfo.getJiedan().getX() != null) {
			OverlayOptions overlayOptions2 = null;
			marker3 = null;
			// 图标
			overlayOptions2 = new MarkerOptions()
					.position(
							new LatLng(guiJiInfo.getJiedan().getY(), guiJiInfo
									.getJiedan().getX())).icon(mCurrentMarker)
					.zIndex(5);
			marker3 = (Marker) (mBaiduMap.addOverlay(overlayOptions2));
		}

		if (guiJiInfo != null && guiJiInfo.getChufa() != null
				&& guiJiInfo.getChufa().getY() != null
				&& guiJiInfo.getChufa().getX() != null) {
			// 将地图移到到最后一个经纬度位置
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(
					guiJiInfo.getChufa().getY(), guiJiInfo.getChufa().getX()));
			mBaiduMap.setMapStatus(u);
			float zoomLevel = Float.parseFloat(18 + "");
			MapStatusUpdate mapZoom = MapStatusUpdateFactory.zoomTo(zoomLevel);
			mBaiduMap.animateMapStatus(mapZoom);
		}

		if (guiJiInfo != null && guiJiInfo.getWancheng() != null
				&& guiJiInfo.getWancheng().getY() != null
				&& guiJiInfo.getWancheng().getX() != null) {
			// 将地图移到到最后一个经纬度位置
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(
					guiJiInfo.getWancheng().getY(), guiJiInfo.getWancheng()
							.getX()));
			mBaiduMap.setMapStatus(u);
			float zoomLevel = Float.parseFloat(18 + "");
			MapStatusUpdate mapZoom = MapStatusUpdateFactory.zoomTo(zoomLevel);
			mBaiduMap.animateMapStatus(mapZoom);
		}

		if (guiJiInfo != null && guiJiInfo.getJiedan() != null
				&& guiJiInfo.getJiedan().getY() != null
				&& guiJiInfo.getJiedan().getX() != null) {
			// 将地图移到到最后一个经纬度位置
			MapStatusUpdate u = MapStatusUpdateFactory
					.newLatLng(new LatLng(guiJiInfo.getJiedan().getY(),
							guiJiInfo.getJiedan().getX()));
			mBaiduMap.setMapStatus(u);
			float zoomLevel = Float.parseFloat(18 + "");
			MapStatusUpdate mapZoom = MapStatusUpdateFactory.zoomTo(zoomLevel);
			mBaiduMap.animateMapStatus(mapZoom);
		}

	}

	/**
	 * 加入覆盖物
	 * 
	 * @param signitems
	 */
	private void addMarker(List<Orderbean> signitems) {
		LatLng latLng = null;
		LatLng lastlatLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		for (Orderbean signitem : signitems) {
			// 位置
			latLng = new LatLng(signitem.getY(), signitem.getX());
			// 最后一个签到点的位置
			lastlatLng = new LatLng(signitems.get(0).getY(), signitems.get(0)
					.getX());
			// 图标
			overlayOptions = new MarkerOptions().position(latLng)
					.icon(mCurrentMarker).zIndex(5);
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putSerializable("signitem", signitem);
			marker.setExtraInfo(bundle);
		}

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(lastlatLng);
		mBaiduMap.setMapStatus(u);
		float zoomLevel = Float.parseFloat(18 + "");
		MapStatusUpdate mapZoom = MapStatusUpdateFactory.zoomTo(zoomLevel);
		mBaiduMap.animateMapStatus(mapZoom);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// activity 暂停时同时暂停地图控件
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity 销毁时同时销毁地图控件
		mMapView.onDestroy();
	}

	private void getAddress() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");
		ap.put("userid", userid);
		ap.put("dingdanid", orderId);
		ap.put("act", "postok");
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		fp.post(baseurl + "/api/showguijiclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						GuiJiInfo msg = gson.fromJson(t.toString(),
								GuiJiInfo.class);
						guiJiInfo = msg;
						if (msg.getBefordaijia() != null
								&& msg.getBefordaijia().size() > 0) {
							addInfosOverlay(msg.getBefordaijia());
						}
					}
				});
	}

}
