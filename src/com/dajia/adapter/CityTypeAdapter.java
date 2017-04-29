package com.dajia.adapter;

import java.util.ArrayList;
import net.k76.wzd.R;
import com.dajia.Bean.CityType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityTypeAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<CityType> mList;

	public CityTypeAdapter(Context context, ArrayList<CityType> mList) {
		super();
		mContext = context;
		this.mList = mList;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.city_list_item, null);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.brand_text);
			viewHolder.letter = (TextView) convertView
					.findViewById(R.id.catalog);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CityType item = mList.get(position);
		// 根据position获取分类的首字母的Char ascii值
		String section = getSectionForPosition(position);

		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.letter.setVisibility(View.VISIBLE);
			viewHolder.letter.setText(item.getZimu());
		} else {
			viewHolder.letter.setVisibility(View.GONE);
		}

		//viewHolder.text.setText(item.getName());
		return convertView;
	}

	final static class ViewHolder {
		TextView letter;
		TextView text;
	}

	public int getPositionForSection(String sectionIndex) {
		int size = mList.size();
		CityType data;
		for (int i = 0; i < size; i++) {
			data = mList.get(i);
			if (data.getZimu().equals(sectionIndex)) {
				return i;
			}
		}
		return -1;
	}

	public String getSectionForPosition(int position) {
		return mList.get(position).getZimu();
	}
}
