package com.dajia.Bean;

import java.util.List;

public class MessageLisyBean {
	
	private List<MessageBean> tongzhilist;
	private String unread;
	private String dingdannum;
	public String getDingdannum() {
		return dingdannum;
	}
	public void setDingdannum(String dingdannum) {
		this.dingdannum = dingdannum;
	}
	private List<MessageBean> chepai;
	
	
	
	public List<MessageBean> getChepai() {
		return chepai;
	}
	public void setChepai(List<MessageBean> chepai) {
		this.chepai = chepai;
	}
	public List<MessageBean> getTongzhilist() {
		return tongzhilist;
	}
	public void setTongzhilist(List<MessageBean> tongzhilist) {
		this.tongzhilist = tongzhilist;
	}
	public String getUnread() {
		return unread;
	}
	public void setUnread(String unread) {
		this.unread = unread;
	}
	
}
