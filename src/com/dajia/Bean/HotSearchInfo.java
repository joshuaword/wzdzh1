package com.dajia.Bean;

public class HotSearchInfo {

	private String mFeature;

	public String getFeature() {
		return mFeature;
	}

	public void setFeature(String Feature) {
		this.mFeature = Feature;
	}
	
	//超过4个汉字占2个格子，默认1格子
	public int getGridCount()
	{
		int length=mFeature.length();
		if(length>4)
			return 2;
		return 1;
	}

}
