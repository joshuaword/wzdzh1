package com.dajia.Bean;

import java.util.List;

public class ChatBean {
	private String ret, lastchatid;
	private List<ChatListBean> chatlist;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}
	
	public String getLastchatid() {
		return lastchatid;
	}

	public void setLastchatid(String lastchatid) {
		this.lastchatid = lastchatid;
	}

	public List<ChatListBean> getChatlist() {
		return chatlist;
	}

	public void setChatlist(List<ChatListBean> chatlist) {
		this.chatlist = chatlist;
	}

}
