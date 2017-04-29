package com.dajia.activity;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.VehicleType;
import com.dajia.util.VehicleComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * 订单详情（车型以及车牌号选择）
 * @author Administrator
 *
 */
public class VehicleActivity extends BaseActivity {
	public final static String SP_KEY_VEHICLE_TYEP = "sp_key_vehicle_type";
	public final static String SP_KEY_VEHICLE_NUMBER = "sp_key_vehicle_number";
	public static final String KEY_INTENT_DATA = "key_intent_data";

	private final static String TAG = "VehicleActivity";
	private static final int REQUEST_CODE = 999;

	private EditText mEditText;
	private TextView mVehicleNumber;
	private Context mContext;
	private String from;
	ArrayAdapter<String> adap ;
	private NoScrollGridView gridview;
	private ArrayList<VehicleType> mVehicleTypeList = new ArrayList<VehicleType>();

	private Button mAdd;
	private EditText mVehicleType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_vehicle);
		from = getIntent().getStringExtra("from");
		((TextView) findViewById(R.id.title))
				.setText(getString(R.string.vehicle_title));
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		mVehicleType = (EditText) findViewById(R.id.vehicle_type);
		mVehicleType.setKeyListener(null);
		gridview = (NoScrollGridView)findViewById(R.id.gridview);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				mVehicleNumber.setText(adap.getItem(position));
			}
		});
		String type = getIntent().getStringExtra("chexing");
		if (type != null) {
			mVehicleType.setText(type);
		}
		mVehicleType.setEnabled(false);
		mVehicleType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, VehicleBrandActivity.class);
				intent.putExtra(KEY_INTENT_DATA,
						(Serializable) mVehicleTypeList);
				startActivityForResult(intent, REQUEST_CODE);
			}

		});

		mAdd = (Button) findViewById(R.id.btn_add);
		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String number = mEditText.getText().toString();
				if (number.length() < 6
						|| mVehicleType.getText().toString().length() < 1) {
					Toast.makeText(mContext,
							mContext.getString(R.string.vehicle_number_not_right),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if(from!=null&&from.equals("addMYCL")){
					addMycheliang(number);
				}else{
					add(number);
				}
			}

		});

		mEditText = (EditText) findViewById(R.id.ed_vehicle_number);
		mVehicleNumber = (TextView) findViewById(R.id.province);

		String[] province = getResources().getStringArray(R.array.province);
		 adap = new ArrayAdapter<String>(this,
				R.layout.province_item, province);
//		mVehicleNumber.setAdapter(adap);
		gridview.setAdapter(adap);
		String number = getIntent().getStringExtra("chepaihao");
		Log.d(TAG,"number:" + number);
		if (number != null) {
			String tmp = number;
			String value = tmp.replaceAll("[a-zA-Z0-9]", "");
			Log.d(TAG,"value:" + value);
			if (value != null) {
				if(!TextUtils.isEmpty(number)){
					mVehicleNumber.setText(number.subSequence(0, 1));
				}
				String temp = number.replaceAll(value, "");
				Log.d(TAG,"temp:" + temp);
				if(temp.length()>6) {
					temp = "";
				}
				mEditText.setText(temp);
			}
		}
		initDtat();
	}

	
	
	private void add(String number){
		SharedPreferences sp = getSharedPreferences("setting", 0);
		String userid = sp.getString("userid", "");
		String baseurl = sp.getString("baseurl", "http://zgjj.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("userid", userid);
		ap.put("chepaihao", mVehicleNumber.getText().toString()
				+ number);
		ap.put("chexing", mVehicleType.getText().toString());
		ap.put("act", "postok");
		fp.post(baseurl + "/api/registerclient.php", ap,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						if(from!=null&&from.equals("yanzhen")){
							sendBroadcast(new Intent("finish"));
							Toast.makeText(mContext, "修改成功",
									Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(mContext, "修改成功",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					}

				});
	}
	
	private void addMycheliang(String number){
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		String userid = settings.getString("userid", "");
		 FinalHttp fp = new FinalHttp();
	        AjaxParams ap = new AjaxParams();
	    	ap.put("userid", userid);
			ap.put("chepaihao", mVehicleNumber.getText().toString()
					+ number);
			ap.put("chexing", mVehicleType.getText().toString());
			ap.put("act", "postok");
	        fp.post(baseurl + "/api/addcheliangclient.php", ap, new AjaxCallBack<Object>() {
	        	//请求成功回调
	        	@Override
	        	public void onSuccess(Object t) {
	        		Gson gson = new Gson();
	        		Log.v("aaaa", t.toString());
				finish();
	        	}
	        	//请求失败回调
	        	@Override
	        	public void onFailure(Throwable t, int errorNo, String strMsg) {
	        		super.onFailure(t, errorNo, strMsg);
	        		Log.e("aaaa", t.toString() + "-------" + errorNo + "-------" + strMsg);
	        	}
			});
	}
	public void initDtat() {
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://zgjj.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("act", "postok");
		
		fp.post(baseurl + "/api/chexinglistclient.php", params, new AjaxCallBack<Object>() {
			@Override
			public void onSuccess(Object t) {
				Context con = VehicleActivity.this;
				if (t == null) {
					Toast.makeText(con,
							con.getString(R.string.server_no_return),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Gson gson = new Gson();

				Type listType = new TypeToken<LinkedList<VehicleType>>() {
				}.getType();
				LinkedList<VehicleType> vehicleTypes = gson.fromJson(
						t.toString(), listType);
				for (Iterator<VehicleType> iterator = vehicleTypes.iterator(); iterator
						.hasNext();) {
					VehicleType type = (VehicleType) iterator.next();
					mVehicleTypeList.add(type);
					Log.d(TAG, type.toString());
				}

				Log.d(TAG, "---------sort begin--------");
				Collections.sort(mVehicleTypeList, new VehicleComparator());
				for (VehicleType type : mVehicleTypeList) {
					Log.d(TAG, type.toString());
				}
				Log.d(TAG, "---------sort end--------");
				mVehicleType.setEnabled(true);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				Context con = VehicleActivity.this;
				Toast.makeText(con, con.getString(R.string.server_fail),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			VehicleType type = mVehicleTypeList.get(resultCode);
			mVehicleType.setText(type.getName());
		}
	}
}
