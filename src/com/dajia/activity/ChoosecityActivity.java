package com.dajia.activity;

import java.util.List;

import com.dajia.Bean.CityListBean;
import com.dajia.Bean.CityType;
import com.dajia.Bean.HotCityType;
import com.dajia.adapter.CityExpandlistAdapter;
import com.dajia.adapter.HotCityAdapter;
import com.dajia.util.CharacterParser;
import com.dajia.util.PinyinComparator;
import com.dajia.view.MyLetterListView;
import com.dajia.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.dajia.view.ClearEditText;
import com.dajia.view.myExpandListview;
import com.dajia.view.myListView;
import com.google.gson.Gson;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosecityActivity extends BaseActivity {
	private myListView hotcitylistview;
	private myExpandListview citylistview;
	private List<HotCityType> hotlist;
	private CityExpandlistAdapter cityadapter;
	private List<CityType> citylist;

	// 右侧A-Z字母列表
	private MyLetterListView letterListView;
	private TextView overlay;
	// 弹出dialog线程
	private OverlayThread overlayThread;
	private Handler handler;
	private ClearEditText search_ed;
	private List<String> zimulist;//存放城市首字母
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private TextView gpstv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_city_list);
		((TextView) findViewById(R.id.title)).setText("选择城市");
		((LinearLayout) findViewById(R.id.back_layout))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});

		initCityData();
		initView();
	}

	public void initView() {
		gpstv = (TextView) findViewById(R.id.locat_city);
		gpstv.setText(getIntent().getCharSequenceExtra("localcity"));
		gpstv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("seclectcity", gpstv.getText().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		hotcitylistview = (myListView) findViewById(R.id.hotcity_list);
		hotcitylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("seclectcity", hotlist.get(position).getChengshi());
				setResult(RESULT_OK, intent);
				finish();
			}
			
		});
		citylistview = (myExpandListview) findViewById(R.id.city_list);
		citylistview.setGroupIndicator(null);
		citylistview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("seclectcity", citylist.get(groupPosition).getChengshi().get(childPosition));
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		});
		handler = new Handler();
		overlayThread = new OverlayThread();
		letterListView = (MyLetterListView) findViewById(R.id.city_letterlist);
		initOverlay();
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		search_ed = (ClearEditText) findViewById(R.id.citysearch_ed);
		search_ed.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 城市清单
	 */
	public void initCityData() {
		// TODO Auto-generated method stub
		final SharedPreferences settings = getSharedPreferences("setting", 0);
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		FinalHttp fp = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("act", "postok");
		params.put("newver", "1");
		fp.post(baseurl + "/api/citylistclient.php", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						CityListBean bean = gson.fromJson(t.toString(),
								CityListBean.class);
						if (bean != null) {
							if (bean.getRemen() != null
									&& bean.getRemen().size() > 0) {
								hotlist=bean.getRemen();
								HotCityAdapter hotadapter = new HotCityAdapter(
										ChoosecityActivity.this, bean
												.getRemen());
								hotcitylistview.setAdapter(hotadapter);
								hotadapter.notifyDataSetChanged();

							}
							if (bean.getCity() != null
									&& bean.getCity().size() > 0) {
								citylist = bean.getCity();
								cityadapter = new CityExpandlistAdapter(
										ChoosecityActivity.this, citylist);
								citylistview.setAdapter(cityadapter);
								// 默认展开所有项
								for (int i = 0; i < cityadapter.getGroupCount(); i++) {
									citylistview.expandGroup(i);
									//zimulist.add(citylist.get(i).getZimu());
								}
								cityadapter.notifyDataSetChanged();
							}

						}

					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						super.onFailure(t, errorNo, strMsg);
						Context con = ChoosecityActivity.this;
						Toast.makeText(con,
								con.getString(R.string.server_fail),
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	// 右侧A-Z字母监听
	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			// 该字母首次出现的位置
			int position = cityadapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				citylistview.setSelection(position);
				overlay.setText(citylist.get(position).getZimu());
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}
	}

	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}
}
