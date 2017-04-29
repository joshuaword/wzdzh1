package com.dajia.Bean;

import java.util.List;

public class AddressBean {
	private String ret;
	private List<Address> address;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "AddressBean [ret=" + ret + ", address=" + address + "]";
	}

	public static class Address {
		private String addressid;
		private String userid;
		private String x;
		private String y;
		private String address;
		public String getAddressid() {
			return addressid;
		}
		public void setAddressid(String addressid) {
			this.addressid = addressid;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getX() {
			return x;
		}
		public void setX(String x) {
			this.x = x;
		}
		public String getY() {
			return y;
		}
		public void setY(String y) {
			this.y = y;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		@Override
		public String toString() {
			return "Address [addressid=" + addressid + ", userid=" + userid
					+ ", x=" + x + ", y=" + y + ", address=" + address + "]";
		}
		
	}
}
