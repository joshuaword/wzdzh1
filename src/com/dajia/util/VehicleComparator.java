package com.dajia.util;

import java.util.Comparator;
import com.dajia.Bean.VehicleType;

public class VehicleComparator implements Comparator<VehicleType> {
	@Override
	public int compare(VehicleType lhs, VehicleType rhs) {
		return lhs.getZimu().compareTo(rhs.getZimu());
	}

}
