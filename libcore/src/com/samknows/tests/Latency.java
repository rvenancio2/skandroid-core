package com.samknows.tests;

//import android.annotation.SuppressLint;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;


import com.samknows.libcore.SKLogger;
import com.samknows.measurement.SKApplication;
import com.samknows.measurement.util.SKDateFormat;
import com.samknows.tests.formats.JsonData;
import com.samknows.tests.interfaces.EDimension;
import com.samknows.tests.interfaces.ETestType;
import com.samknows.tests.util.UdpDatagram;

public class Latency extends Test {
	
	public static final String STRING_ID = "JUDPLATENCY";
	public static final int STATUSFIELD = 2;
	public static final int TARGETFIELD = 3;
	public static final int IPTARGETFIELD = 4;
	public static final int AVERAGEFIELD = 5;
	private static final String LATENCYRUN = "Running latency and loss tests";
	private static final String LATENCYDONE = "Latency and loss tests completed";
	
	/*
	 * constants for creating a latency test
	 */
	private static final String NUMBEROFPACKETS = "numberOfPackets";
	private static final String DELAYTIMEOUT = "delayTimeout";
	private static final String INTERPACKETTIME = "interPacketTime";
	private static final String PERCENTILE = "percentile";
	private static final String MAXTIME = "maxTime";


	
	// Used internally ... and externally, by the HttpTest fallback for ClosestTarget testing.
/*	static void sCreateAndPushLatencyResultNanoseconds(BlockingQueue<Result> bq_results, String inTarget, double inRttNanoseconds) {
		if (bq_results != null) {
			// Pass-in the value in nanoseconds
     		Result r = new Result(inTarget, (long)inRttNanoseconds);
			try {
				bq_results.put(r);
			} catch (InterruptedException e) {
				SKLogger.sAssert(Latency.class, false);
			}
		}
	}*/
	

	// Used internally ...
	private void setLatencyValueNanoseconds(long inRttNanoseconds) {
		try {
				bq_results.put(inRttNanoseconds);
			} catch (InterruptedException e) {
				SKLogger.sAssert(Latency.class, false);
			}
	}

	@SuppressWarnings("serial")
	private class PacketTimeOutException extends Exception{
		
	}
			
	public Latency(List<Param> params){
		setParams(params);
	}

	public String getStringID() {
		return STRING_ID;
	}
	
	public Latency(String server, int port, int numdatagrams,
			int interPacketTime, int delayTimeout) {
		target = server;
		this.port = port;
		this.numdatagrams = numdatagrams;
		results = new long[numdatagrams];
		this.interPacketTime = interPacketTime * 1000; // nanoSeconds
		this.delayTimeout = delayTimeout / 1000; // mSeconds
	}

	@Override
	public int getNetUsage() {
		return UdpDatagram.getSize() * (sentPackets + recvPackets);
	}

	// @SuppressLint("NewApi")
	@Override
	public boolean isReady() {
		if (target.length() == 0) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		if (port == 0) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		if (numdatagrams == 0 || results == null) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		if (delayTimeout == 0) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		if (interPacketTime == 0) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		if (percentile < 0 || percentile > 100) {
			SKLogger.sAssert(getClass(),  false);
			return false;
		}
		
		return true;
	}

	@Override
	public void execute() {
		run();
	}

	@Override
	public boolean isSuccessful() {
		return testStatus.equals("OK");
	}

	public String getInfo() {
		return infoString;
	}

	@Override
	public String getHumanReadableResult() {
		String ret = "";
		if (testStatus.equals("FAIL")) {
			ret = String.format("The latency test has failed.");
		} else {
			// added cast otherwise it will always be 0 or 1;
			int packetLoss = (int) (100 * (((float) sentPackets - recvPackets) / sentPackets)); 
			
			int jitter = (int) ((averageNanoseconds - minimumNanoseconds) / 1000000);
			ret = String.format(Locale.UK,
					"Latency is %d ms. Packet loss is %d %%. Jitter is %d ms",
					(int) (averageNanoseconds / 1000000), packetLoss, jitter);
		}
		return ret;
	}

	
	private String[] formValuesArr(){
		String[] values = new String[3];
		values[0] = "" + ((int) (averageNanoseconds / 1000000));
		values[1] = "" + ((int) (100 * (((float) sentPackets - recvPackets) / sentPackets)));
		values[2] = "" + ((int) ((averageNanoseconds - minimumNanoseconds) / 1000000));

		return values;
	}
	
/*	@Override
	public String getResultsAsString(){							 New Human readable implementation 
		if (testStatus.equals("FAIL")){
			return "";
		}else{
			String[] values = formValuesArr();			
			return String.format(Locale.UK, values[0], values[1], values[2]);
		}		
	}*/
	/*@Override
	public String getResults(String locale){			 New Human readable implementation 
		if (testStatus.equals("FAIL")){
			return locale;
		}else{
			String[] values = formValuesArr();			
			return String.format(locale, values[0], values[1], values[2]);
		}		
	}*/
	@Override
	public HashMap<String, String> getResults(){
		HashMap<String, String> ret = new HashMap<String, String>();
		if (!testStatus.equals("FAIL")) {
			String[] values = formValuesArr();
			ret.put("latency", values[0]);
			ret.put("packetloss", values[1]);
			ret.put("jitter", values[2]);
		}
		return ret;		
	}
		

	private void output() {
		Map<String, Object> output = new HashMap<String, Object>();
		ArrayList<String> o = new ArrayList<String>();
		// test string id
		o.add(STRING_ID);
		output.put(JsonData.JSON_TYPE, STRING_ID);
		// time
		Long time_stamp = unixTimeStamp();
		o.add(Long.toString(time_stamp));
		output.put(JsonData.JSON_TIMESTAMP, time_stamp);
		output.put(JsonData.JSON_DATETIME, SKDateFormat.sGetDateAsIso8601String(new java.util.Date(time_stamp*1000)));
		// test status
		o.add(testStatus);
		output.put(JsonData.JSON_SUCCESS, isSuccessful());
		// target
		o.add(target);
		output.put(JsonData.JSON_TARGET, target);
		// target ipaddress
		o.add(ipAddress);
		output.put(JsonData.JSON_TARGET_IPADDRESS, ipAddress);
		// average
		o.add(Long.toString(((long)(averageNanoseconds / 1000))));
		output.put(JsonData.JSON_RTT_AVG, (long) (averageNanoseconds / 1000));
		// minimum
		o.add(Long.toString(minimumNanoseconds / 1000));
		output.put(JsonData.JSON_RTT_MIN, minimumNanoseconds / 1000);
		// maximum
		o.add(Long.toString(maximumNanoseconds / 1000));
		output.put(JsonData.JSON_RTT_MAX, maximumNanoseconds /1000);
		// standard deviation
		o.add(Long.toString((long) (stddeviationNanoseconds / 1000)));
		
		output.put(JsonData.JSON_RTT_STDDEV,(long) (stddeviationNanoseconds / 1000));
		// recvPackets
		o.add(Integer.toString(recvPackets));
		output.put(JsonData.JSON_RECEIVED_PACKETS, recvPackets);
		// lost packets
		o.add(Integer.toString(sentPackets - recvPackets));
		output.put(JsonData.JSON_LOST_PACKETS, sentPackets - recvPackets);
		setOutput(o.toArray(new String[1]));
		setJSONResult(output);
	}

	@Override
	public void run() {
		start();
		
		sentPackets=0;															/* set to zero internal variables in case the same test object is executed severals times */
		recvPackets=0;
		startTimeNanonseconds = System.nanoTime();								/* Memorise start time  */
		
		DatagramSocket socket = null;											/* Initialise socket */
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(delayTimeout);
		} catch (SocketException e) {
			failure();
			return;
		}
		
//		try {
//			int sendBufferSizeBytes = socket.getSendBufferSize();
//			Log.d(getClass().getName(), "LatencyTest: sendBufferSizeBytes=" + sendBufferSizeBytes);
//			int receiveBufferSizeBytes = socket.getReceiveBufferSize();
//			Log.d(getClass().getName(), "LatencyTest: receiveBufferSizeBytes=" + receiveBufferSizeBytes);
//		} catch (SocketException e1) {
//			SKLogger.sAssert(getClass(),  false);
//		}

		InetAddress address = null;
		try {
			address = InetAddress.getByName(target);							/* Try to setup socket. If socket fails report FAIL output */
			ipAddress = address.getHostAddress();
		} catch (UnknownHostException e) {
			failure();
     		socket.close();
     		socket = null;
			return;
		}
		for (int i = 0; i < numdatagrams; ++i) {								/* For required number of datagrams - run test */
			
			if ((maxExecutionTimeNanoseconds > 0)								/* If out of break cycle */						
					&& (System.nanoTime() - startTimeNanonseconds > 
							maxExecutionTimeNanoseconds)) {
				break;
			}

			UdpDatagram data = new UdpDatagram(i);								/* Prepare new datagram #i */
			byte[] buf = data.byteArray();										/* Get raw datagram buffer */
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			long answerTime = 0;

			// It isn't the current time as in the original but a random value.
			// Let's hope nobody changes the server to make this important...???
			long time = System.nanoTime();												/* Get up time and   */
			//data.setTime(time);															/* add it to datagram */

			try {
				socket.send(packet);
				sentPackets++;
			} catch (IOException e) {
				continue;
			}

			try {
				UdpDatagram answer = null;												/* Declare datagram */					
				do {
					//Checks for the current time and 
					//set the SoTimeout accordingly 
					//because of duplicate packets or 
					//packets received after delayTimeout
					
					long now = System.nanoTime();										/* Set up socket block limit timeout */
					long timeout = delayTimeout - (now - time)/1000000;
					if(timeout<0){
						throw new PacketTimeOutException();
					}
					socket.setSoTimeout((int) timeout);
					socket.receive(packet);
					
					answer = new UdpDatagram(buf);										/* Create datagram from packet */
				
				} while (!answer.isReady(i));											/* Check if this is really datagram with ID i */
				answerTime = System.nanoTime();											/* Receive time */
				recvPackets++;															/* Increment packets counter */
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				continue;
			} catch (PacketTimeOutException e){
				continue;
			}
			

			long rtt = answerTime - time;												/* RTT */
			results[recvPackets - 1] = rtt;												/* add current result into results array */
			
			// *** Pablo's modifications *** //TODO to be removed
			// Local Broadcast receiver to inform about the current speed to the speedTestActivity			
			Intent intent = new Intent("currentLatencyIntent");			
			intent.putExtra("currentLatencyValue", String.valueOf(rtt/1000000));			
			LocalBroadcastManager.getInstance(SKApplication.getAppInstance().getBaseContext()).sendBroadcast(intent);
			// *** End Pablo's modifications *** //

			sleep(rtt);																	/* sleep RTT time before sending next datagram */
		}

		socket.close();																	/* close socket */

		getStats();																		/* process stats aggregates */
		
																						/* Return results */
		output();
		finish();
		infoString = LATENCYDONE;
		
		setLatencyValueNanoseconds( (long)averageNanoseconds );
	}

	
	private void sleep(long rtt) {//TODO rethink
		long sleepPeriod = interPacketTime - rtt;										/* If RTT is greater then interpackettime then there is not sleep */

		if (sleepPeriod > 0) {
			long millis = (long) Math.floor(sleepPeriod / 1000000);
			int nanos = (int) sleepPeriod % 1000000;
			try {
				Thread.sleep(millis, nanos);
			} catch (InterruptedException e) {

			}
		}
	}

	private void failure() {
		testStatus = "FAIL";
		output();
		finish();
	}

	public/*private*/ int getNumOfResults(){
		int nResults = 0;
		if (recvPackets < 100) {																/* If result set is less then 100 packets  */
			nResults = recvPackets;																/* use it as is */
		} else {
			nResults = (int) Math.ceil(percentile / 100.0 * recvPackets);						/* otherwise apply percentile (100 - absent default) */
		}
		return nResults;
	}
	
	private void getAverage(){
		int nResults = getNumOfResults();
		
		Arrays.sort(results, 0, recvPackets);													/* find min and max values of the array considering percentile */
		minimumNanoseconds = results[0];
		maximumNanoseconds = results[nResults - 1];
		averageNanoseconds = 0;
		for (int i = 0; i < nResults; i++) {
			averageNanoseconds += results[i];
		}
		averageNanoseconds /= nResults;															/* average */
	}

	private void getStdDev(){
		int nResults = getNumOfResults();
		
		stddeviationNanoseconds = 0;

		for (int i = 0; i < nResults; ++i) {
			stddeviationNanoseconds += Math.pow(results[i] - averageNanoseconds, 2);
		}

		if (nResults - 1 > 0) {
			stddeviationNanoseconds = Math.sqrt(stddeviationNanoseconds / (nResults - 1));
		} else {
			stddeviationNanoseconds = 0;
		}		
	}
	
	private void getStats() {
		if (recvPackets <= 0) {																	/* Return if weird number of packets  */
			failure();
			return;
		}
		testStatus = "OK";																		/* Otherwise status is OK */

		getAverage();
		getStdDev();																			/* std deviation */
	}

	private void setTarget(String target) {
		this.target = target;
	}

	private void setPort(int port) {
		this.port = port;
	}

	private void setNumberOfDatagrams(int n) {
		numdatagrams = n;
		results = new long[numdatagrams];
	}

	private void setDelayTimeout(int delay) {
		delayTimeout = delay / 1000;
	}

	private void setInterPacketTime(int time) {
		interPacketTime = time * 1000; // nanoSeconds
	}

	private void setPercentile(int n) {
		percentile = n;
	}

	private void setMaxExecutionTime(long time) {
		maxExecutionTimeNanoseconds = time * 1000; // nanoSeconds
	}

	public boolean isProgressAvailable() {
		return true;
	}

	public int getProgress() {
		double retTime = 0;
		double retPackets = 0;
		if (maxExecutionTimeNanoseconds > 0) {
			long currTime = (System.nanoTime() - startTimeNanonseconds);
			retTime = (double) currTime / maxExecutionTimeNanoseconds;
		}
		retPackets = (double) sentPackets / numdatagrams;

		double ret = retTime > retPackets ? retTime : retPackets;
		ret = ret > 1 ? 1 : ret;
		return (int) (ret * 100);
	}
	
	public String getTarget() {
		return target;
	}

	private String target = "";
	private int port = 0;
	private String infoString = LATENCYRUN;
	private String ipAddress;
	private String testStatus = "FAIL";
	
	private double averageNanoseconds = 0.0;
	private double stddeviationNanoseconds = 0.0;
	private long minimumNanoseconds = 0;
	private long maximumNanoseconds = 0;
	private long startTimeNanonseconds = 0;
	private long maxExecutionTimeNanoseconds = 0;
	
	private double percentile = 100;
	private int numdatagrams = 0;
	private int delayTimeout = 0;
	private int sentPackets = 0;
	private int recvPackets = 0;
	private int interPacketTime = 0;
	/*private*/public long[] results = null;
	private BlockingQueue<Long> bq_results = null;
	
	final private void setParams(List<Param> params) {
		try {
			for (Param param : params) {
				String value = param.getValue();
				if (param.contains(TARGET)) {
					setTarget(value);
				} else if (param.contains( PORT)) {
					setPort(Integer.parseInt(value));
				} else if (param.contains( NUMBEROFPACKETS)) {
					setNumberOfDatagrams(Integer.parseInt(value));
				} else if (param.contains( DELAYTIMEOUT)) {
					setDelayTimeout(Integer.parseInt(value));
				} else if (param.contains( INTERPACKETTIME)) {
					setInterPacketTime(Integer.parseInt(value));
				} else if (param.contains( PERCENTILE)) {
					setPercentile(Integer.parseInt(value));
				} else if (param.contains( MAXTIME)) {
					setMaxExecutionTime(Long.parseLong(value));
				} 
			}
		} catch (NumberFormatException nfe) {
			return;
		}
	}


	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public EDimension getDimension() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ETestType getType() {
		return ETestType.LATENCY;
	}
}
