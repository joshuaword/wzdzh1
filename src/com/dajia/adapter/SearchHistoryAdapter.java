package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;

import com.dajia.Bean.CityType;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchHistoryAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;
	public SearchHistoryAdapter(Context context, List<String> mList) {
		super();
		mContext = context;
		this.mList = mList;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_search_history, null);
			viewHolder.txt_keyword = (TextView) convertView
					.findViewById(R.id.txt_keyword);
			convertView.setTag(viewHolder);
			String history = mList.get(position).toString();
			viewHolder.txt_keyword.setText(history);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	final static class ViewHolder {
		TextView txt_keyword;
	}
	
	

}
