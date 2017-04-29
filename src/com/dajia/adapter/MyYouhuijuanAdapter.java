package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dajia.Bean.YouHuiItem;

public class MyYouhuijuanAdapter extends BaseAdapter implements OnClickListener {
	private List<YouHuiItem> mList;
	private Context mContext;
	
	public interface Callback {
		public void click(View v, int flag);
	}

	public MyYouhuijuanAdapter(Context context, List<YouHuiItem> mList) {
		super();
		mContext = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		if(mList == null) {
			return 0;
		}
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
		final YouHuiItem mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.youhuijuan_item,
					null);
			viewHolder.title = (TextView) view.findViewById(R.id.tv_youhui_title);
			viewHolder.content = (TextView) view.findViewById(R.id.tv_youhui_content);
			viewHolder.price = (TextView) view.findViewById(R.id.tv_youhui_money);
			viewHolder. youhui_item_layout = view.findViewById(R.id.youhui_item_layout);
			viewHolder.lasttime=(TextView) view.findViewById(R.id.tv_youhui_lasttime);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.title.setText(mContent.getName());
		viewHolder.content.setText(mContent.getShuoming());
		viewHolder.price.setText(mContent.getJine());
		viewHolder.lasttime.setText("失效时间："+mContent.getEndtime() );
		
		return view;
	}

	final static class ViewHolder {
		TextView title,content,price,lasttime;
		View youhui_item_layout;
	}

	@Override
	public void onClick(View v) {
	}

}
