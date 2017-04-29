package com.dajia.Bean;

import java.io.Serializable;
import java.util.List;

public class GuiJiInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Orderbean jiedan;
	
	private Orderbean chufa;
	
	private Orderbean wancheng;
	private List<Orderbean>befordaijia;
	private List<Orderbean>begindaijia ;
	public Orderbean getJiedan() {
		return jiedan;
	}
	public void setJiedan(Orderbean jiedan) {
		this.jiedan = jiedan;
	}
	public Orderbean getChufa() {
		return chufa;
	}
	public void setChufa(Orderbean chufa) {
		this.chufa = chufa;
	}
	public Orderbean getWancheng() {
		return wancheng;
	}
	public void setWancheng(Orderbean wancheng) {
		this.wancheng = wancheng;
	}
	public List<Orderbean> getBefordaijia() {
		return befordaijia;
	}
	public void setBefordaijia(List<Orderbean> befordaijia) {
		this.befordaijia = befordaijia;
	}
	public List<Orderbean> getBegindaijia() {
		return begindaijia;
	}
	public void setBegindaijia(List<Orderbean> begindaijia) {
		this.begindaijia = begindaijia;
	}
	
	
}
