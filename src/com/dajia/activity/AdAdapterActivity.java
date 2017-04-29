package com.dajia.activity;

import java.util.ArrayList;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.AddressBean;
import com.dajia.adapter.AddressAdapter;
import com.dajia.constant.Constant;
import com.google.gson.Gson;

public class AdAdapterActivity extends BaseActivity implements
		AddressAdapter.Callback {
	private final static String TAG = "AddressActivity";
	private Context mContext;
	private ListView mList;
	private TextView mAdd;
	private String mUserid;

	private ArrayList<AddressBean.Address> mAddressList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_address);
		((TextView) findViewById(R.id.title))
				.setText(getString(R.string.maage_address));
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		mAdd = (TextView) findViewById(R.id.btnpay);
		mAdd.setText(R.string.vehicle_add);
		mAdd.setVisibility(View.VISIBLE);
		mAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						SelectAddressActivity.class);
				startActivity(intent);
			}
		});
		mList = (ListView) findViewById(R.id.list);

		SharedPreferences sp = getSharedPreferences("setting", 0);
		mUserid = sp.getString("userid", "");
		getAddressData();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		getAddressData();
	}

	private void getAddressData() {

		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", mUserid);
		params.put("act", "postok");
		Log.d(TAG, Constant.API_ADDRESS_MANAGE + " params:" + params);
		fp.post(Constant.API_ADDRESS_MANAGE, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						if (t == null) {
							Toast.makeText(
									mContext,
									mContext.getString(R.string.server_no_return),
									Toast.LENGTH_SHORT).show();
							return;
						}
						Log.d(TAG, t.toString());
						Gson gson = new Gson();

						AddressBean add = gson.fromJson(t.toString(),
								AddressBean.class);
						if (!("success".equals(add.getRet()))) {
							Toast.makeText(mContext,
									mContext.getString(R.string.server_fail),
									Toast.LENGTH_SHORT).show();
							return;
						}
						mAddressList = (ArrayList<AddressBean.Address>) add
								.getAddress();
						Log.d(TAG, "get address:" + mAddressList);
						mList.setAdapter(new AddressAdapter(mContext,
								mAddressList, AdAdapterActivity.this));
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(mContext,
								mContext.getString(R.string.server_fail),
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void deleteAddressData(int position) {
		Log.d(TAG, "deleteAddressData position:" + position);
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("userid", mUserid);
		params.put("deladdressid", mAddressList.get(position).getAddressid());
		params.put("act", "postok");
		Log.d(TAG, Constant.API_ADDRESS_MANAGE + " params:" + params);
		fp.post(Constant.API_ADDRESS_MANAGE, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						if (t == null) {
							Toast.makeText(
									mContext,
									mContext.getString(R.string.server_no_return),
									Toast.LENGTH_SHORT).show();
							return;
						}
						Log.d(TAG, t.toString());
						Gson gson = new Gson();
						DeleteBean resulte = gson.fromJson(t.toString(),
								DeleteBean.class);
						Log.d(TAG,
								"deleteAddressData resulte:" + resulte.getRet());
						if (!("success".equals(resulte.getRet()))) {
							Toast.makeText(mContext,
									mContext.getString(R.string.server_fail),
									Toast.LENGTH_SHORT).show();
						} else {
							getAddressData();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Toast.makeText(mContext,
								mContext.getString(R.string.server_fail),
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	@Override
	public void click(View v, int flag) {
		if (flag == 1) {
			Log.d(TAG,"delete a address");
			deleteAddressData((Integer) (v.getTag()));
		} else {
			Log.d(TAG,"select a address");
			Intent intent = new Intent(mContext, CallDrvierForOtherActivity.class);
			intent.putExtra("flag", "chufa");
			intent.putExtra("content", ((TextView) v).getText());
			startActivity(intent);
			finish();
		}
	}

	class DeleteBean {
		private String ret;

		public String getRet() {
			return ret;
		}

		public void setRet(String ret) {
			this.ret = ret;
		}
	}
}
