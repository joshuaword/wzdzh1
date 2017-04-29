package com.dajia.fragment;

import java.util.ArrayList;

import net.k76.wzd.R;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dajia.Bean.YouHuiItem;
import com.dajia.Bean.ZhanghuBean;
import com.dajia.adapter.MyYouhuijuanAdapter;
import com.google.gson.Gson;
/**
 * 优惠券：我的账户下面的1/3
 * @author Administrator
 *
 */
public class YouhuijuanFragment extends BaseFragment  {
	private MyYouhuijuanAdapter mAdapter;
	private ListView listView;
	private  TextView  no_no;
	private ArrayList<YouHuiItem> mInfoList = new ArrayList<YouHuiItem>();
	String userid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return setRootView(inflater, R.layout.yue_list);
	}

	@Override
	public void initData() {
		userid = getActivity().getIntent().getStringExtra("userid");
		FinalHttp fp = new FinalHttp();
		final SharedPreferences settings = getActivity().getSharedPreferences(
				"setting", 0);
		AjaxParams params = new AjaxParams();
		params.put("userid", userid);
		params.put("act", "postok");
		String baseurl = settings.getString("baseurl", "http://wzd.k76.net");
		fp.post(baseurl + "/api/userinfoclient.php", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						Gson gson = new Gson();
						ZhanghuBean bean = gson.fromJson(t.toString(),
								ZhanghuBean.class);
						if (bean != null) {
							if ("success".equals(bean.getRet())) {
								if (bean.getYouhuiquan() != null
										&& bean.getYouhuiquan().size() > 0) {
									mInfoList.addAll(bean.getYouhuiquan());
									mAdapter = new MyYouhuijuanAdapter(getActivity(),
											bean.getYouhuiquan());
									listView.setAdapter(mAdapter);
								}else{
									//如果没有数据，隐藏listview显示没有信息
									listView.setVisibility(View.INVISIBLE);
									no_no.setVisibility(View.VISIBLE);
									no_no.setText("没有优惠券信息");
								}
							}
						}
					}

				});
	}

	@Override
	protected void initView(View rootView) {
		// TODO Auto-generated method stub
		listView = (ListView) rootView.findViewById(R.id.commonlistView);
		no_no=(TextView) rootView.findViewById(R.id.no_no);
	}


}
