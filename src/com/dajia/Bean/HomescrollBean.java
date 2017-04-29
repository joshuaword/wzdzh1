package com.dajia.Bean;

import java.util.List;


public class HomescrollBean {
	
	private String fuwuleixingid;
	private String fuwuleixing;
	private List<ServiceProjectBean>  fuwuxiangmu;

	public List<ServiceProjectBean> getFuwuxiangmu() {
		return fuwuxiangmu;
	}


	public void setFuwuxiangmu(List<ServiceProjectBean> fuwuxiangmu) {
		this.fuwuxiangmu = fuwuxiangmu;
	}


	public String getFuwuleixingid() {
		return fuwuleixingid;
	}


	public void setFuwuleixingid(String fuwuleixingid) {
		this.fuwuleixingid = fuwuleixingid;
	}

	public String getFuwuleixing() {
		return fuwuleixing;
	}

	public void setFuwuleixing(String fuwuleixing) {
		this.fuwuleixing = fuwuleixing;
	}
	
}
