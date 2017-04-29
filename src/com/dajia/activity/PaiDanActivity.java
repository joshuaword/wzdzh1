package com.dajia.activity;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dajia.Bean.CurrentOrderBean;
import com.dajia.view.AbHorizontalProgressBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 派单中
 * @author Administrator
 *
 */
public class PaiDanActivity extends BaseActivity {
	private TextView txt_cancle, title, txt_time, txt_address, txt_phone;
	private View back_layout;
	String userid;
	private String dingdanid,servicename;
	private AbHorizontalProgressBar progressbar;
	SharedPreferences settings;
	private String baseurl;
	private int i = 70;
	private int TIME = 10000;
	private int JISHI = 1000;
	EditText edt;
	private int progress = 70;
	private TextView second;
	private static DisplayImageOptions options;
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				queryNewOrder();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paidan);
		back_layout = findViewById(R.id.back_layout);
		title = (TextView) findViewById(R.id.title);
		//second = (TextView) findViewById(R.id.second);
		progressbar = (AbHorizontalProgressBar) findViewById(R.id.progressbar);
		progressbar.setProgress(100);
		txt_cancle = (TextView) findViewById(R.id.txt_cancle);
		txt_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cancelDialog();
			}
		});
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_phone = (TextView) findViewById(R.id.txt_phone);
		txt_address = (TextView) findViewById(R.id.txt_address);
		dingdanid = getIntent().getStringExtra("Dingdanid");
		title.setText("派单中");
		settings = getSharedPreferences("setting", 0);
		userid = settings.getString("userid", "");
		baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		servicename=settings.getString("selectfuwulx", "");
		back_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView invoice_tv=(TextView) findViewById(R.id.tv_apply_invoice_title);
		if (servicename.contains("上门洗车")) {
			invoice_tv.setText("请稍候，正在为您联系洗车工... ");
		} else if (servicename.contains("到店洗车")) {
			invoice_tv.setText("请稍候，正在为您联系洗车店... ");
		} else if (servicename.contains("代驾")) {
			invoice_tv.setText("请稍候，正在为您联系司机... ");
		} else {
			invoice_tv.setText("请稍候，正在为您联系" + servicename + "员... ");
		}
		
		TextView success_tv=(TextView) findViewById(R.id.success_tv);
		if (servicename.contains("上门洗车")) {
			success_tv.setText("您已预约成功，请耐心等待洗车工与您联系 ");
		} else if (servicename.contains("到店洗车")) {
			success_tv.setText("您已预约成功，请耐心等待洗车店与您联系 ");
		} else if (servicename.contains("代驾")) {
			success_tv.setText("您已预约成功，请耐心等待代驾员与您联系 ");
		} else {
			success_tv.setText("您已预约成功，请耐心等待" + servicename + "员与您联系 ");
		}
		queryNewOrder();
		startAddProgress();
		progressbar
				.setAbOnProgressListener(new AbHorizontalProgressBar.AbOnProgressListener() {
					@Override
					public void onProgress(int progress) {

					}

					@Override
					public void onComplete() {
						progress = 70;
						startAddProgress();
					}
				});
		handler.postDelayed(runnable, TIME); // 每隔10s执行
	//	handlertime.postDelayed(runnabletime, JISHI); // 每隔10s执行
	}

	public void startAddProgress() {
		if (progress == 0) {
			progress = 70;
		}
		progress = progress - 1;
		progressbar.setProgress(progress);
		mUpdateHandler.sendEmptyMessageDelayed(1, 1000);
	}

	private Handler mUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startAddProgress();
				break;
			}
			super.handleMessage(msg);
		}
	};

	//取消弹出框
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
										sendBroadcast(new Intent("cancle"));
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

	@Override
	protected void onResume() {
		super.onResume();
	}
/**
 * 获取订单
 */
	public void queryNewOrder() {
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		Log.e("MUSIC", "userid=" + userid);
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
							if(bean.getIfhavedingdan().equals("no")){
								finish();
							}
							if(bean.getIfquxiao().equals("yes")){
								txt_cancle.setVisibility(View.VISIBLE);
							}else{
								txt_cancle.setVisibility(View.GONE);
							}
							txt_time.setText(bean.getCreatetime());
							txt_address.setText(bean.getChufadidian());
							txt_phone.setText(bean.getShiyongrendianhua());
							Log.d("***getState***", bean.getState());
							if (bean.getState() != null
									&& !bean.getState().equals("")) {
								if (bean.getState().equals("接单")
										|| bean.getState().equals("到达")
										|| bean.getState().equals("出发")) {
									Intent intent = new Intent(
											PaiDanActivity.this,
											DaijiaActivity.class);
									intent.putExtra("Dingdanid", dingdanid);
									System.out
											.println("dingdaniddingdaniddingdaniddingdanid"
													+ dingdanid);
									Log.d("PaiDanActivity",
											"PaiDanActivityPaiDanActivityPaiDanActivity");
									startActivity(intent);
									finish();
								}
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (handler != null) {
			handler.removeCallbacks(runnable);
			handler = null;
		}
		
		/*if (handlertime != null) {
			handlertime.removeCallbacks(runnabletime);
			handlertime = null;
		}*/
		
		if (mUpdateHandler != null) {
			mUpdateHandler.removeMessages(1);
			mUpdateHandler = null;
		}
		super.onDestroy();
	}
}
