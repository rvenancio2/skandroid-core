package com.samknows.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;


import com.samknows.libcore.SKLogger;

public abstract class UploadTest extends HttpTest {
		
	protected double bitrateMpbs1024Based = 		-1.0;			/* ???? Scale coefficient */
	protected byte[] buff;											/* buffer to send values */
	
	protected UploadTest(List<Param> params){
		super(_UPSTREAM, params);
		this.init();
	}
		
	private String[] formValuesArr(){
		String[] values = new String[1];			
		values = new String[1];
		values[0] = String.format("%.2f", (getTransferBytesPerSecond() * 8d / 1000000));

		return values;
	}
					
	private void init(){											/* don't forget to check error state after this method */
																	/* getSocket() is a method from the parent class */
		int maxSendDataChunkSize = 32768;
			
		// Generate this value in case we need it.
		// It is a random value from [0...2^32-1]
		Random sRandom = new Random();								/* Used for initialisation of upload array */
				
		if ( uploadBufferSize > 0 &&  uploadBufferSize <= maxSendDataChunkSize){
			buff = new byte[uploadBufferSize];
		}
		else{
			buff = new byte[maxSendDataChunkSize];
			SKLogger.sAssert(getClass(), false);
		}				

		if (randomEnabled){											/* randomEnabled comes from the parent (HTTPTest) class */
			sRandom = new Random();									/* Used for initialisation of upload array */	
			sRandom.nextBytes(buff);
		}	
	}

	@Override
	public String getStringID() {
		String ret = "";
		if (getThreadsNum() == 1) {
			ret = UPSTREAMSINGLE;
		} else {
			ret = UPSTREAMMULTI;
		}
		return ret;
	}
	
	@Override
	public int getNetUsage() {
		return (int)(getTotalTransferBytes() + getTotalWarmUpBytes());
	}
			
	@Override
	public HashMap<String, String> getResults(){
		HashMap<String, String> ret = new HashMap<String, String>();
		if (!testStatus.equals("FAIL")) {
			String[] values = formValuesArr();
			ret.put("upspeed", values[0]);
		}
		return ret;		
	}
		
	@Override
	public boolean isReady() {
		super.isReady();
		
		if ( uploadBufferSize == 0 || postDataLength == 0) {
			setError("Upload parameter missing");
			return false;
		}
		
		return true;
	}
	
	public String getHumanReadableResult() {
		String ret = "";
		String direction = "upload";
		String type = getThreadsNum() == 1 ? "single connection" : "multiple connection";
		if (testStatus.equals("FAIL")) {
			ret = String.format("The %s has failed.", direction);
		} else {
			ret = String.format(Locale.UK,"The %s %s test achieved %.2f Mbps.", type, direction, (getTransferBytesPerSecond() * 8d / 1000000));
		}
		return ret;
	}
}


/*	@Override
public String getResultsAsString(){							 New Human readable implementation 
	if (testStatus.equals("FAIL")){
		return "";
	}else{
		String[] values = formValuesArr();			
		return String.format(Locale.UK, values[0]);
	}		
}*/
/*	@Override
public String getResults(String locale){			 New Human readable implementation 
	if (testStatus.equals("FAIL")){
		return locale;
	}else{
		String[] values = formValuesArr();			
		return String.format(locale, values[0]);
	}		
}*/


/*public class HumanReadable {
public TEST_STRING testString;
public String[] values;

public String getString(String locale) {
	switch (testString) {
	case DOWNLOAD_SINGLE_SUCCESS:
	case DOWNLOAD_MULTI_SUCCESS:
	case UPLOAD_SINGLE_SUCCESS:
	case UPLOAD_MULTI_SUCCESS:
		return String.format(locale, values[0]);
	case LATENCY_SUCCESS:
		return String.format(locale, values[0], values[1], values[2]);
	case DOWNLOAD_FAILED:
	case UPLOAD_FAILED:
	case LATENCY_FAILED:
		return locale;
	case NONE:
		return "";
	}
	return "";
}

public HashMap<String, String> getValues() {
	HashMap<String, String> ret = new HashMap<String, String>();
	switch (testString) {
	case DOWNLOAD_SINGLE_SUCCESS:
	case DOWNLOAD_MULTI_SUCCESS:
		ret.put("downspeed", values[0]);
		break;
	case UPLOAD_SINGLE_SUCCESS:
	case UPLOAD_MULTI_SUCCESS:
		ret.put("upspeed", values[0]);
		break;
	case LATENCY_SUCCESS:
		ret.put("latency", values[0]);
		ret.put("packetloss", values[1]);
		ret.put("jitter", values[2]);
		break;
	default:
	}
	return ret;
}
}*/