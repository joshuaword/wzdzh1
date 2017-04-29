package com.dajia.adapter;

import java.util.ArrayList;

import net.k76.wzd.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dajia.Bean.AddressBean.Address;

public class AdAdapter extends PagerAdapter implements OnClickListener {
	private ArrayList<Address> mList;
	private Context mContext;
	private Callback mCallback;

	public interface Callback {
		public void click(View v, int flag);
	}

	public AdAdapter(Context context, ArrayList<Address> mList,  Callback callback) {
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
	public void onClick(View v) {
		 int flag = (v instanceof ImageButton)? 1:0;
		 mCallback.click(v, flag);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
