package com.dajia.adapter;

import java.util.List;

import net.k76.wzd.R;

import com.dajia.view.XCRoundRectImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StorePicAdapter extends BaseAdapter {
	private List<String> imglist;
	Context context;
	private LayoutInflater inflater;

	public StorePicAdapter(Context context, List<String> gist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.imglist = gist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (imglist == null) {
			return 0;
		} else {
			return imglist.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (imglist == null) {
			return null;
		} else {
			return imglist.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.storepic_griditem, null,
					false);
			holder = new GViewHolder();
			holder.giv = (XCRoundRectImageView) convertView
					.findViewById(R.id.store_gridimg);
			convertView.setTag(holder);
		} else {
			holder = (GViewHolder) convertView.getTag();
		}
		Uri path = Uri.parse(imglist.get(position));
		Picasso.with(context).load(path).into(holder.giv);
		return convertView;
	}

	public static class GViewHolder {
		XCRoundRectImageView giv;
	}
}
