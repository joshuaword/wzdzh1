package com.dajia.adapter;

import java.io.File;
import java.util.ArrayList;

import net.k76.wzd.R;

import com.dajia.Bean.VehicleType;
import com.dajia.widgets.AsyncImageView;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class VehicleTypeAdapter extends BaseAdapter {
	private ArrayList<VehicleType> mList;
	private Context mContext;
	private final static String CACHE_DIR = getSDPath() + "/dashou/";
	
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}
	
	public VehicleTypeAdapter(Context context, ArrayList<VehicleType> mList) {
		super();
		mContext = context;
		this.mList = mList;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final VehicleType mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.brand_item, null);
			viewHolder.text = (TextView) view.findViewById(R.id.brand_text);
			viewHolder.letter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.image = (AsyncImageView) view.findViewById(R.id.brand_logo);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		VehicleType item = mList.get(position);
		//根据position获取分类的首字母的Char ascii值
		String section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.letter.setVisibility(View.VISIBLE);
			viewHolder.letter.setText(item.getZimu());
		}else{
			viewHolder.letter.setVisibility(View.GONE);
		}
	    
		viewHolder.text.setText(item.getName());
		viewHolder.image.asyncLoadBitmapFromUrl(item.getLogo(), CACHE_DIR
				+ item.getName());
		return view;
	}
	
	final static class ViewHolder {
		TextView letter;
		TextView text;
		AsyncImageView image;
	}
	
	public int getPositionForSection(String sectionIndex) {
		int size = mList.size();
		VehicleType data;
		for (int i = 0; i < size; i++) {
			data = mList.get(i);
			if(data.getZimu().equals(sectionIndex)){
				return i;
			}
		}
		return -1;
	}

	public String getSectionForPosition(int position) {
		return mList.get(position).getZimu();
	}
}
