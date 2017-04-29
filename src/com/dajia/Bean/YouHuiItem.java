package com.dajia.Bean;
/**
 * 优惠券上的详情
 * @author Administrator
 *
 */
public class YouHuiItem {
	  private String youhuiquanid;//id
	  private String  name;//名字
	  private String  jine;//优惠金额
	  private String  shuoming;//优惠券简单说明
	  private String  endtime;//失效时间
	 
	  
	
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getYouhuiquanid() {
		return youhuiquanid;
	}
	public void setYouhuiquanid(String youhuiquanid) {
		this.youhuiquanid = youhuiquanid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJine() {
		return jine;
	}
	public void setJine(String jine) {
		this.jine = jine;
	}
	public String getShuoming() {
		return shuoming;
	}
	public void setShuoming(String shuoming) {
		this.shuoming = shuoming;
	}
	  
	  
	  
}
