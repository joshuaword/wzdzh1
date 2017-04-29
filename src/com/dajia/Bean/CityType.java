package com.dajia.Bean;

import java.util.List;

/**
 * 城市属性实体类
 * @author zf
 *
 */
public class CityType  {
	
	 private String zimu;
	 private List<String> chengshi;

	public String getZimu() {
		return zimu;
	}

	public void setZimu(String zimu) {
		this.zimu = zimu;
	}

	public List<String> getChengshi() {
		return chengshi;
	}

	public void setChengshi(List<String> chengshi) {
		this.chengshi = chengshi;
	}

}
