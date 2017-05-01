package com.dajia.fragment;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daijia.chat.ChatActivity;
import com.daijia.chat.SearchChatActivity;
import com.dajia.Bean.ChatBean;
import com.dajia.Bean.ChatListBean;
import com.dajia.Bean.MessageBean;
import com.dajia.activity.ChatLoginActivity;
import com.dajia.activity.MessageHtmlActivity;
/**
 * * 实现对listView的循环滚动 
 */
public class ALLChatListAdapter extends BaseAdapter {
	private List<ChatListBean> list;
	private String unRead;
	private LayoutInflater mInflater;
	Context context;
	public ALLChatListAdapter(Context context, List<ChatListBean> list,String unRead){
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
			convertView = mInflater.inflate(R.layout.my_item_list_chat, null);
			viewHolder.img=(ImageView)convertView.findViewById(R.id.img);
			viewHolder.txt_message = (TextView) convertView.findViewById(R.id.txt_content);
			viewHolder.linearLayout= convertView.findViewById(R.id.LinearLayout);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHoler) convertView.getTag();
		}
		
		viewHolder.txt_message.setText(list.get(postition).getLeirong());//取余展示数据
		
		
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
