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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajia.Bean.YouhuiListItem;
import com.dajia.Bean.ZhanghuBean;
import com.dajia.adapter.YueAdapter;
import com.google.gson.Gson;
/**
 * 积分页面：我的账户下面的1/3 
 * @author Administrator
 */
public class JifengFragment extends BaseFragment implements OnClickListener {
	private YueAdapter mAdapter;
	private ListView listView;
	private  TextView   no_no;
	private ArrayList<YouhuiListItem> mInfoList = new ArrayList<YouhuiListItem>();
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
								if (bean.getJifenlist() != null
										&& bean.getJifenlist().size() > 0) {
									mInfoList.addAll(bean.getJifenlist());
									mAdapter = new YueAdapter(getActivity(),
											bean.getJifenlist());
									listView.setAdapter(mAdapter);
								}else{
									//如果没有数据，listview消失,显示没有信息
									listView.setVisibility(View.INVISIBLE);
									no_no.setVisibility(View.VISIBLE);
									no_no.setText("没有积分信息");
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
