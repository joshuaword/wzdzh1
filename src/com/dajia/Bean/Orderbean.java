package com.dajia.Bean;

import java.io.Serializable;
import java.util.List;

public class Orderbean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jiagebiao;
	private String color;
	private String  fadanfang;
	private String daijienum;// 0, //待接订单数，为0表示没有订单，如果为1，表示有新订单
	private String id; // 订单ID号
	private String state;// 订单执行状态：未指派，执行中，接单，到达，出发，完成
	private String danhao;// 订单号，如20141209， 与订单ID号不同写法供人类看的
	private String shijian;// 当前时间减派单时间，历时多少分钟
	private String yuyuechufashijia;// 预约出发时间
	private String chufadidian;// 出发地点
	private String laiyuan; // 订单来源
	private String jine;// 订单金额
	private String daijialeixing;// 代驾类型
	private String yuyuerenxinming;
	private String yuyuerendianhua;
	private String paidanshijian;
	private String mudidi;
	private String jiezhangfangshi;
	private String createtime;
	private String kehutelphone;
	private String lishinum;
	private String ifyouhuiquan;
	private String youhuiquanname;
	private String youhuiquanjiage;
	private String jijin;
	private String jifen;
	private String yue;
	// 创建订单：
	private String ret;
	private String msg;
	private String img;
	private String yijinzhifu;
	private String dingdanid;

	private String bianhao;
	private String drivermoney;

	private String appchufa;
	private String appdaoda;
	private String juli;
	private String ifbusy;

	private String qingjia;
	private String denghoufei;
	private String gonglifei;
	private String qibujia;
	private String line11;
	private String line12;
	private String line13;
	private String line21;
	private String line22;
	private String line23;
	private String line31;
	private String line32;
	private String line33;
	private String line41;
	private String line42;
	private String line43;
	private String line51;
	private String line52, line6, line7, ifxiche, ifpaotui;
	private String appjiedan;
	private String gujigonglishu;
	private String chepaihao;
	private String chexing;
	private String danjuhao;
	private String gonglijia;
	private String fanchenfei;
	private String shoufeijine;
	private String fukuankahao;
	private String fukuankamima;
	private String beizhu;
	private String zhanghuyue;

	private String title, zhongtudenghoufei, zhongtudenghou, zekou, yingshoujine, youhuijine;
	private String info1;
	private String info2;
	private String begintime;
	private String button;
	private String buttonpost;
	private List<OrderInfo> dingdaninfo;
	private String wheretojump;

	private String strzhanghuyue, zhanghuyuenum, jinridan, jinriyuan, zaixian, strzhuangtai, yijiedingdannum;
    private String ifchexing, ifchepaihao, ifmudidi;
    
    private Double  startx;
    private Double  starty;
    private Double  endx;
    private Double  endy;
    
    private String showname;
    private Double x;
    private Double y;
    private String  xingji;
    private String telphone;
    private String touxiang;
    private String beijintu;
    
    private String  driverid;
    private String  ifpingche;
    private String str;
    private String thetime;
    
    
    
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getThetime() {
		return thetime;
	}

	public void setThetime(String thetime) {
		this.thetime = thetime;
	}

	public String getIfyouhuiquan() {
		return ifyouhuiquan;
	}

	public void setIfyouhuiquan(String ifyouhuiquan) {
		this.ifyouhuiquan = ifyouhuiquan;
	}

	public String getYouhuiquanname() {
		return youhuiquanname;
	}

	public void setYouhuiquanname(String youhuiquanname) {
		this.youhuiquanname = youhuiquanname;
	}

	public String getYouhuiquanjiage() {
		return youhuiquanjiage;
	}

	public void setYouhuiquanjiage(String youhuiquanjiage) {
		this.youhuiquanjiage = youhuiquanjiage;
	}

	public String getXingji() {
		return xingji;
	}

	public void setXingji(String xingji) {
		this.xingji = xingji;
	}

	public String getDriverid() {
		return driverid;
	}

	public void setDriverid(String driverid) {
		this.driverid = driverid;
	}

	public String getIfpingche() {
		return ifpingche;
	}

	public void setIfpingche(String ifpingche) {
		this.ifpingche = ifpingche;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getTouxiang() {
		return touxiang;
	}

	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}

	public String getBeijintu() {
		return beijintu;
	}

	public void setBeijintu(String beijintu) {
		this.beijintu = beijintu;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getJiagebiao() {
		return jiagebiao;
	}

	public void setJiagebiao(String jiagebiao) {
		this.jiagebiao = jiagebiao;
	}

	public String getFadanfang() {
		return fadanfang;
	}

	public void setFadanfang(String fadanfang) {
		this.fadanfang = fadanfang;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getYijinzhifu() {
		return yijinzhifu;
	}

	public void setYijinzhifu(String yijinzhifu) {
		this.yijinzhifu = yijinzhifu;
	}

	public Double getStartx() {
		return startx;
	}

	public void setStartx(Double startx) {
		this.startx = startx;
	}

	public Double getStarty() {
		return starty;
	}

	public void setStarty(Double starty) {
		this.starty = starty;
	}

	public Double getEndx() {
		return endx;
	}

	public void setEndx(Double endx) {
		this.endx = endx;
	}

	public Double getEndy() {
		return endy;
	}

	public void setEndy(Double endy) {
		this.endy = endy;
	}

	public String getIfchexing() {
		return ifchexing;
	}

	public void setIfchexing(String ifchexing) {
		this.ifchexing = ifchexing;
	}
	
	public String getIfchepaihao() {
		return ifchepaihao;
	}

	public void setIfchepaihao(String ifchepaihao) {
		this.ifchepaihao = ifchepaihao;
	}
	
	public String getIfmudidi() {
		return ifmudidi;
	}

	public void setIfmudidi(String ifmudidi) {
		this.ifmudidi = ifmudidi;
	}
	

	public String getZekou() {
		return zekou;
	}

	public void setZekou(String zekou) {
		this.zekou = zekou;
	}
	
	public String getYouhuijine() {
		return youhuijine;
	}

	public void setYouhuijine(String youhuijine) {
		this.youhuijine = youhuijine;
	}
	
	public String getYingshoujine() {
		return yingshoujine;
	}

	public void setYingshoujine(String yingshoujine) {
		this.yingshoujine = yingshoujine;
	}
	
	public String getZhongtudenghoufei() {
		return zhongtudenghoufei;
	}

	public void setZhongtudenghoufei(String zhongtudenghoufei) {
		this.zhongtudenghoufei = zhongtudenghoufei;
	}
	
	public String getZhongtudenghou() {
		return zhongtudenghou;
	}

	public void setZhongtudenghou(String zhongtudenghou) {
		this.zhongtudenghou = zhongtudenghou;
	}
	
	public String getKehutelphone() {
		return kehutelphone;
	}

	public void setKehutelphone(String kehutelphone) {
		this.kehutelphone = kehutelphone;
	}

	public List<OrderInfo> getDingdaninfo() {
		return dingdaninfo;
	}

	public void setDingdaninfo(List<OrderInfo> dingdaninfo) {
		this.dingdaninfo = dingdaninfo;
	}

	public String getWheretojump() {
		return wheretojump;
	}

	public void setWheretojump(String wheretojump) {
		this.wheretojump = wheretojump;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public String getButtonpost() {
		return buttonpost;
	}

	public void setButtonpost(String buttonpost) {
		this.buttonpost = buttonpost;
	}

	public String getZhanghuyue() {
		return zhanghuyue;
	}

	public void setZhanghuyue(String zhanghuyue) {
		this.zhanghuyue = zhanghuyue;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getGujigonglishu() {
		return gujigonglishu;
	}

	public void setGujigonglishu(String gujigonglishu) {
		this.gujigonglishu = gujigonglishu;
	}

	public String getChepaihao() {
		return chepaihao;
	}

	public void setChepaihao(String chepaihao) {
		this.chepaihao = chepaihao;
	}

	public String getChexing() {
		return chexing;
	}

	public void setChexing(String chexing) {
		this.chexing = chexing;
	}

	public String getDanjuhao() {
		return danjuhao;
	}

	public void setDanjuhao(String danjuhao) {
		this.danjuhao = danjuhao;
	}

	public String getGonglijia() {
		return gonglijia;
	}

	public void setGonglijia(String gonglijia) {
		this.gonglijia = gonglijia;
	}

	public String getFanchenfei() {
		return fanchenfei;
	}

	public void setFanchenfei(String fanchenfei) {
		this.fanchenfei = fanchenfei;
	}

	public String getShoufeijine() {
		return shoufeijine;
	}

	public void setShoufeijine(String shoufeijine) {
		this.shoufeijine = shoufeijine;
	}

	public String getFukuankahao() {
		return fukuankahao;
	}

	public void setFukuankahao(String fukuankahao) {
		this.fukuankahao = fukuankahao;
	}

	public String getFukuankamima() {
		return fukuankamima;
	}

	public void setFukuankamima(String fukuankamima) {
		this.fukuankamima = fukuankamima;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public String getAppjiedan() {
		return appjiedan;
	}

	public void setAppjiedan(String appjiedan) {
		this.appjiedan = appjiedan;
	}

	public String getLine11() {
		return line11;
	}

	public void setLine11(String line11) {
		this.line11 = line11;
	}

	public String getLine12() {
		return line12;
	}

	public void setLine12(String line12) {
		this.line12 = line12;
	}

	public String getLine13() {
		return line13;
	}

	public void setLine13(String line13) {
		this.line13 = line13;
	}

	public String getLine21() {
		return line21;
	}

	public void setLine21(String line21) {
		this.line21 = line21;
	}

	public String getLine22() {
		return line22;
	}

	public void setLine22(String line22) {
		this.line22 = line22;
	}

	public String getLine23() {
		return line23;
	}

	public void setLine23(String line23) {
		this.line23 = line23;
	}

	public String getLine31() {
		return line31;
	}

	public void setLine31(String line31) {
		this.line31 = line31;
	}

	public String getLine32() {
		return line32;
	}

	public void setLine32(String line32) {
		this.line32 = line32;
	}

	public String getLine33() {
		return line33;
	}

	public void setLine33(String line33) {
		this.line33 = line33;
	}

	public String getLine41() {
		return line41;
	}

	public void setLine41(String line41) {
		this.line41 = line41;
	}

	public String getLine42() {
		return line42;
	}

	public void setLine42(String line42) {
		this.line42 = line42;
	}

	public String getLine43() {
		return line43;
	}

	public void setLine43(String line43) {
		this.line43 = line43;
	}

	public String getLine51() {
		return line51;
	}

	public void setLine51(String line51) {
		this.line51 = line51;
	}

	public String getLine52() {
		return line52;
	}

	public void setLine52(String line52) {
		this.line52 = line52;
	}
	
	public String getLine6() {
		return line6;
	}

	public void setLine6(String line6) {
		this.line6 = line6;
	}
	
	public String getLine7() {
		return line7;
	}

	public void setLine7(String line7) {
		this.line7 = line7;
	}
	
	public String getIfxiche() {
		return ifxiche;
	}

	public void setIfxiche(String ifxiche) {
		this.ifxiche = ifxiche;
	}
	
	public String getIfpaotui() {
		return ifpaotui;
	}

	public void setIfpaotui(String ifpaotui) {
		this.ifpaotui = ifpaotui;
	}
	

	public String getQibujia() {
		return qibujia;
	}

	public void setQibujia(String qibujia) {
		this.qibujia = qibujia;
	}

	public String getDenghoufei() {
		return denghoufei;
	}

	public void setDenghoufei(String denghoufei) {
		this.denghoufei = denghoufei;
	}

	public String getGonglifei() {
		return gonglifei;
	}

	public void setGonglifei(String gonglifei) {
		this.gonglifei = gonglifei;
	}

	public String getJijin() {
		return jijin;
	}

	public void setJijin(String jijin) {
		this.jijin = jijin;
	}

	public String getJifen() {
		return jifen;
	}

	public void setJifen(String jifen) {
		this.jifen = jifen;
	}

	public String getYue() {
		return yue;
	}

	public void setYue(String yue) {
		this.yue = yue;
	}

	public String getQingjia() {
		return qingjia;
	}

	public void setQingjia(String qingjia) {
		this.qingjia = qingjia;
	}

	public String getIfbusy() {
		return ifbusy;
	}

	public void setIfbusy(String ifbusy) {
		this.ifbusy = ifbusy;
	}

	public String getLishinum() {
		return lishinum;
	}

	public void setLishinum(String lishinum) {
		this.lishinum = lishinum;
	}
	
	
	public String getYijiedingdannum() {
		return yijiedingdannum;
	}

	public void setYijiedingdannum(String yijiedingdannum) {
		this.yijiedingdannum = yijiedingdannum;
	}
	
	public String getStrzhuangtai() {
		return strzhuangtai;
	}

	public void setStrzhuangtai(String strzhuangtai) {
		this.strzhuangtai = strzhuangtai;
	}
	
	public String getZaixian() {
		return zaixian;
	}

	public void setZaixian(String zaixian) {
		this.zaixian = zaixian;
	}
	
	public String getStrzhanghuyue() {
		return strzhanghuyue;
	}

	public void setStrzhanghuyue(String strzhanghuyue) {
		this.strzhanghuyue = strzhanghuyue;
	}
	
	public String getZhanghuyuenum() {
		return zhanghuyuenum;
	}

	public void setZhanghuyuenum(String zhanghuyuenum) {
		this.zhanghuyuenum = zhanghuyuenum;
	}
	
	public String getJinridan() {
		return jinridan;
	}

	public void setJinridan(String jinridan) {
		this.jinridan = jinridan;
	}
	
	public String getJinriyuan() {
		return jinriyuan;
	}

	public void setJinriyuan(String jinriyuan) {
		this.jinriyuan = jinriyuan;
	}
	

	public String getAppchufa() {
		return appchufa;
	}

	public void setAppchufa(String appchufa) {
		this.appchufa = appchufa;
	}

	public String getAppdaoda() {
		return appdaoda;
	}

	public void setAppdaoda(String appdaoda) {
		this.appdaoda = appdaoda;
	}

	public String getJuli() {
		return juli;
	}

	public void setJuli(String juli) {
		this.juli = juli;
	}

	public String getJiezhangfangshi() {
		return jiezhangfangshi;
	}

	public void setJiezhangfangshi(String jiezhangfangshi) {
		this.jiezhangfangshi = jiezhangfangshi;
	}

	public String getPaidanshijian() {
		return paidanshijian;
	}

	public void setPaidanshijian(String paidanshijian) {
		this.paidanshijian = paidanshijian;
	}

	public String getYuyuerendianhua() {
		return yuyuerendianhua;
	}

	public void setYuyuerendianhua(String yuyuerendianhua) {
		this.yuyuerendianhua = yuyuerendianhua;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDrivermoney() {
		return drivermoney;
	}

	public void setDrivermoney(String drivermoney) {
		this.drivermoney = drivermoney;
	}

	public String getYuyuerenxinming() {
		return yuyuerenxinming;
	}

	public void setYuyuerenxinming(String yuyuerenxinming) {
		this.yuyuerenxinming = yuyuerenxinming;
	}

	public String getMudidi() {
		return mudidi;
	}

	public void setMudidi(String mudidi) {
		this.mudidi = mudidi;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getDingdanid() {
		return dingdanid;
	}

	public void setDingdanid(String dingdanid) {
		this.dingdanid = dingdanid;
	}

	public String getBianhao() {
		return bianhao;
	}

	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}

	public String getDaijienum() {
		return daijienum;
	}

	public void setDaijienum(String daijienum) {
		this.daijienum = daijienum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDanhao() {
		return danhao;
	}

	public void setDanhao(String danhao) {
		this.danhao = danhao;
	}

	public String getShijian() {
		return shijian;
	}

	public void setShijian(String shijian) {
		this.shijian = shijian;
	}

	public String getYuyuechufashijia() {
		return yuyuechufashijia;
	}

	public void setYuyuechufashijia(String yuyuechufashijia) {
		this.yuyuechufashijia = yuyuechufashijia;
	}

	public String getChufadidian() {
		return chufadidian;
	}

	public void setChufadidian(String chufadidian) {
		this.chufadidian = chufadidian;
	}

	public String getLaiyuan() {
		return laiyuan;
	}

	public void setLaiyuan(String laiyuan) {
		this.laiyuan = laiyuan;
	}

	public String getJine() {
		return jine;
	}

	public void setJine(String jine) {
		this.jine = jine;
	}

	public String getDaijialeixing() {
		return daijialeixing;
	}

	public void setDaijialeixing(String daijialeixing) {
		this.daijialeixing = daijialeixing;
	}

}
