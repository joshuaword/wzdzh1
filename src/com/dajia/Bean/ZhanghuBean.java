package com.dajia.Bean;

import java.io.Serializable;
import java.util.List;

public class ZhanghuBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<YouhuiListItem> yuelist;
	private List<YouHuiItem> youhuiquan;
	private List<YouhuiListItem> jifenlist;

	private String ret;
	private String stryouhuijuan;
	private String strjifen;
	private String stryue;
	private String fenxiang;
	private String liantu;
	private String weixinliantu;
	private String yue;
	
	

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getStryouhuijuan() {
		return stryouhuijuan;
	}

	public void setStryouhuijuan(String stryouhuijuan) {
		this.stryouhuijuan = stryouhuijuan;
	}

	public String getStrjifen() {
		return strjifen;
	}

	public void setStrjifen(String strjifen) {
		this.strjifen = strjifen;
	}

	public String getStryue() {
		return stryue;
	}

	public void setStryue(String stryue) {
		this.stryue = stryue;
	}

	public String getFenxiang() {
		return fenxiang;
	}

	public void setFenxiang(String fenxiang) {
		this.fenxiang = fenxiang;
	}

	public String getLiantu() {
		return liantu;
	}

	public void setLiantu(String liantu) {
		this.liantu = liantu;
	}

	public String getWeixinliantu() {
		return weixinliantu;
	}

	public void setWeixinliantu(String weixinliantu) {
		this.weixinliantu = weixinliantu;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public List<YouhuiListItem> getYuelist() {
		return yuelist;
	}

	public void setYuelist(List<YouhuiListItem> yuelist) {
		this.yuelist = yuelist;
	}

	public List<YouHuiItem> getYouhuiquan() {
		return youhuiquan;
	}

	public void setYouhuiquan(List<YouHuiItem> youhuiquan) {
		this.youhuiquan = youhuiquan;
	}

	public List<YouhuiListItem> getJifenlist() {
		return jifenlist;
	}

	public void setJifenlist(List<YouhuiListItem> jifenlist) {
		this.jifenlist = jifenlist;
	}

}