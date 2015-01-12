package com.samknows.libcore;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;




import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.samknows.tests.Latency;
import com.samknows.tests.Param;


@RunWith(RobolectricTestRunner.class)
public class JLatency {
	
	/*	<param name="target" value="$closest"/>
	<param name="port" value="6000"/>
	<param name="interPacketTime" value="500000"/>
	<param name="delayTimeout" value="2000000"/>
	<param name="numberOfPackets" value="60"/>
	<param name="percentile" value="100"/>
	<param name="maxTime" value="30000000"/>
*/	

	List<Param> params = new ArrayList<Param>();

	public JLatency(){
		params.add(new Param("target", "n1-the1.samknows.com"));
		params.add(new Param("port", "6000"));
		params.add(new Param("intrPacketTime", "500000"));
		params.add(new Param("delayTimeout", "2000000"));
		params.add(new Param("numberOfPackets", "60"));
		params.add(new Param("percentile", "100"));
		params.add(new Param("maxTime", "30000000"));
		
		latency = new Latency(params);
	}
	
	
	Latency latency = null;

	@Test
	public final void testGetStringID() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIsSuccessful() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetHumanReadableResult() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetResults() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIsReady() {
		assertTrue(latency.isReady());
	}

	@Test
	public final void testGetNetUsage() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetProgress() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPacketSize() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLatencyListOfParam() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetInfo() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTarget() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetValue() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetDimension() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetType() {
		fail("Not yet implemented"); // TODO
	}

}
