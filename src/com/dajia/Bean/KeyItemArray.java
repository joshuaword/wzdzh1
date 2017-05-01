package com.dajia.Bean;

import com.dajia.util.SharedPreferencesDTO;

/**
 * 
 * @author fucheng
 * 2015年2月9日
 */
public class KeyItemArray extends SharedPreferencesDTO<KeyItemArray> {
	
	private static final long serialVersionUID = 968265836389017986L;
	private String array;
	
	public KeyItemArray(String array) {
		super();
		this.array = array;
	}

	public KeyItemArray() {
		super();
	}

	public String getArray() {
		return array;
	}

	public void setArray(String array) {
		this.array = array;
	}

	@Override
	public boolean isSame(KeyItemArray o) {
		String	bId = o.getArray();
		String	aId = this.getArray();
		return aId.equals(bId);
	}

}
