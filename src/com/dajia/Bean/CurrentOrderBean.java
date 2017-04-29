package com.dajia.Bean;

import java.util.List;
/**
 * 当前订单实体类（具体参数含义自己看客户端8接口）
 * @author Administrator
 *
 */

public class CurrentOrderBean {

	private String ret;
	private String str1;
	private String str2;
	private String str3;
	private String str4;
	private String ifneedprepay;
	private String telphone;
	private String dingdanid;
	private String dingdanhao;
	private String fuwuleirong;
	private String zhifujine;
	private String zhanghuyue;
	private String zhifufangshi;
	private String state;
	private String ifhavedingdan;
	private String ifquxiao;
	private String createtime;
	private String chufadidian;
	private String shiyongrendianhua;
	private String thefile;
	private String showname;
	private String xinji;
	private String appjiedan;
	private String appjiedanstr;
	private String appdaoda;
	private String appdaodastr;
	private String appchufa;
	private String appchufastr;
	private String juli;
	private String ifshowphoto;
	private String ifpinlun;
	private String ifcanpay;

	private List<MessageBean> pinlun;

	private List<DaijiaInfo> daijiainfo;

	
	public String getXinji() {
		return xinji;
	}

	public void setXinji(String xinji) {
		this.xinji = xinji;
	}
	public String getIfcanpay() {
		return ifcanpay;
	}

	public void setIfcanpay(String ifcanpay) {
		this.ifcanpay = ifcanpay;
	}

	public String getIfpinlun() {
		return ifpinlun;
	}

	public void setIfpinlun(String ifpinlun) {
		this.ifpinlun = ifpinlun;
	}

	public List<MessageBean> getPinlun() {
		return pinlun;
	}

	public void setPinlun(List<MessageBean> pinlun) {
		this.pinlun = pinlun;
	}

	public List<DaijiaInfo> getDaijiainfo() {
		return daijiainfo;
	}

	public void setDaijiainfo(List<DaijiaInfo> daijiainfo) {
		this.daijiainfo = daijiainfo;
	}

	public String getIfshowphoto() {
		return ifshowphoto;
	}

	public void setIfshowphoto(String ifshowphoto) {
		this.ifshowphoto = ifshowphoto;
	}

	public String getIfneedprepay() {
		return ifneedprepay;
	}

	public String getDingdanhao() {
		return dingdanhao;
	}

	public String getZhifufangshi() {
		return zhifufangshi;
	}

	public void setZhifufangshi(String zhifufangshi) {
		this.zhifufangshi = zhifufangshi;
	}

	public void setDingdanhao(String dingdanhao) {
		this.dingdanhao = dingdanhao;
	}

	public String getFuwuleirong() {
		return fuwuleirong;
	}

	public void setFuwuleirong(String fuwuleirong) {
		this.fuwuleirong = fuwuleirong;
	}

	public String getZhifujine() {
		return zhifujine;
	}

	public void setZhifujine(String zhifujine) {
		this.zhifujine = zhifujine;
	}

	public String getZhanghuyue() {
		return zhanghuyue;
	}

	public void setZhanghuyue(String zhanghuyue) {
		this.zhanghuyue = zhanghuyue;
	}

	public void setIfneedprepay(String ifneedprepay) {
		this.ifneedprepay = ifneedprepay;
	}

	public String getJuli() {
		return juli;
	}

	public void setJuli(String juli) {
		this.juli = juli;
	}

	public String getAppjiedan() {
		return appjiedan;
	}

	public void setAppjiedan(String appjiedan) {
		this.appjiedan = appjiedan;
	}

	public String getAppjiedanstr() {
		return appjiedanstr;
	}

	public void setAppjiedanstr(String appjiedanstr) {
		this.appjiedanstr = appjiedanstr;
	}

	public String getAppdaoda() {
		return appdaoda;
	}

	public void setAppdaoda(String appdaoda) {
		this.appdaoda = appdaoda;
	}

	public String getAppdaodastr() {
		return appdaodastr;
	}

	public void setAppdaodastr(String appdaodastr) {
		this.appdaodastr = appdaodastr;
	}

	public String getAppchufa() {
		return appchufa;
	}

	public void setAppchufa(String appchufa) {
		this.appchufa = appchufa;
	}

	public String getAppchufastr() {
		return appchufastr;
	}

	public void setAppchufastr(String appchufastr) {
		this.appchufastr = appchufastr;
	}

	public String getThefile() {
		return thefile;
	}

	public void setThefile(String thefile) {
		this.thefile = thefile;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getChufadidian() {
		return chufadidian;
	}

	public void setChufadidian(String chufadidian) {
		this.chufadidian = chufadidian;
	}

	public String getShiyongrendianhua() {
		return shiyongrendianhua;
	}

	public void setShiyongrendianhua(String shiyongrendianhua) {
		this.shiyongrendianhua = shiyongrendianhua;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getDingdanid() {
		return dingdanid;
	}

	public void setDingdanid(String dingdanid) {
		this.dingdanid = dingdanid;
	}

	public String getIfhavedingdan() {
		return ifhavedingdan;
	}

	public void setIfhavedingdan(String ifhavedingdan) {
		this.ifhavedingdan = ifhavedingdan;
	}

	public String getIfquxiao() {
		return ifquxiao;
	}

	public void setIfquxiao(String ifquxiao) {
		this.ifquxiao = ifquxiao;
	}

}
