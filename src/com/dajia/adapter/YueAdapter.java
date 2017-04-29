package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dajia.Bean.YouhuiListItem;
/**
 * 余额adapter
 * @author Administrator
 *
 */
public class YueAdapter extends BaseAdapter {
	List<YouhuiListItem> list;
	LayoutInflater mLayoutInflater;
	private Context context;

	public YueAdapter(Context paramContext,
			List<YouhuiListItem> paramList) {
		this.mLayoutInflater = LayoutInflater.from(paramContext);
		this.list = paramList;
		context = paramContext;
	}


	public int getCount() {
		return this.list.size();
	}


	public Object getItem(int paramInt) {
		return this.list.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

		final YouhuiListItem youhuiitem = (YouhuiListItem)this.list.get(paramInt);
		final ViewHolder localViewHolder;
		if (paramView == null) {
			paramView = this.mLayoutInflater.inflate(
					R.layout.item_yue, null);
			localViewHolder = new ViewHolder();
			localViewHolder.line1 = (TextView)paramView.findViewById(R.id.line1);
			localViewHolder.line2 = (TextView)paramView.findViewById(R.id.line2);
			paramView.setTag(localViewHolder);
		} else {
			localViewHolder = (ViewHolder) paramView.getTag();
		}
		
		localViewHolder.line1 .setText(youhuiitem.getLine1()!=null?youhuiitem.getLine1():"");//时间
		localViewHolder.line2 .setText(youhuiitem.getLine2()!=null?youhuiitem.getLine2():"");//充值和余额信息
		return paramView;
	}
	
	static class ViewHolder {
		TextView line1;
		TextView line2;
	}
}