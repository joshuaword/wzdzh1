package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;

import com.dajia.Bean.HotCityType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotCityAdapter extends BaseAdapter {
	private List<HotCityType> mList;
	Context context;
	private LayoutInflater inflater;

	public HotCityAdapter(Context context, List<HotCityType> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_hotlist_layout, null, false);
			holder = new viewHolder();
			holder.hotcity = (TextView) convertView.findViewById(R.id.hotlist_city);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		holder.hotcity.setText(mList.get(position).getChengshi());
		return convertView;
	}
	public static class viewHolder {
		TextView hotcity;
	}
}
