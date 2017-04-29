package com.dajia.adapter;

import java.util.ArrayList;

import net.k76.wzd.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dajia.Bean.AddressBean.Address;

public class AddressAdapter extends BaseAdapter implements OnClickListener {
	private ArrayList<Address> mList;
	private Context mContext;
	private Callback mCallback;

	public interface Callback {
		public void click(View v, int flag);
	}

	public AddressAdapter(Context context, ArrayList<Address> mList,  Callback callback) {
		super();
		mContext = context;
		this.mList = mList;
		mCallback = callback;
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
		final Address mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.address_item,
					null);
			viewHolder.add = (TextView) view.findViewById(R.id.address);
			viewHolder.button = (ImageButton) view.findViewById(R.id.delete);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.add.setText(mContent.getAddress());
		viewHolder.add.setOnClickListener(this);
		viewHolder.button.setOnClickListener(this);
		viewHolder.button.setTag(position);
		return view;
	}

	final static class ViewHolder {
		TextView add;
		ImageButton button;
	}

	@Override
	public void onClick(View v) {
		 int flag = (v instanceof ImageButton)? 1:0;
		 mCallback.click(v, flag);
	}

}
