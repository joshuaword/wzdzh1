package com.dajia.util;

import java.util.Comparator;

import com.dajia.Bean.CityType;


public class PinyinComparator implements Comparator<CityType> {

	public int compare(CityType o1, CityType o2) {
		if (o1.getZimu().equals("@")
				|| o2.getZimu().equals("#")) {
			return -1;
		} else if (o1.getZimu().equals("#")
				|| o2.getZimu().equals("@")) {
			return 1;
		} else {
			return o1.getZimu().compareTo(o2.getZimu());
		}
	}

}
