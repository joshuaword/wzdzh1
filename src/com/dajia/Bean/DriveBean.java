package com.dajia.Bean;

import java.util.List;

public class DriveBean {
	private String ret, dingdanid, msg,dingdanhao;

	private List<DriverInfo> list;

	public String getDingdanid() {
		return dingdanid;
	}

	public void setDingdanid(String dingdanid) {
		this.dingdanid = dingdanid;
	}

	public String getDingdanhao() {
		return dingdanhao;
	}

	public void setDingdanhao(String dingdanhao) {
		this.dingdanhao = dingdanhao;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<DriverInfo> getList() {
		return list;
	}

	public void setList(List<DriverInfo> list) {
		this.list = list;
	}

}
