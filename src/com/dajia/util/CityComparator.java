package com.dajia.util;

import java.util.Comparator;
import com.dajia.Bean.CityType;


public class CityComparator implements Comparator<CityType> {
	@Override
	public int compare(CityType lhs, CityType rhs) {
		return lhs.getZimu().compareTo(rhs.getZimu());
	}

}
