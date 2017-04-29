 package com.dajia.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.k76.wzd.R;
import net.k76.wzd.wxapi.WXPayEntryActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.dajia.Bean.DriveBean;
import com.dajia.Bean.UserBean;
import com.dajia.Bean.YouhuiJuanInfo;
import com.dajia.adapter.YouhuijuanAdapter;
import com.dajia.widgets.WheelView;

import com.google.gson.Gson;

/**
 * 一键下单
 * 
 * @author Administrator
 * 
 */
@SuppressLint("NewApi")
public class CallDrvierForOtherActivity extends BaseActivity implements
		OnClickListener, OnGetRoutePlanResultListener {
	private static final String TAG = "CallDrvierForOtherActivity";
	public int REQUEST_CODE = 997;
	RoutePlanSearch mSearch; // 搜索模块，
	private TextView title;
	private LinearLayout backLayout;
	private YouhuijuanAdapter youhuijuanadapter;
	private Button subBtn;
	private Button addBtn;
	private int num = 1;
	private EditText edtNum;
	private Button callOrderBtn;
	// private ConfirmOrderView mConfirmOrderView;
	// private Button confirmOrderBtn;
	// private Button cancelOrderBtn;
	private int youhuijuanJin;
	private int orderallmoney;
	private int chongmoney;
	private TextView timetxt;
	// TextView phone;
	TextView chufadiTv;
	TextView mudidiTv;
	TextView typeTv, fuwushijianTv;
	TextView remarkTv;
	String showType;
	String youhuiquanid;
	String driverid;
	String companyid;
	String type = "0";
	String money = "20";
	String price = "";
	private UserBean userBean;
	LinearLayout timeLayout;
	LinearLayout countLayout, juli_layout;// 公里数布局，根据是否有目的地显示
	String titleFlag;
	LinearLayout nameLayout;
	String showname;
	String[] fuwuxiangmu, fuwuxiangmuprice, fuwushijian;
	int needpay = 0, useryue = 0;
	String fuwuxiangmuid;
	String ifxiadancongzi;
	String ifnodrivernodan;
	String ifcanyuyue;
	private String yuyuebegintime;
	private String yuyueendtime;
	private String yuyueneedtime;
	private String selectime;
	private String defaulttime;
	private String yuyuebegin;
	private String x, y;// 出发地坐标
	private String x2, y2;// 目的地坐标
	private String chufa, mudi;// 出发地和目的地
	private String strJuLi;
	private String yuyuechufashijian;
	// private TimeCount mTime;
	// private static final long MILLIS_IN_FUTURE = 5000; // 总额时间数
	// private static final long COUNT_DOWN_INTERVAL = 1000; // 计数间隔时间
	// private View youhuijuan_layout;
	private View chepai_layout;
	private TextView youhuijuan_txt, chepaihao_tv;
	// private TextView yue_txt;
	// private TextView zhifu_text;
	private ListView listView;
	private Set<String> setPriceString;
	private String chepai, chexing, from;
	// private TextView sex;
	// private TextView name;
	private String ifshowcheliang;
	private String zhifuType = "accountpay";
	private CheckBox carcapay_checkbox;
	private CheckBox accountpay_checkbox;
	//private Button recharge_btn;
	private String phoneString, passwordString, kehuleixing;

	//private String latStart, lonStart, latEnd, lonEnd;
	double latS, lonS, latE, lonE;
	boolean jisuan = false;
	private BroadcastReceiver finish = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			from = arg1.getStringExtra("from");
			chepai = arg1.getStringExtra("chepai");
			chexing = arg1.getStringExtra("chexing");
			chepaihao_tv.setText(chexing + "  " + chepai);
		}
	};
	private String ifshowdrivernum;
	private TextView payall_tv;
	private TextView youhui_tv;
	private TextView payneed_tv;
	private TextView haoyue_tv;
	private TextView qing, chong_tv;// 请充值
	private EditText pwd_ed;
	private EditText kanum_ed;
	private String ifMudidi;
	private int currenttime;
	private String ifdriverull;
	private String currentdate;
	private String currentmin;
	private TextView juli_tv;
	private int select_start, select_end;
	private RelativeLayout layout_accountpay;
	private RelativeLayout layout_carcapay;
	private String yuYue;
	private String tomoorwdate, afterdate;
	private String[] times1 = new String[24];
	private String[] times2 = new String[24];
	private String[] times3 = new String[24];
	private ArrayList<String> timeslist = new ArrayList<String>();
	private String typename, servicename;
	private SharedPreferences settings;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_driver_for_other);
		registerReceiver(finish, new IntentFilter("finish"));

		juli_tv = (TextView) findViewById(R.id.juli_tv);// 公里数
		mSearch = RoutePlanSearch.newInstance();// 搜索初始化（测距离）
		mSearch.setOnGetRoutePlanResultListener(this);// 注册 路线规划 监听
		// youhuijuan_layout = findViewById(R.id.youhuijuan_layout);
		youhuijuan_txt = (TextView) findViewById(R.id.youhuijuan_txt);
		chepaihao_tv = (TextView) findViewById(R.id.chupaihao_tv);
		// yue_txt = (TextView) findViewById(R.id.yue_txt);
		// zhifu_text = (TextView) findViewById(R.id.zhifu_text);
		listView = (ListView) findViewById(R.id.youhuijuan_list);
		title = (TextView) findViewById(R.id.title);
		backLayout = (LinearLayout) findViewById(R.id.back_layout);
		settings = getSharedPreferences("setting", 0);
		Intent intent = getIntent();
		driverid = intent.getStringExtra("driverid");
		companyid = intent.getStringExtra("companyid");
		titleFlag = intent.getStringExtra("titleFlag");
		showname = intent.getStringExtra("showname");
		servicename = settings.getString("selectfuwulx", "");
		typename = settings.getString("selectfuwutype", "");
		phoneString = settings.getString("sp_phone", "");
		passwordString = settings.getString("sp_password", "");
		kehuleixing = settings.getString("kehuleixing", "");
		getTimes();
		initView();

		if ("one".equals(titleFlag)) {
			// title.setText("一键下单");
			title.setText(typename);
			// timeLayout.setVisibility(View.GONE);
			countLayout.setVisibility(View.GONE);
		} else if ("two".equals(titleFlag)) {
			title.setText(typename);
		} else {
			title.setText(typename);
			// timeLayout.setVisibility(View.GONE);
			countLayout.setVisibility(View.GONE);
			nameLayout.setVisibility(View.VISIBLE);
		}

		// 返回
		backLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if ("one".equals(titleFlag) || "two".equals(titleFlag)) {

					finish();
				} else {
					finish();
				}

			}
		});
		getYouhuijuan();
		// 车牌号
		chepai_layout = findViewById(R.id.chepai_layout);
		if (!TextUtils.isEmpty(kehuleixing)) {
			if (kehuleixing.equals("会员")) {
				return;
			} else {
				chepai_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(CallDrvierForOtherActivity.this,
								CheLiangguanliListActivity.class);
						intent.putExtra("from", "order");
						startActivity(intent);
					}
				});
			}
		}
		
	}

	/**
	 * 获取时间
	 */
	@SuppressWarnings("static-access")
	@SuppressLint("SimpleDateFormat")
	public void getTimes() {
		// 获取当前小时
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		Date curdate = new Date(System.currentTimeMillis());
		currenttime = Integer.parseInt(sdf.format(curdate));

		// 获取当前日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		currentdate = formatter.format(curdate);
		// 获取明天日期
		Date date1 = new Date();// 取时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date1 = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		tomoorwdate = formatter.format(date1);

		calendar.add(calendar.DATE, 1);
		date1 = calendar.getTime();
		afterdate = formatter.format(date1);

		// 获取当前分钟
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		currentmin = formatter1.format(curdate);
	}

	/**
	 * 优惠券
	 */
	private void getYouhuijuan() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();// 携带数据（功能强大，可以携带文件，流等）

		String userid = settings.getString("userid", "");
		ap.put("userid", userid);
		ap.put("act", "postok");

		String baseurl = settings.getString("baseurl", "http://sxah.k76.net");
		// finalhttp的post请求
		fp.post(baseurl + "/api/userinfoclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						final YouhuiJuanInfo info = gson.fromJson(t.toString(),
								YouhuiJuanInfo.class);
						String regEx = "[^0-9]";
						Pattern p = Pattern.compile(regEx);
						Matcher m = p.matcher(typeTv.getText().toString());
						price = m.replaceAll("").trim();
						if (TextUtils.isEmpty(price)) {
							price = "0";
						}
						Log.d("***oder****", price);
						if (price != null) {
							// orderallmoney = Integer.parseInt(price);
							int yue = Integer.parseInt(price) - youhuijuanJin;
							if (yue > 0) {
								needpay = yue;
								// zhifu_text.setText("余额支付:" + yue + "元");
							} else {
								needpay = 0;
								// zhifu_text.setText("余额支付: 0 元");
							}
						}

						String inyue = info.getYue();
						if (TextUtils.isEmpty(inyue)) {
							inyue = "0";
						}
						useryue = Integer.parseInt(inyue);

						// yue_txt.setText("账户余额:" + info.getYue() + "元");
						youhuijuanadapter = new YouhuijuanAdapter(
								CallDrvierForOtherActivity.this, info
										.getYouhuiquan());
						listView.setAdapter(youhuijuanadapter);
						if (info.getYouhuiquan() != null) {
							youhuijuanadapter.setSeclection(0);
							youhuijuanadapter.notifyDataSetChanged();
							youhuiquanid = info.getYouhuiquan().get(0)
									.getYouhuiquanid();
							// 优惠券对应的更新name+price
							youhuijuan_txt.setText(info.getYouhuiquan().get(0)
									.getName()
									+ info.getYouhuiquan().get(0).getJine());

							String youjine = info.getYouhuiquan().get(0)
									.getJine();
							if (TextUtils.isEmpty(youjine)) {
								youjine = "0";
							}
							youhuijuanJin = Integer.parseInt(youjine);

						} else {
							youhuijuan_txt.setText("无");
							LinearLayout youhuijuan_layout = (LinearLayout) findViewById(R.id.youhuijuan_layout);
							youhuijuan_layout.setVisibility(View.GONE);
						}
						// listview点击事件
						listView.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								youhuijuanadapter.setSeclection(position);// 确定点击的位置
								youhuijuanadapter.notifyDataSetChanged();// 更新数据
								// 优惠券
								youhuiquanid = info.getYouhuiquan()
										.get(position).getYouhuiquanid();
								youhuijuan_txt.setText(info.getYouhuiquan()
										.get(position).getName()
										+ info.getYouhuiquan().get(position)
												.getJine());

								String youjine1 = info.getYouhuiquan()
										.get(position).getJine();
								if (TextUtils.isEmpty(youjine1)) {
									youjine1 = "0";
								}
								youhuijuanJin = Integer.parseInt(youjine1);

								System.out.println(price + "" + youhuijuanJin);
								if (price != null) {
									int yue = Integer.parseInt(price)
											- youhuijuanJin;
									if (yue > 0) {
										needpay = yue;
										// zhifu_text.setText("余额支付:" + yue +
										// "元");
									} else {
										needpay = 0;
										// zhifu_text.setText("余额支付:" + 0 +
										// "元");
									}
								}
							}
						});

						youhuijuanadapter.notifyDataSetChanged();
						yuZhiFu();
					}
				});
	}

	public void yuZhiFu() {
		// 预支付显示设置
		askPrice();
		int uerneedpay = 0;
		if (orderallmoney - youhuijuanJin > 0) {
			uerneedpay = orderallmoney - youhuijuanJin;
		} else {
			uerneedpay = 0;
		}
		payall_tv.setText(orderallmoney + "元");
		youhui_tv.setText(youhuijuanJin + "元");
		payneed_tv.setText(uerneedpay + "元");
		haoyue_tv.setText(useryue + "元");
		// 如果优惠金额为0 就不显示“请充值0元”
		if (youhuijuanJin == 0) {
			qing.setVisibility(View.INVISIBLE);
			chong_tv.setVisibility(View.INVISIBLE);
		}

		if (uerneedpay - useryue > 0) {
			chongmoney = uerneedpay - useryue;
		} else {
			chongmoney = 0;
		}
		chong_tv.setText(chongmoney + "元");
		if (ifxiadancongzi.equals("yes")) {
			if (chongmoney == 0) {
				callOrderBtn.setText("价格无误，确认付款");
			} else {
				callOrderBtn.setText("余额不足，请充值");
			}

		} else {
			callOrderBtn.setText("立即下单");
		}

	}

	/*
	 * @Override public void onNewIntent(Intent intent) {
	 * super.onNewIntent(intent); String flag = intent.getStringExtra("flag");
	 * String content = intent.getStringExtra("content"); if
	 * ("chufa".equals(flag)) { Log.d(TAG, "get a address:" + content);
	 * chufadiTv.setText(content); } }
	 */

	private void initView() {

		subBtn = (Button) findViewById(R.id.btn_sub);
		subBtn.setOnClickListener(this);
		addBtn = (Button) findViewById(R.id.btn_add);
		addBtn.setOnClickListener(this);
		edtNum = (EditText) findViewById(R.id.edt_num);
		callOrderBtn = (Button) findViewById(R.id.btn_call_order);
		callOrderBtn.setOnClickListener(this);
		juli_layout = (LinearLayout) findViewById(R.id.juli_layout);// 公里数

		String address = settings.getString("address", "");
		String mudidijizhu = settings.getString("mudi", "");
		String beizhujizhu = settings.getString("beizhu", "");
		// chepai=settings.getString("chepai", "");
		// chexing=settings.getString("chexing", "");
		// chepaihao_tv.setText(chexing + "  " + chepai);
		ifMudidi = settings.getString("ifshowmudidi", "no");
		ifshowdrivernum = settings.getString("ifshowdrivernum", "no");
		ifshowcheliang = settings.getString("ifshowcheliang", "no");
		ifcanyuyue = settings.getString("ifcanyuyue", "no");
		yuyuebegintime = settings.getString("yuyuebegintime", "8");// 预约开始时间
		yuyueendtime = settings.getString("yuyueendtime", "19");// 预约结束时间

		if (TextUtils.isEmpty(yuyuebegintime)) {
			yuyuebegintime = "0";
		}
		if (TextUtils.isEmpty(yuyueendtime)) {
			yuyueendtime = "24";
		}
		yuyueneedtime = settings.getString("yuyueneedtime", "15");
		Log.e("MUSIC", "yuyueneedtime=" + yuyueneedtime);
		ifdriverull = settings.getString("ifdriverull", "no");
		if ("no".equals(ifshowcheliang)) {
			chepai_layout.setVisibility(View.GONE);
		}

		ifxiadancongzi = settings.getString("ifxiadancongzi", "no");
		ifnodrivernodan = settings.getString("ifnodrivernodan", "no");
		setPriceString = new HashSet<String>();
		setPriceString = settings.getStringSet("fuwuxiangmuprice",
				setPriceString);

		TextView staradress = (TextView) findViewById(R.id.startadress_tv);
		if (servicename.contains("洗车")) {
			staradress.setText("洗车地点");
		} else {
			staradress.setText("出发地点");
		}
		chufadiTv = (TextView) findViewById(R.id.chufadi_tv);
		chufadiTv.setText(address);
		mudidiTv = (TextView) findViewById(R.id.mudidi_tv);
		mudidiTv.setText(mudidijizhu);
		typeTv = (TextView) findViewById(R.id.type_tv);// 服务类型
		typeTv.setText(typename);
		fuwushijianTv = (TextView) findViewById(R.id.fuwushijian_tv);

		remarkTv = (TextView) findViewById(R.id.remark_tv);// 备注
		remarkTv.setText(beizhujizhu);

		countLayout = (LinearLayout) findViewById(R.id.layout_driverother_personcount);
		if (ifshowdrivernum.equals("no")) {
			countLayout.setVisibility(View.GONE);
		}

		nameLayout = (LinearLayout) findViewById(R.id.layout_driver_name);
		TextView showNameText = (TextView) findViewById(R.id.show_name);
		if (!TextUtils.isEmpty(showname)) {
			showNameText.setText(showname);
		}

		// 出发地
		LinearLayout chufaLayout = (LinearLayout) findViewById(R.id.chufa_layout);
		chufaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// sent("chufa",chufadiTv.getText().toString());
				Intent intent = new Intent(CallDrvierForOtherActivity.this,
						SelectMudiAddressActivity.class);
				intent.putExtra("select_start", 99);// 在选择地址时，根据此参数决定要不要保存经纬度
				select_start = 99;
				startActivityForResult(intent, 0);
			}

		});

		// 目的地
		LinearLayout mudiLayout = (LinearLayout) findViewById(R.id.mudi_layout);
		mudiLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// sent("mudi", mudidiTv.getText().toString());
				Intent intent = new Intent(CallDrvierForOtherActivity.this,
						SelectMudiAddressActivity.class);
				intent.putExtra("select_end", 88);// 在选择地址时，根据此参数决定要不要保存经纬度
				startActivityForResult(intent, 1);

			}

		});

		timeLayout = (LinearLayout) findViewById(R.id.time_layout);
		if ("yes".equals(ifcanyuyue)) {
			timeLayout.setVisibility(View.VISIBLE);
		} else {
			timeLayout.setVisibility(View.GONE);
		}
		timetxt = (TextView) findViewById(R.id.time_txt);

		defaulttime = Integer.parseInt(yuyuebegintime) + ":00-"
				+ (Integer.parseInt(yuyuebegintime) + 1) + ":00";
		if (!TextUtils.isEmpty(defaulttime)) {
			timetxt.setText(tomoorwdate + "  " + defaulttime);
		}
		timeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showtimepop(v);
			}
		});

		// 如果显示目的地则显示公里数
		if ("yes".equals(ifMudidi)) {
			mudiLayout.setVisibility(View.VISIBLE);
			juli_layout.setVisibility(View.VISIBLE);
		} else {
			mudiLayout.setVisibility(View.GONE);
			juli_layout.setVisibility(View.GONE);
		}

		// 备注
		LinearLayout remarkLayout = (LinearLayout) findViewById(R.id.remark_layout);
		remarkLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// sent("remark", remarkTv.getText().toString());
				Intent intent = new Intent(CallDrvierForOtherActivity.this,
						EditAddressActivity.class);
				startActivityForResult(intent, 2);
			}

		});

		// LinearLayout typeLayout = (LinearLayout)
		// findViewById(R.id.type_layout);

		/*
		 * showType = settings.getString("ifshowfuwuxiangmu", "no"); if
		 * ("yes".equals(showType)) { typeLayout.setVisibility(View.VISIBLE);
		 * 
		 * } else { typeLayout.setVisibility(View.GONE);
		 * 
		 * }
		 */

		String fuwuxiangmu1 = settings.getString("fuwuxiangmu1", "1111,2222");
		Log.e("MUSIC", "fuwuxiangmu1=" + fuwuxiangmu1);
		fuwuxiangmu = fuwuxiangmu1.split(",");
		// typeTv.setText(fuwuxiangmu[0]);
		fuwuxiangmuid = "0";

		LinearLayout fuwushijianLayout = (LinearLayout) findViewById(R.id.fuwushijian_layout);
		showType = settings.getString("ifshowfuwushijian", "no");
		if ("yes".equals(showType)) {
			fuwushijianLayout.setVisibility(View.VISIBLE);
		}

		String fuwushijian1 = settings.getString("fuwushijian1", "3333,4444");
		Log.e("MUSIC", "fuwushijian1=" + fuwushijian1);
		fuwushijian = fuwushijian1.split(",");
		fuwushijianTv.setText(fuwushijian[0]);

		/*
		 * typeLayout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * new AlertDialog.Builder(CallDrvierForOtherActivity.this)
		 * .setTitle("服务类型") .setItems(fuwuxiangmu, new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) { for
		 * (int i = 0; i < fuwuxiangmu.length; i++) { Log.e("MUSIC",
		 * "fuwuxiangmu=" + fuwuxiangmu[i]); if (arg1 == i) { fuwuxiangmuid =
		 * String .valueOf(arg1); Log.e("fuwuxiangmuid++=", fuwuxiangmuid);
		 * typeTv.setText(fuwuxiangmu[i]); type = Integer.toString(i); money =
		 * "20"; String regEx = "[^0-9]"; Pattern p = Pattern .compile(regEx);
		 * Matcher m = p.matcher(typeTv .getText().toString()); price =
		 * m.replaceAll("").trim(); // orderallmoney = Integer //
		 * .parseInt(price);// // 把服务项目的金额赋给总金额 // 预支付显示设
		 * 
		 * yuZhiFu(); System.out.println(m .replaceAll("").trim()); if (price !=
		 * null) { int yue = Integer .parseInt(price) - youhuijuanJin; if (yue >
		 * 0) { needpay = yue; } else { needpay = yue;// 应该是零吧 } } }
		 * 
		 * }
		 * 
		 * /* if (arg1 == 0) { typeTv.setText("普通洗车 20元"); type = "1"; money =
		 * "20"; }else if(arg1 == 1){ typeTv.setText("精品洗车 40元"); type = "2";
		 * money = "40"; }else if(arg1 == 2){ typeTv.setText("女子洗车 60元"); type =
		 * "3"; money = "60"; }
		 * 
		 * } }).show(); } });
		 */

		// 设置服务时间
		fuwushijianLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new AlertDialog.Builder(CallDrvierForOtherActivity.this)
						.setTitle("服务时间")
						.setItems(fuwushijian,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

										for (int i = 0; i < fuwushijian.length; i++) {
											// Log.e("MUSIC", "fuwushijian=" +
											// fuwuxiangmu[i]);
											if (arg1 == i) {
												fuwushijianTv
														.setText(fuwushijian[i]);
												type = Integer.toString(i);
												money = "20";
											}
										}

									}
								}).show();
			}
		});
		// 初始化时间
		Date date = new Date();
		date.setTime(date.getTime() + 15 * 60 * 1000);

		// 预支付
		LinearLayout prepay_layout = (LinearLayout) findViewById(R.id.prepay_layout);
		if (ifxiadancongzi.equals("yes")) {
			prepay_layout.setVisibility(View.VISIBLE);
		} else {
			prepay_layout.setVisibility(View.GONE);
		}

		payall_tv = (TextView) findViewById(R.id.payall_tv);
		youhui_tv = (TextView) findViewById(R.id.youhui_tv);
		payneed_tv = (TextView) findViewById(R.id.payneed_tv);
		haoyue_tv = (TextView) findViewById(R.id.yue_tv);
		chong_tv = (TextView) findViewById(R.id.chong_tv);
		qing = (TextView) findViewById(R.id.qing);

		layout_accountpay = (RelativeLayout) findViewById(R.id.clayout_accountpay);
		layout_carcapay = (RelativeLayout) findViewById(R.id.clayout_carcapay);
		accountpay_checkbox = (CheckBox) findViewById(R.id.accountpay_checkbox);
		carcapay_checkbox = (CheckBox) findViewById(R.id.carcapay_checkbox);
		accountpay_checkbox.setChecked(true);
		kanum_ed = (EditText) findViewById(R.id.kanum_edit);
		pwd_ed = (EditText) findViewById(R.id.password_edit);

		layout_accountpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (carcapay_checkbox.isChecked()) {
					carcapay_checkbox.setChecked(false);
					accountpay_checkbox.setChecked(true);
					zhifuType = "accountpay";
				}
			}
		});
		layout_carcapay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (accountpay_checkbox.isChecked()) {
					accountpay_checkbox.setChecked(false);
					carcapay_checkbox.setChecked(true);
					zhifuType = "carcapay";
				}
			}
		});
		carcapay_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							accountpay_checkbox.setChecked(false);
							zhifuType = "carcapay";
							callOrderBtn.setText("核对无误，确认下单");
						}
					}

				});
		accountpay_checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							carcapay_checkbox.setChecked(false);
							zhifuType = "accountpay";
							if (chongmoney > 0) {
								callOrderBtn.setText("余额不足，请充值");
							} else {
								callOrderBtn.setText("价格无误，确认付款");
							}
						}

					}
				});

	}

	// 显示时间pop
	@SuppressLint("SimpleDateFormat")
	public void showtimepop(View v) {
		// TODO Auto-generated method stub

		View view = getLayoutInflater().inflate(R.layout.time_wheelview, null);
		@SuppressWarnings("deprecation")
		final PopupWindow pw = new PopupWindow(view,
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);
		pw.setTouchable(true);
		pw.showAtLocation(v, Gravity.BOTTOM, 0, 0);

		// 设置今天的预约时间
		yuYue = "立即服务(" + yuyueneedtime + "分钟)";
		if (currenttime < Integer.parseInt(yuyueendtime)) {
			// 当前时间小于服务开始时间
			if (currenttime < Integer.parseInt(yuyuebegintime)) {
				int length = Integer.parseInt(yuyueendtime)
						- Integer.parseInt(yuyuebegintime);
				for (int i = 0; i < length; i++) {
					times1[i] = "今天  " + currentdate + " "
							+ (Integer.parseInt(yuyuebegintime) + i) + ":00-"
							+ (Integer.parseInt(yuyuebegintime) + i + 1)
							+ ":00";
					timeslist.add(times1[i]);
				}
			} else {
				// 当前时间小于服务结束时间大于服务开始时间
				int length = Integer.parseInt(yuyueendtime) - currenttime;
				if (ifnodrivernodan.equals("yes") && ifdriverull.equals("yes")) {
					for (int i = 0; i < length; i++) {
						times1[i] = "今天  " + currentdate + " "
								+ (currenttime + i) + ":00-"
								+ (currenttime + i + 1) + ":00";
						timeslist.add(times1[i]);
					}
				} else {
					times1[0] = yuYue;
					timeslist.add(yuYue);
					for (int i = 1; i < length; i++) {
						times1[i] = "今天  " + currentdate + " "
								+ (currenttime + i) + ":00-"
								+ (currenttime + i + 1) + ":00";
						timeslist.add(times1[i]);
					}
				}
			}
		} else {
			times1[0] = "已过服务时间!";
			timeslist.add("已过服务时间!");
		}
		// 设置明天的预约时间
		if (Integer.parseInt(yuyuebegintime) < Integer.parseInt(yuyueendtime)) {
			int llength = Integer.parseInt(yuyueendtime)
					- Integer.parseInt(yuyuebegintime);
			for (int i = 0; i < llength; i++) {
				times2[i] = "明天  " + tomoorwdate + " "
						+ (Integer.parseInt(yuyuebegintime) + i) + ":00-"
						+ (Integer.parseInt(yuyuebegintime) + i + 1) + ":00";
				timeslist.add(times2[i]);
			}
		}
		// 设置后天的预约时间
		if (Integer.parseInt(yuyuebegintime) < Integer.parseInt(yuyueendtime)) {
			int llength = Integer.parseInt(yuyueendtime)
					- Integer.parseInt(yuyuebegintime);
			for (int i = 0; i < llength; i++) {
				times3[i] = "后天  " + afterdate + " "
						+ (Integer.parseInt(yuyuebegintime) + i) + ":00-"
						+ (Integer.parseInt(yuyuebegintime) + i + 1) + ":00";
				timeslist.add(times3[i]);
			}
		}

		// 设置WheelView
		WheelView wv = (WheelView) view.findViewById(R.id.time_wheel);
		wv.setOffset(4);
		// wv.setItems(Arrays.asList(times));
		wv.setItems(timeslist);
		wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				selectime = item;
			}
		});

		Button confirm_bt = (Button) view.findViewById(R.id.time_btn_confirm);
		Button cancel_bt = (Button) view.findViewById(R.id.time_btn_cancel);
		confirm_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(selectime)) {

					if (selectime.contains("已过服务时间")) {
						timetxt.setText("已过服务时间");
					} else if (selectime.contains("立即服务")) {
						timetxt.setText(yuYue);
					} else {
						timetxt.setText(selectime);
						int aa = selectime.indexOf("  ");
						selectime = selectime.substring(aa);// 2016-3-3
															// 8:00-9:00
						Log.e("没日期selectime====", selectime);
						int a = selectime.indexOf(":");
						String ba = selectime.substring(0, a);
						yuyuebegin = ba + ":00";// 2016-3-3 8:00
						Log.e("没日期yuyuebegin====", yuyuebegin);
						int c1 = selectime.indexOf("  ");
						int c2 = selectime.lastIndexOf(" ");
						String sdate = selectime.substring(c1 + 1, c2);
						selectime = selectime.substring(c2);
						int b1 = selectime.indexOf("-");
						int b2 = selectime.lastIndexOf(":");
						String jiu = selectime.substring(b1 + 1, b2);
						yuyuechufashijian = sdate + "  " + jiu + ":00";// 2016-3-3
																		// 9:00
						Log.e("没日期yuyuechufashijian====", yuyuechufashijian);
					}
					askPrice();// 询价
				}
				pw.dismiss();

			}
		});
		cancel_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pw.dismiss();
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sub:
			num--;
			addBtn.setEnabled(true);
			if (num <= 1) {
				subBtn.setEnabled(false);
			}
			edtNum.setText(String.valueOf(num));
			break;

		case R.id.btn_add:
			num++;
			subBtn.setEnabled(true);
			if (num >= 4) {
				addBtn.setEnabled(false);
			}
			edtNum.setText(String.valueOf(num));
			break;

		case R.id.btn_call_order:
			/*
			 * mConfirmOrderView.setVisibility(View.VISIBLE); mTime.start();
			 */
			if (from == null) {
				Log.e("MUSIC1", "userBeanuserBeanuserBeanuserBeanuserBean");
			} else {
				Log.e("MUSIC1", "fromfromfromfromfrom");
			}
			toCurrentOrder();
			break;

		default:
			break;
		}
	}

	private void toCurrentOrder() {

		if ("yes".equals(ifMudidi)) {
			if (TextUtils.isEmpty(mudidiTv.getText().toString().trim())) {
				Toast.makeText(CallDrvierForOtherActivity.this, "请选择目的地",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if ("yes".equals(ifshowcheliang)) {
			if (TextUtils.isEmpty(chepaihao_tv.getText().toString().trim())) {
				Toast.makeText(CallDrvierForOtherActivity.this, "请填写车牌号和车型",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if (TextUtils.isEmpty(chufadiTv.getText().toString().trim())) {
			Toast.makeText(CallDrvierForOtherActivity.this, "请选择出发地",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("MUSIC", "ifxiadancongzi=" + ifxiadancongzi + " useryue="
				+ useryue + " needpay=" + needpay);
		// ifnodrivernodan从接口获取，在

		if (ifnodrivernodan.equals("yes") && ifdriverull.equals("yes")) {
			Toast.makeText(CallDrvierForOtherActivity.this,
					"非常抱歉，在您附近没有空闲司机，暂时不能下单！", Toast.LENGTH_SHORT).show();
		} else if (timetxt.getText().toString().trim().contains("已过服务时间")) {
			Toast.makeText(CallDrvierForOtherActivity.this, "已过服务时间！",
					Toast.LENGTH_SHORT).show();
		} else if (ifxiadancongzi.equals("yes")) {
			// 预支付
			if (zhifuType.equals("accountpay")) {
				if (chongmoney > 0) {
					callOrderBtn.setText("余额不足，请充值");
					Toast.makeText(CallDrvierForOtherActivity.this, "余额不足请充值！",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CallDrvierForOtherActivity.this,
							WXPayEntryActivity.class);
					startActivityForResult(intent, 0);
					return;
				} else {
					callOrderBtn.setText("价格无误，确认付款");
					submitOrder();
				}

			} else if (zhifuType.equals("carcapay")) {
				if (!TextUtils.isEmpty(kanum_ed.getText().toString().trim())
						&& !TextUtils.isEmpty(pwd_ed.getText().toString()
								.trim())) {
					callOrderBtn.setText("核对无误，确认下单");
					submitOrder();

				} else {
					callOrderBtn.setText("请输入卡号和密码");
					Toast.makeText(CallDrvierForOtherActivity.this, "请输入卡号和密码",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			submitOrder();
		}

	}

	private void submitOrder() {
		settings = getSharedPreferences("setting", 0);
		String userid = settings.getString("userid", "");

		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		x = settings.getString("x", "");// 121.489541
		y = settings.getString("y", "");// 31.296934

		// 出发地坐标
		params.put("x", x);
		params.put("y", y);

		// 目的地坐标
		x2 = settings.getString("x2", "");// 121.56004
		y2 = settings.getString("y2", "");// 31.224836
		params.put("endx", x2);
		params.put("endy", y2);
		// 公里数
		if (strJuLi != null) {
			params.put("gujigonglishu", strJuLi);
		}
		if (driverid != null) {
			params.put("driverid", driverid);
		} else {
			params.put("driverid", "");
		}
		if (companyid != null) {
			params.put("companyid", companyid);
		} else {
			params.put("companyid", "");
		}
		if ("yes".equals(ifcanyuyue)) {
			params.put("yuyuebegin", yuyuebegin);
			params.put("yuyuechufashijian", yuyuechufashijian);
		}
		// params.put("yuyuechufashijian", time.getText().toString());
		params.put("chufadidian", chufadiTv.getText().toString());
		params.put("mudidi", mudidiTv.getText().toString());
		params.put("daijialeixing", type);

		// 服务项目名称

		params.put("fuwuxiangmu", typename);

		params.put("jine", payall_tv.getText().toString());
		params.put("yingfujine", payneed_tv.getText().toString());
		// params.put("yonghutel", phone.getText().toString().trim());
		// String phone = settings.getString("phone", "");

		// 电话必填
		if (TextUtils.isEmpty(phoneString)) {
			Toast.makeText(CallDrvierForOtherActivity.this, "电话不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		params.put("yonghutel", phoneString);
		if (ifshowdrivernum.equals("yes")) {
			params.put("renshu", edtNum.getText().toString().trim());
		} else {
			params.put("renshu", "1");
		}

		params.put("beizhu", remarkTv.getText().toString().trim());
		params.put("youhuiquanid", youhuiquanid);
		params.put("act", "postok");
		if (ifshowcheliang.equals("yes")) {
			/*
			 * if (userBean != null) { params.put("chepaihao",
			 * userBean.getChepaihao()); params.put("chexing",
			 * userBean.getChexing()); } else {
			 */
			params.put("chepaihao", chepai);
			params.put("chexing", chexing);

		}
		if (zhifuType.equals("carcapay")) {
			params.put("fuwukahao", kanum_ed.getText().toString());
			params.put("fuwukamima", pwd_ed.getText().toString());
		}

		String baseurl = settings.getString("baseurl", "http://sxah.k76.net");

		fp.post(baseurl + "/api/kuaisuxiadanapi.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						DriveBean bean = gson.fromJson(t.toString(),
								DriveBean.class);
						if ("success".equals(bean.getRet())) {
							Log.d("order", t.toString());

							Intent intent = new Intent(
									CallDrvierForOtherActivity.this,
									PaiDanActivity.class);
							intent.putExtra("Dingdanid", bean.getDingdanid());
							intent.putExtra("money", money);
							intent.putExtra("from", "order");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
							startActivity(intent);
							finish();

						} else if ("error:allready have dingdan".equals(bean
								.getRet())) {
							Toast.makeText(CallDrvierForOtherActivity.this,
									"不能重复下单", Toast.LENGTH_SHORT).show();
						} else if ("error".equals(bean.getRet())) {
							Toast.makeText(CallDrvierForOtherActivity.this,
									bean.getMsg(), Toast.LENGTH_SHORT).show();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(CallDrvierForOtherActivity.this,
								"下单失败，请稍后再试", Toast.LENGTH_SHORT).show();
					}

				});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		settings = getSharedPreferences("setting", 0);

		chufa = chufadiTv.getText().toString();
		mudi = mudidiTv.getText().toString();
		// 当目的地选择之后执行该方法
		if ("yes".equals(ifMudidi)) {
			if ((!TextUtils.isEmpty(chufa)) && (!TextUtils.isEmpty(mudi))) {
				x2 = settings.getString("x2", "");// 121.56004
				y2 = settings.getString("y2", "");// 31.224836
				x = settings.getString("x", "");// 121.489541
				y = settings.getString("y", "");// 31.296934

				// String转double
				lonS = Double.parseDouble(x);
				latS = Double.parseDouble(y);
				lonE = Double.parseDouble(x2);
				latE = Double.parseDouble(y2);

				LatLng ptStart = new LatLng(latS, lonS);// 出发地
				LatLng ptEnd = new LatLng(latE, lonE);// 目的地
				PlanNode stNode = PlanNode.withLocation(ptStart);
				PlanNode enNode = PlanNode.withLocation(ptEnd);

				DrivingRoutePlanOption drpo = new DrivingRoutePlanOption();
				// 设置出发点，路线策略：距离最短，目的地
				drpo.from(stNode).policy(DrivingPolicy.ECAR_DIS_FIRST)
						.to(enNode);
				mSearch.drivingSearch(drpo);// 发起请求
			}
		}

		if (from == null) {
			getInfo();
		}
		if (chongmoney > 0) {
			getYouhuijuan();
		}

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(finish);
		super.onDestroy();
	}

	private void askPrice() {
		FinalHttp fp = new FinalHttp();
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		AjaxParams params = new AjaxParams();
		String userId = settings.getString("userid", "");
		params.put("userid", userId);
		if (driverid != null) {
			params.put("driverid", driverid);
		} else {
			params.put("driverid", "");
		}
		params.put("gonglishu", strJuLi);
		params.put("fuwuxiangmu", fuwuxiangmuid);
		params.put("fuwumingcheng", typename);
		String xselectime = timetxt.getText().toString().trim();// 读取预约时间
		String chufashijian;// 询价时传的出发时间
		if (xselectime.contains("立即服务")) {
			// 如果是“立即服务(30分钟)”，取当前日期+当前时间+分钟
			chufashijian = currentmin;
		} else {
			// 如果是时间段，取当前日期+时间段的开始时间
			int a = xselectime.indexOf(":");
			if (a == -1)
				return; // selectime = 已过服务时间 a=-1 ;下一行会导致闪退

			String ba = xselectime.substring(0, a);
			chufashijian = ba + ":00";
		}
		params.put("chufashijian", chufashijian);
		
		params.put("act", "postok");
		fp.post(baseurl + "/api/askjiage.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						Gson gson = new Gson();
						userBean = gson.fromJson(t.toString(), UserBean.class);

						if (userBean != null) {
							String xunJiaPrice = userBean.getJine();
							if (TextUtils.isEmpty(xunJiaPrice)) {
								xunJiaPrice = "0";
							}
							orderallmoney = Integer.parseInt(xunJiaPrice);
							payall_tv.setText(orderallmoney + "元");
							yuZhiFu();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
						Log.e("aaaa", t.toString() + "-------" + errorNo
								+ "-------" + strMsg);
					}
				});
	}

	private void getInfo() {

		String baseurl = settings.getString("baseurl", "http://sxah.k76.net");
		String dengluhao = settings.getString("dengluhao", "");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("lmdengluhao", dengluhao);
		params.put("telphone", phoneString);
		params.put("password", passwordString);
		params.put("act", "postok");
		Log.e("MUSIC", "LOGINAcount--baseurl=" + baseurl + " dengluhao="
				+ dengluhao);
		fp.post(baseurl + "/api/applogin.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						Gson gson = new Gson();
						Log.v("aaaa", t.toString());
						userBean = gson.fromJson(t.toString(), UserBean.class);
						if (userBean != null) {
							chepaihao_tv.setText(userBean.getChexing() + "  "
									+ userBean.getChepaihao());
							chepai = userBean.getChepaihao();
							chexing = userBean.getChexing();
							if (TextUtils.isEmpty(kehuleixing)) {
								kehuleixing = userBean.getKehuleixing();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "22222222222222222");
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK == resultCode) {
			/*
			 * String flag = data.getStringExtra("flag"); String content =
			 * data.getStringExtra("content");
			 */
			SharedPreferences sp = getSharedPreferences("setting",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			String address = data.getStringExtra("address");// 从选择地址页面返回的地址
			String content = data.getStringExtra("content");// 从编辑页面返回的备注
			editor.putString("beizhu", content);
			if (0 == requestCode) {
				chufadiTv.setText(address);
			}
			if (1 == requestCode) {
				mudidiTv.setText(address);
				editor.putString("mudi", address);
			}
			if (2 == requestCode) {
				remarkTv.setText(content);
			}
			editor.commit();

		}

		/*
		 * if ("chufa".equals(flag)) { Log.d(TAG, "get a address:" + content);
		 * chufadiTv.setText(content); } else if ("mudi".equals(flag)) {
		 * mudidiTv.setText(content); } else if ("remark".equals(flag)) {
		 * remarkTv.setText(content); }
		 */

	}

	/*
	 * public void showDistance(){ // 构建 导航参数 // 出发地坐标 double mlatStart =
	 * Double.parseDouble(latStart); double mlonStart =
	 * Double.parseDouble(lonStart); // 目的地坐标 double mlatEnd =
	 * Double.parseDouble(latEnd); double mlonEnd = Double.parseDouble(lonEnd);
	 * 
	 * LatLng ptStart= new LatLng(mlatStart, mlonStart); LatLng ptEnd = new
	 * LatLng(mlatEnd, mlonEnd); PlanNode stNode =
	 * PlanNode.withCityNameAndPlaceName("北京", chufadiTv.getText().toString());
	 * PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京",
	 * mudidiTv.getText().toString()); mSearch.drivingSearch((new
	 * DrivingRoutePlanOption()) .from(stNode) .to(enNode));
	 * 
	 * 
	 * }
	 */

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult resultDriving) {
		if (resultDriving == null
				|| resultDriving.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(CallDrvierForOtherActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (resultDriving.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// resultDriving.getSuggestAddrInfo()
			return;
		}
		if (resultDriving.error == SearchResult.ERRORNO.NO_ERROR) {
			int juLi = resultDriving.getRouteLines().get(0).getDistance();
			Float fJuLi = (float) (juLi / 1000.0);
			// 构造方法的字符格式这里如果小数不足2位,会以0补足.
			DecimalFormat decimalFormat = new DecimalFormat("0.0");
			strJuLi = decimalFormat.format(fJuLi);// format 返回的是字符串
			juli_tv.setText(strJuLi + "公里");
			if (!TextUtils.isEmpty(strJuLi)) {
				askPrice();// 询价
			}

		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult resultTransit) {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult resultWalking) {

	}
}
