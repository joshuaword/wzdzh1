package com.dajia.fragment;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dajia.Bean.MessageBean;
import com.dajia.activity.MainActivity;
import com.dajia.activity.MessageHtmlActivity;
import com.dajia.activity.MessageListActivity;
/**
 * * 实现对listView的循环滚动 
 */
public class MessageListAdapter extends BaseAdapter {
	
	private List<MessageBean> list;
	private String unRead;
	private LayoutInflater mInflater;
	Context context;
	public MessageListAdapter(Context context, List<MessageBean> list,String unRead){
		this.list = list;
		this.unRead = unRead;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}
	/**
	 * 将数据循环展示三遍
	 */
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0 % list.size());
	}

	@Override
	public long getItemId(int arg0) {
		return arg0 % list.size();
	}
	@Override
	public View getView(final int postition, View convertView, ViewGroup arg2) {
		ViewHoler viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHoler();
			convertView = mInflater.inflate(R.layout.item_list_message, null);
			viewHolder.txt_message = (TextView) convertView.findViewById(R.id.txt_message);
			viewHolder.linearLayout= convertView.findViewById(R.id.LinearLayout);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHoler) convertView.getTag();
		}
		
		viewHolder.txt_message.setText(list.get(postition % list.size()).getBiaoti());//取余展示数据
		viewHolder.linearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MessageListActivity.class);
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	static class ViewHoler{
		TextView txt_message;
		View linearLayout;
	}

}
