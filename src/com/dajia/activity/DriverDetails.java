package com.dajia.activity;

import java.util.ArrayList;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import net.k76.wzd.R;

import com.dajia.Bean.DriveDetailBean;
import com.dajia.Bean.DriverInfo;
import com.dajia.Bean.PinglunBean;
import com.dajia.Bean.UserBean;
import com.dajia.adapter.EvalutateAdapter;
import com.dajia.adapter.StorePicAdapter;
import com.google.gson.Gson;

/**
 * 司机列表
 * 
 * @author Administrator
 * 
 */
public class DriverDetails extends BaseActivity implements OnClickListener {

	private Button btnCallDriver;
	private EvalutateAdapter mAdapter;
	private TextView nearby_sign;// 代驾司机签名
	private TextView nearby_distance;// 距离
	private TextView nearby_name;// 姓名
	private RatingBar nearby_star;// 星级
	private ImageView nearby_confirm;//是否认证
	private ArrayList<PinglunBean> mInfoList = new ArrayList<PinglunBean>();
	// 该车主暂没有评价
	private View mLayoutNoComments;

	private ListView mCommentListView;
	private DriverInfo info;
	private UserBean userBean;

	private TextView title;
	private String companyid;
	private String driverid;
	private String signstr;
	private LinearLayout backLayout;
	String showname;
	private String typename;
	private SharedPreferences settings;
	private GridView driver_imgs;
	private ArrayList<String> imglist;
	private String phoneString,passwordString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_detail);
		info = (DriverInfo) getIntent().getSerializableExtra("info");
		companyid = info.getCompanyid();
		driverid = info.getDriverid();
		signstr = info.getSign();
		Log.e("diridhhhhhh=", companyid);
		Log.e("signhhhhhhh=", signstr);

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

	private void initView() {

		this.nearby_name = ((TextView) findViewById(R.id.nearby_name));
		this.nearby_star = ((RatingBar) findViewById(R.id.nearby_star));
		this.nearby_sign = ((TextView) findViewById(R.id.ny_sign));
		this.nearby_sign.setText(signstr);
		this.nearby_confirm=(ImageView) findViewById(R.id.dirver_confirm);
		this.nearby_distance = ((TextView) findViewById(R.id.nearby_distance));
		this.btnCallDriver = ((Button) findViewById(R.id.image_available));
		this.btnCallDriver.setOnClickListener(this);
		this.mLayoutNoComments = findViewById(R.id.linear_review);
		this.mCommentListView = ((ListView) findViewById(R.id.driver_listView));
		driver_imgs = (GridView) findViewById(R.id.driver_pictures);
		driver_imgs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DriverDetails.this,
						ScaleimgActivity.class);
				intent.putExtra("selectposition", position);
				intent.putStringArrayListExtra("imglist", imglist);
				startActivity(intent);
			}
		});
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
						DriveDetailBean dd = gson.fromJson(t.toString(),
								DriveDetailBean.class);
						if ("success".equals(dd.getRet())) {
							showname = dd.getShowname();
							if (showname.contains("：")) {
								int a = showname.indexOf("：");
								nearby_name.setText(showname.substring(a + 1));
							}
							title.setText(dd.getTitle());
							nearby_star.setRating(Float.valueOf(dd.getXinji()));
							nearby_distance.setText(dd.getShowinfo1());
							if (dd.getConfirm().equals("yes")) {
								nearby_confirm.setBackgroundResource(R.drawable.dirver_confirm);
							}else {
								nearby_confirm.setBackgroundResource(R.drawable.dirver_noconfirm);
							}
							mInfoList = (ArrayList<PinglunBean>) dd
									.getPinglun();
							if (mInfoList.size() > 0 && mInfoList != null) {
								mAdapter = new EvalutateAdapter(
										DriverDetails.this, mInfoList);
								mCommentListView.setAdapter(mAdapter);
								mAdapter.notifyDataSetChanged();
							} else {
								mLayoutNoComments.setVisibility(View.VISIBLE);
							}

							imglist = dd.getHeadImageArray();
							StorePicAdapter picadapter = new StorePicAdapter(
									DriverDetails.this, imglist);
							driver_imgs.setAdapter(picadapter);
							picadapter.notifyDataSetChanged();
						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(DriverDetails.this, "查询司机详情失败，请稍后再试",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	public Boolean yanzhen() {
		String phone = settings.getString("phone", "");
		String yanzhenma = settings.getString("ifyanzhenma", "no");
		if ("yes".equals(yanzhenma)) {
			if (TextUtils.isEmpty(phone)) {
				Intent intent = new Intent(DriverDetails.this,
						ChatLoginActivity.class);
				startActivity(intent);
				return false;
			}
		}
		return true;
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
		params.put("qquid", settings.getString("qquid", ""));
		params.put("weixinuid", settings.getString("weixinuid", ""));
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
		case R.id.image_available:

			if (userBean != null && TextUtils.isEmpty(userBean.getTelphone())) {
				Intent intent = new Intent(DriverDetails.this,
						ChatLoginActivity.class);
				startActivity(intent);
				return;
			}
			String ifshowcheliang = settings.getString("ifshowcheliang", "no");
			if ("yes".equals(ifshowcheliang)
					&& TextUtils.isEmpty(userBean.getChepaihao())
					&& TextUtils.isEmpty(userBean.getChepaihao())) {
				Intent intent = new Intent(DriverDetails.this,
						VehicleActivity.class);
				startActivity(intent);
				return;
			}
			Intent intent = new Intent(DriverDetails.this,
					CallDrvierForOtherActivity.class);
			intent.putExtra("driverid", driverid);
			intent.putExtra("companyid", companyid);
			intent.putExtra("showname", showname);
			startActivity(intent);

			break;

		default:
			break;
		}
	}

}
