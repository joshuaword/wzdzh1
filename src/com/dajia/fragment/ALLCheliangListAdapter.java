package com.dajia.fragment;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajia.Bean.MessageBean;
/**
 * * 实现对listView的循环滚动 
 */
public class ALLCheliangListAdapter extends BaseAdapter {
	private List<MessageBean> list;
	private String unRead;
	private LayoutInflater mInflater;
	Context context;
	public ALLCheliangListAdapter(Context context, List<MessageBean> list,String unRead){
		this.list = list;
		this.unRead = unRead;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public View getView(final int postition, View convertView, ViewGroup arg2) {
		ViewHoler viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.my_item_list_cheliang, null);
			viewHolder.txt_message = (TextView) convertView.findViewById(R.id.txt_content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHoler) convertView.getTag();
		}
		viewHolder.txt_message.setText(list.get(postition).getChexing()+"  "+list.get(postition).getChepaihao());//取余展示数据
		return convertView;
	}
	
	static class ViewHoler{
		TextView txt_message,txt_biaoti;
		View linearLayout;
		ImageView img;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
