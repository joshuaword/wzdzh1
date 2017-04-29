package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;

import com.dajia.Bean.HomescrollBean;
import com.dajia.Bean.ServiceProjectBean;
import com.dajia.activity.MainActivity;
import com.dajia.view.myGridView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class HomeListviewAdapter extends BaseAdapter {
	private List<HomescrollBean> mList;
	private List<ServiceProjectBean> mGist;
	Context context;

	// 水平间距
	private int hSpacing = 10;

	public HomeListviewAdapter(Context context, List<HomescrollBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mList == null) {
			return 0;
		} else {
			return mList.size();
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList == null) {
			return null;
		} else {
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.homescrollview_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.hometype_tv);
			holder.gv = (myGridView) convertView.findViewById(R.id.hometype_gv);
			holder.line = convertView.findViewById(R.id.homelist_line);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(mList.get(position).getFuwuleixing());

		if (mList.size() == 1) {
			holder.line.setVisibility(View.GONE);
		} else {
			holder.line.setVisibility(View.VISIBLE);
		}

		mGist = mList.get(position).getFuwuxiangmu();

		HomeGridviewAdapter ga = new HomeGridviewAdapter(context, mGist);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		holder.gv.setLayoutParams(params);
		holder.gv.setHorizontalSpacing(hSpacing);
		holder.gv.setVerticalSpacing(10);
		holder.gv.setPadding(10, 0, 10, 0);
		holder.gv.setNumColumns(4);
		holder.gv.setAdapter(ga);
		holder.gv.setHaveScrollbar(false);
		holder.gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int positionid, long id) {
				// TODO Auto-generated method stub

				SharedPreferences settings = context.getSharedPreferences(
						"setting", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("selectfuwulx", mList.get(position)
						.getFuwuleixing());
				editor.putString("ifshowdrivernum", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfshowdrivernum());
				editor.putString("ifshowcheliang", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfshowcheliang());
				editor.putString("ifcanyuyue", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfcanyuyue());
				editor.putString("ifshowmudidi", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfshowmudidi());
				editor.putString("ifxiadancongzi", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfxiadancongzi());
				editor.putString("ifmapfirst", mList.get(position)
						.getFuwuxiangmu().get(positionid).getIfmapfirst());
				editor.putString("selectfuwutype", mList.get(position)
						.getFuwuxiangmu().get(positionid).getName());
				editor.commit();

				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	public static class ViewHolder {
		TextView tv;
		myGridView gv;
		View line;
	}
}
