package com.dajia.activity;

import net.k76.wzd.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.dajia.VehicleApp;
import com.dajia.fragment.JifengFragment;
import com.dajia.fragment.YouhuijuanFragment;
import com.dajia.fragment.YueFragment;
/**
 * 我的账户：余额，优惠券，积分
 * @author Administrator
 *
 */
public class YouhuiAcitivity extends FragmentActivity {

	private FragmentTabHost mTabHost;

	private LayoutInflater layoutInflater;//布局填充器

	private Class<?> fragmentArray[] = { YueFragment.class,
			
			YouhuijuanFragment.class, JifengFragment.class };//frgment数组
	private String mTextviewArray[] = { "余额", "优惠劵", "积分" };
	public String activityId = "";
	public String activityName = "";
	private String userid = "";
	private String menu = "";
	private View back_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youhui_layout);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		VehicleApp.getInstance().addActivity(this);
		userid = getIntent().getStringExtra("userid");
		menu = getIntent().getStringExtra("menu");//接收上个页面传过来的值，确定初始fragment的位置
		initView();
	}

	protected void initView() {
		back_layout = findViewById(R.id.back_layout);
		back_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		layoutInflater = LayoutInflater.from(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, this.getSupportFragmentManager(),
				R.id.realtabcontent);
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		//把传过来的字符串转成int  确定frgment位置
		int menuid =  Integer.parseInt(menu); 
		mTabHost.setCurrentTab(menuid);
	}

	/** {@inheritDoc} */
	protected void initData() {

	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.youhui_tab_item_view, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextviewArray[index]);
		return view;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
