package com.dajia.Bean;

import java.io.Serializable;

public class HistoryOrderInfo implements Serializable {
	private static final long serialVersionUID = -2226653918161843441L;
	private String income;
	private String is_comment;
	private String order_id;
	private String ratingNum;
	private String  pingjiaxingji ;
	private String yuyuerendianhua;
	private String chufadidian;
	private String mudidi;
	private String yuyuechufashijia;
	private String dingdanid;//订单id
	private String jine;
	private String gonghao;
	private String line1; //"日期：20150110001 2015-01-10 16:16",
	private String line2;
	private String line3;//出发地
	private String line4;//目的地
	private String dingdanhao;//订单号

	

	private String ifpingjia;//是否评价
	private String xinji;
	private String pingjiatext;
	private String xiangqingtitle;  //"订单详情",
	private String xiangqingline1; 
	private String xiangqingline2;   //"总金额：",
	private String xiangqingjien;    //"￥39",
	private String xiangqingline3;   //"余额扣除：￥0 优惠券：1 现金支付：￥39",
	private String xiangqingline4;    //"出发地：南京市三山街地铁口2号口",
	private String xiangqingline5; //"目的地：南京市新街口2号地铁口",
	private String xiangqingline6; //"服务您的司机",
	private String xiangqingline7; //"朱清请",
	private String xiangqingline8; //"代驾31次 驾龄11年 籍贯江苏",
	private String xiangqingline9;  //"喜欢这个师傅就给他打5分吧",
	private String xiangqingtouxiang; //"http://t.k76.net/pic/t/76/76.jpg"
	private String state;//订单状态
	
	 
	
	public String getDingdanhao() {
		return dingdanhao;
	}

	public void setDingdanhao(String dingdanhao) {
		this.dingdanhao = dingdanhao;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}
	public String getPingjiatext() {
		return pingjiatext;
	}

	public void setPingjiatext(String pingjiatext) {
		this.pingjiatext = pingjiatext;
	}

	public String getPingjiaxingji() {
		return pingjiaxingji;
	}

	public void setPingjiaxingji(String pingjiaxingji) {
		this.pingjiaxingji = pingjiaxingji;
	}

	public String getRatingNum() {
		return ratingNum;
	}

	public void setRatingNum(String ratingNum) {
		this.ratingNum = ratingNum;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getIs_comment() {
		return is_comment;
	}

	public void setIs_comment(String is_comment) {
		this.is_comment = is_comment;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getYuyuerendianhua() {
		return yuyuerendianhua;
	}

	public void setYuyuerendianhua(String yuyuerendianhua) {
		this.yuyuerendianhua = yuyuerendianhua;
	}

	public String getChufadidian() {
		return chufadidian;
	}

	public void setChufadidian(String chufadidian) {
		this.chufadidian = chufadidian;
	}

	public String getMudidi() {
		return mudidi;
	}

	public void setMudidi(String mudidi) {
		this.mudidi = mudidi;
	}

	public String getYuyuechufashijia() {
		return yuyuechufashijia;
	}

	public void setYuyuechufashijia(String yuyuechufashijia) {
		this.yuyuechufashijia = yuyuechufashijia;
	}

	public String getDingdanid() {
		return dingdanid;
	}

	public void setDingdanid(String dingdanid) {
		this.dingdanid = dingdanid;
	}

	public String getJine() {
		return jine;
	}

	public void setJine(String jine) {
		this.jine = jine;
	}

	public String getGonghao() {
		return gonghao;
	}

	public void setGonghao(String gonghao) {
		this.gonghao = gonghao;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getIfpingjia() {
		return ifpingjia;
	}

	public void setIfpingjia(String ifpingjia) {
		this.ifpingjia = ifpingjia;
	}

	public String getXinji() {
		return xinji;
	}

	public void setXinji(String xinji) {
		this.xinji = xinji;
	}

	public String getXiangqingtitle() {
		return xiangqingtitle;
	}

	public void setXiangqingtitle(String xiangqingtitle) {
		this.xiangqingtitle = xiangqingtitle;
	}

	public String getXiangqingline1() {
		return xiangqingline1;
	}

	public void setXiangqingline1(String xiangqingline1) {
		this.xiangqingline1 = xiangqingline1;
	}

	public String getXiangqingline2() {
		return xiangqingline2;
	}

	public void setXiangqingline2(String xiangqingline2) {
		this.xiangqingline2 = xiangqingline2;
	}

	public String getXiangqingjien() {
		return xiangqingjien;
	}

	public void setXiangqingjien(String xiangqingjien) {
		this.xiangqingjien = xiangqingjien;
	}

	public String getXiangqingline3() {
		return xiangqingline3;
	}

	public void setXiangqingline3(String xiangqingline3) {
		this.xiangqingline3 = xiangqingline3;
	}

	public String getXiangqingline4() {
		return xiangqingline4;
	}

	public void setXiangqingline4(String xiangqingline4) {
		this.xiangqingline4 = xiangqingline4;
	}

	public String getXiangqingline5() {
		return xiangqingline5;
	}

	public void setXiangqingline5(String xiangqingline5) {
		this.xiangqingline5 = xiangqingline5;
	}

	public String getXiangqingline6() {
		return xiangqingline6;
	}

	public void setXiangqingline6(String xiangqingline6) {
		this.xiangqingline6 = xiangqingline6;
	}

	public String getXiangqingline7() {
		return xiangqingline7;
	}

	public void setXiangqingline7(String xiangqingline7) {
		this.xiangqingline7 = xiangqingline7;
	}

	public String getXiangqingline8() {
		return xiangqingline8;
	}

	public void setXiangqingline8(String xiangqingline8) {
		this.xiangqingline8 = xiangqingline8;
	}

	public String getXiangqingline9() {
		return xiangqingline9;
	}

	public void setXiangqingline9(String xiangqingline9) {
		this.xiangqingline9 = xiangqingline9;
	}

	public String getXiangqingtouxiang() {
		return xiangqingtouxiang;
	}

	public void setXiangqingtouxiang(String xiangqingtouxiang) {
		this.xiangqingtouxiang = xiangqingtouxiang;
	}

}