package com.dajia.Bean;

import java.io.Serializable;

public class VehicleType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6255202344575878081L;
	
	String zimu;
	String name;
	String logo;

	public String getZimu() {
		return zimu;
	}

	public void setZimu(String zimu) {
		this.zimu = zimu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "VehicleType [zimu=" + zimu + ", name=" + name + ", logo="
				+ logo + "]";
	}
}
