package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajia.Bean.DaijiaInfo;

public class DaijiaLcAdapter extends BaseAdapter implements OnClickListener {
	private List<DaijiaInfo> mList;
	private Context mContext;
	private int clickTemp = -1;

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
	}

	public interface Callback {
		public void click(View v, int flag);
	}

	public DaijiaLcAdapter(Context context, List<DaijiaInfo> mList
			) {
		super();
		mContext = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		if (mList == null) {
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
		final DaijiaInfo mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.daijialc_item, null);

			viewHolder.title = (TextView) view.findViewById(R.id.txt_one);
			viewHolder.content = (TextView) view
					.findViewById(R.id.txt_one_content);
			viewHolder.circle_imge1 = (ImageView) view
					.findViewById(R.id.circle_imge1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.title.setText(mContent.getLine1());//“洗车工已接单”
		viewHolder.content.setText(mContent.getLine2());//时间
		//listview的第一条因为是正在进行的，让它的颜色有别于下边的
		if (position == 0) {
			viewHolder.circle_imge1.setImageResource(R.drawable.xiao3);
			viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.blue_text));
			viewHolder.content.setTextColor(mContext.getResources().getColor(R.color.blue_text));
		} else {
			viewHolder.title.setTextColor(Color.BLACK);
			viewHolder.content.setTextColor(Color.BLACK);
			viewHolder.circle_imge1.setImageResource(R.drawable.xiao2);
		}

		return view;
	}

	final static class ViewHolder {
		TextView title, content;
		View youhui_item_layout;
		ImageView circle_imge1;

	}

	@Override
	public void onClick(View v) {
	}

}
