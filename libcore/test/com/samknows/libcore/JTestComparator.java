package com.samknows.libcore;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.samknows.tests.*;
import com.samknows.tests.interfaces.ETestType;
import com.samknows.tests.interfaces.ITest;
import com.samknows.tests.util.TestComparator;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import edu.emory.mathcs.backport.java.util.Collections;

@RunWith(RobolectricTestRunner.class)
public class JTestComparator {

	@Test
	public final void testCompare() {
		List<Param> params = new ArrayList<Param>(); 
		
		List<ITest> tests = new ArrayList<ITest>(); 
		
		tests.add(new Download(params));
		tests.add(new ClosestTarget(params));
		tests.add(new ActiveServerUpload(params));
		tests.add(new PassiveServerUpload(params));
		//tests.add(new ProxyDetector(params));
		//tests.add(new Latency(params));
		//tests.add(new Trigger(params));
		
		assertTrue(tests.get(1).getType() == ETestType.CLOSEST_TARGET);
		assertTrue(tests.get(0).getType() == ETestType.DOWNLOAD);
		assertTrue(tests.get(3).getType() == ETestType.C_UPLOAD);
		assertTrue(tests.get(2).getType() == ETestType.S_UPLOAD);
		
		
		Collections.sort(tests, new TestComparator());
	
		assertTrue(tests.get(0).getType() == ETestType.CLOSEST_TARGET);
		assertTrue(tests.get(1).getType() == ETestType.DOWNLOAD);
		assertTrue(tests.get(2).getType() == ETestType.C_UPLOAD);
		assertTrue(tests.get(3).getType() == ETestType.S_UPLOAD);
		//assertTrue(tests.get(0).getType() == ETestType.CLOSEST_TARGET);
		//assertTrue(tests.get(0).getType() == ETestType.CLOSEST_TARGET);
		//assertTrue(tests.get(0).getType() == ETestType.CLOSEST_TARGET);
		
	}

}
//DOWNLOAD, C_UPLOAD, S_UPLOAD, LATENCY, CLOSEST_TARGET, PROXY_DETECTOR, TRIGGER 