package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;

import com.dajia.Bean.CityType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CityExpandlistAdapter extends BaseExpandableListAdapter {
	private List<CityType> mList;
	private List<String> citynamelist;
	Context context;
	private LayoutInflater mChildInflater; // 用于加载二级分类的布局xml
	private LayoutInflater mGroupInflater; // 用于加载一级分类的布局xml

	public CityExpandlistAdapter(Context context, List<CityType> mList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mGroupInflater = LayoutInflater.from(context);
		mChildInflater = LayoutInflater.from(context);
		this.mList = mList;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return mList.get(groupPosition).getChengshi().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return mList.get(groupPosition).getChengshi().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mGroupInflater.inflate(R.layout.city_zimu_layout,
					null);
		}
		TextView zimutxt = (TextView) convertView.findViewById(R.id.city_zimu);
		zimutxt.setText(mList.get(groupPosition).getZimu());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mChildInflater.inflate(R.layout.city_name_layout,
					null);
			holder.nametxt = (TextView) convertView
					.findViewById(R.id.cityname_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		citynamelist = mList.get(groupPosition).getChengshi();
		holder.nametxt.setText(citynamelist.get(childPosition));
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		// 是否选中指定位置上的子元素。
		String temp =mList.get(groupPosition).getChengshi().get(childPosition);
		Toast.makeText(context, "你选择了[" + temp + "]", Toast.LENGTH_LONG).show();
		return true;
	}

	final static class ViewHolder {
		TextView nametxt;
	}
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int groupPosition) {
		return mList.get(groupPosition).getZimu().charAt(0);
	}
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getGroupCount(); i++) {
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
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "热门";
		}
	}
	
}
