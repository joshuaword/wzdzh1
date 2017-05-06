package com.dajia.activity;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;

import com.dajia.Bean.DriveDetailBean;
import com.dajia.Bean.DriverInfo;
import com.dajia.Bean.PinglunBean;
import com.dajia.Bean.UserBean;
import com.dajia.adapter.EvalutateAdapter;
import com.dajia.adapter.StorePicAdapter;
import com.dajia.view.myGridView;
import com.dajia.view.myListView;

import com.google.gson.Gson;
import android.content.DialogInterface;
import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class StoreDetailsActivity extends BaseActivity implements
		OnClickListener {
	private DriverInfo info;
	private UserBean userBean;

	private TextView title;
	private String companyid;
	private String driverid;
	private LinearLayout backLayout;
	private String typename;
	private RatingBar store_star;
	private TextView store_name;
	private TextView store_address;
	private ImageView store_phone;
	private Button btnCallDriver;
	private myListView mCommentListView;
	private String showname;
	private EvalutateAdapter mAdapter;
	private ArrayList<PinglunBean> mInfoList = new ArrayList<PinglunBean>();
	// 该车主暂没有评价
	private View mLayoutNoComments;
	private String storephone;
	private myGridView store_imggrid;
	private SharedPreferences settings;
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private ProgressDialog pd;
	private String chufadi;
	private Double startx;
	private Double starty;
	private String mudidi;
	private Double endx;
	private Double endy;
	private ImageView store_nav;
	private ArrayList<String> imglist;
	private StorePicAdapter picadapter;
	private ImageView store_confirm;
	private TextView store_diatance;
	private TextView store_sign,store_type,store_price;
	private String signstr;
	private String phoneString,passwordString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_detail);
		info = (DriverInfo) getIntent().getSerializableExtra("info");
		companyid = info.getCompanyid();
		driverid = info.getDriverid();
		signstr = info.getSign();
		title = (TextView) findViewById(R.id.title);
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		settings = getSharedPreferences("setting", 0);
		typename = settings.getString("selectfuwutype", "");
		phoneString = settings.getString("sp_phone", "");
		passwordString= settings.getString("sp_password", "");
		initView();
		initData();
	}

	public void initView() {
		store_star = (RatingBar) findViewById(R.id.store_star);
		store_name = (TextView) findViewById(R.id.store_name);
		store_address = (TextView) findViewById(R.id.store_address);
		store_nav = (ImageView) findViewById(R.id.store_nav);
		store_nav.setOnClickListener(this);
		store_phone = (ImageView) findViewById(R.id.store_telephone);
		store_phone.setOnClickListener(this);
		store_sign=(TextView) findViewById(R.id.store_sign);
		store_sign.setText(signstr);
		store_price=(TextView) findViewById(R.id.store_price);
		store_type=(TextView) findViewById(R.id.store_select_type);
		store_type.setText(typename);
		btnCallDriver = (Button) findViewById(R.id.store_order);
		btnCallDriver.setOnClickListener(this);
		store_confirm=(ImageView) findViewById(R.id.store_confirm);
		store_diatance=(TextView) findViewById(R.id.store_distance);
		mCommentListView = (myListView) findViewById(R.id.store_listView);
		store_imggrid = (myGridView) findViewById(R.id.store_pictures);
		store_imggrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StoreDetailsActivity.this,
						ScaleimgActivity.class);
				intent.putExtra("selectposition", position);
				intent.putStringArrayListExtra("imglist",imglist);
				startActivity(intent);
			}
		});
		this.mLayoutNoComments = findViewById(R.id.linear_review);
	}

	public void initData() {
		String userid = settings.getString("userid", "");
		String x = settings.getString("x", "");
		String y = settings.getString("y", "");

		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("x", x);
		ap.put("y", y);
		ap.put("driverid", driverid);
		ap.put("companyid", companyid);
		ap.put("fuwuxiangmu", typename);
		ap.put("act", "postok");

		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		fp.post(baseurl + "/api/chaxundriverclient.php", ap,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);

						Gson gson = new Gson();
						DriveDetailBean bean = gson.fromJson(t.toString(),
								DriveDetailBean.class);
						if ("success".equals(bean.getRet())) {
							showname = bean.getShowname();
							store_name.setText(showname);
							title.setText(bean.getTitle());
							String xingnum=bean.getXinji();
							if (TextUtils.isEmpty(xingnum)) {
								xingnum="0";
							}
							store_star.setRating(Float.valueOf(xingnum));
							store_address.setText(bean.getAddress());
							storephone = bean.getTelphone();
							store_price.setText(bean.getPrice()+"元");
							endx = bean.getX();
							endy = bean.getY();
							mudidi = bean.getAddress();

							if (bean.getConfirm().equals("yes")) {
								store_confirm.setBackgroundResource(R.drawable.dirver_confirm);
							}else {
								store_confirm.setBackgroundResource(R.drawable.dirver_noconfirm);
							}
							store_diatance.setText(bean.getShowinfo1());
							mInfoList = (ArrayList<PinglunBean>) bean
									.getPinglun();
							if (mInfoList.size() > 0 && mInfoList != null) {
								mAdapter = new EvalutateAdapter(
										StoreDetailsActivity.this, mInfoList);
								mCommentListView.setAdapter(mAdapter);
								mAdapter.notifyDataSetChanged();
							} else {
								mLayoutNoComments.setVisibility(View.VISIBLE);
							}

							imglist = bean.getHeadImageArray();
							picadapter = new StorePicAdapter(
									StoreDetailsActivity.this, imglist);
							store_imggrid.setAdapter(picadapter);
							picadapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(StoreDetailsActivity.this,
								"查询司机详情失败，请稍后再试", Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");

		String dengluhao = settings.getString("dengluhao", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("telphone", phoneString);
		params.put("password", passwordString);
		params.put("act", "postok");
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.store_order:
			if (TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(StoreDetailsActivity.this,
						ChatLoginActivity.class);
				startActivity(intent);
				return;
			}
			String ifshowcheliang = settings.getString("ifshowcheliang", "no");
			if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(StoreDetailsActivity.this,
						VehicleActivity.class);
				startActivity(intent);
				return;
			}
			Intent intent = new Intent(StoreDetailsActivity.this,
					CallDrvierForOtherActivity.class);
			intent.putExtra("driverid", driverid);
			intent.putExtra("companyid", companyid);
			intent.putExtra("showname", showname);
			startActivity(intent);

			break;
		case R.id.store_nav:
			pd = new ProgressDialog(StoreDetailsActivity.this);
			pd.setMessage("正在定位...");
			pd.show();
			// 定位初始化
			mLocClient = new LocationClient(StoreDetailsActivity.this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setIsNeedAddress(true);
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(5000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			break;
		case R.id.store_telephone:
			Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ storephone));
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent2);
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
			// TODO Auto-generated method stub
			if (location == null) {
				Toast.makeText(StoreDetailsActivity.this, "定位失败",
						Toast.LENGTH_LONG).show();
				pd.dismiss();
				return;
			}
			startx = location.getLongitude();
			starty = location.getLatitude();
			chufadi = location.getAddrStr();
			pd.dismiss();
			mLocClient.stop();
			if (!TextUtils.isEmpty(chufadi) && !TextUtils.isEmpty(mudidi)
					&& startx != null && starty != null && startx != 0.0
					&& starty != 0.0 && endx != null && endy != null
					&& endx != 0.0 && endy != 0.0) {
				launchNavigator();
			} else if (startx == null || starty == null || startx == 0.0
					|| starty == 0.0) {
				Toast.makeText(StoreDetailsActivity.this, "无法导航,暂无始发地gps",
						Toast.LENGTH_LONG).show();
			} else if (endx == null || endy == null || endx == 0.0
					|| endy == 0.0) {
				Toast.makeText(StoreDetailsActivity.this, "无法导航,暂无目的地gps",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(StoreDetailsActivity.this, "无法导航,暂无目的地或始发地点",
						Toast.LENGTH_LONG).show();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void launchNavigator() {
		LatLng pt1 = new LatLng(starty, startx);
		LatLng pt2 = new LatLng(endy, endx);
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = chufadi;
		para.endPoint = pt2;
		para.endName = mudidi;
		try {
			BaiduMapNavigation
					.openBaiduMapNavi(para, StoreDetailsActivity.this);

		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您尚未安装百度地图app或app版本过低!");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.create().show();
		}
	}
}
