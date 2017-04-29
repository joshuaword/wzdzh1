package com.dajia.adapter;

import java.util.List;
import java.util.regex.Pattern;

import net.k76.wzd.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dajia.Bean.PinglunBean;

public class EvalutateAdapter extends BaseAdapter
{
  private List<PinglunBean> list;
  private LayoutInflater mInflater;

  public EvalutateAdapter(Context paramContext, List<PinglunBean> paramList)
  {
    this.mInflater = LayoutInflater.from(paramContext);
    this.list = paramList;
  }

  public int getCount()
  {
    return this.list.size();
  }

  public Object getItem(int paramInt)
  {
    return this.list.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ViewHolder localViewHolder;
    if (paramView == null)
    {
      paramView = this.mInflater.inflate(R.layout.evalute_list_item, null);
      localViewHolder = new ViewHolder();
      localViewHolder.driver_rating = ((RatingBar)paramView.findViewById(R.id.driver_rating));
      localViewHolder.text_evalate_content = ((TextView)paramView.findViewById(R.id.text_evalate_content));
      localViewHolder.text_evalate_date = ((TextView)paramView.findViewById(R.id.text_evalate_date));
      paramView.setTag(localViewHolder);
    }else{
        localViewHolder = (ViewHolder) paramView.getTag();
    }
    //如果返回的星级不是数字型的String
    String xingJi=((PinglunBean)this.list.get(paramInt)).getXingji();
    if(!isNumber(xingJi)){
    	xingJi="5";
    	}
      localViewHolder.driver_rating.setRating(Float.parseFloat(xingJi));
      String str1 = ((PinglunBean)this.list.get(paramInt)).getLeirong();
      String str2 = Pattern.compile("\t|\r|\n").matcher(str1).replaceAll("");
      localViewHolder.text_evalate_content.setText(str2);
      String str3 = ((PinglunBean)this.list.get(paramInt)).getRiqi();
      if (!TextUtils.isEmpty(str3))
      {
        int i = str3.trim().indexOf(" ");
        if (i > 0)
          str3 = str3.substring(0, i);
      }
      localViewHolder.text_evalate_date.setText(str3);
      return paramView;
  }
 //判断字符串是否是数字 
  public static boolean isNumber(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 

  private static class ViewHolder
  {
    RatingBar driver_rating;
    TextView text_evalate_content;
    TextView text_evalate_date;
  }
}