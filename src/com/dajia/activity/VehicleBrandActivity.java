package com.dajia.activity;

import java.util.ArrayList;

import net.k76.wzd.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dajia.Bean.VehicleType;
import com.dajia.adapter.VehicleTypeAdapter;

public class VehicleBrandActivity extends BaseActivity {
	private final static String TAG = "VehicleBrandActivity";
	private Context mContext;
	private ArrayList<VehicleType> mVehicleTypeList;
	private ListView mList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_vehicle_brand);
		((TextView) findViewById(R.id.title))
				.setText(getString(R.string.vehicle_title));
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		mList = (ListView)findViewById(R.id.list);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d(TAG,"brand selected:" + mVehicleTypeList.get(position).toString());
				setResult(position);
				finish();
			}
		});
		mVehicleTypeList = (ArrayList<VehicleType>)(getIntent().getSerializableExtra(VehicleActivity.KEY_INTENT_DATA));
		mList.setAdapter(new VehicleTypeAdapter(this, mVehicleTypeList));
		
	}
}
