package com.samknows.tests.util;

import java.util.Comparator;

import com.samknows.tests.interfaces.ITest;

public class TestComparator implements Comparator<ITest> {

	@Override
	public int compare(ITest lhs, ITest rhs) {
		return lhs.getType().ordinal() - (int)rhs.getType().ordinal();
	}
}
//DOWNLOAD, C_UPLOAD, S_UPLOAD, LATENCY, CLOSEST_TARGET, PROXY_DETECTOR, TRIGGER 