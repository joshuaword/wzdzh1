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

public class CitylistAdapter extends BaseAdapter {
	private Context mContext;
	private List<CityType> mList;
	private List<String> citynamelist;
	public CitylistAdapter(Context context, List<CityType> mList) {
		super();
		mContext = context;
		this.mList = mList;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CityType> list){
		this.mList = list;
		notifyDataSetChanged();
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
					R.layout.city_list_item, null);
			viewHolder.text = (TextView) convertView
					.findViewById(R.id.cityname_text);
			viewHolder.letter = (TextView) convertView
					.findViewById(R.id.city_zimu);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
	/*	if(position == getPositionForSection(section)){
			viewHolder.letter.setVisibility(View.VISIBLE);
			viewHolder.letter.setText((mList.get(position).getZimu()));
		}else{
			viewHolder.letter.setVisibility(View.GONE);
			
		}*/
		viewHolder.letter.setText((mList.get(position).getZimu()));
		Log.e("zimu", mList.get(position).getZimu());
		citynamelist=mList.get(position).getChengshi();
	/*	for (int i = 0; i < citynamelist.size(); i++) {
			viewHolder.text.setText(citynamelist.get(i));
		}*/
		viewHolder.text.setText(citynamelist.get(position));
		return convertView;
	}
	final static class ViewHolder {
		TextView letter;
		TextView text;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mList.get(position).getZimu().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getZimu();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

}
