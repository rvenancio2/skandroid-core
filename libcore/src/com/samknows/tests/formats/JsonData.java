package com.samknows.tests.formats;

public final class JsonData {
	/* General data */
	public static final String JSON_TYPE = "type";
	public static final String JSON_TIMESTAMP = "timestamp";
	public static final String JSON_DATETIME = "datetime";
	public static final String JSON_TARGET = "target";
	public static final String JSON_TARGET_IPADDRESS = "target_ipaddress";
	public static final String JSON_SUCCESS = "success";
	
	/* HTTP data */
	public static final String JSON_TRANFERTIME = "transfer_time";
	public static final String JSON_TRANFERBYTES = "transfer_bytes";
	public static final String JSON_BYTES_SEC = "bytes_sec";
	public static final String JSON_WARMUPTIME = "warmup_time";
	public static final String JSON_WARMUPBYTES = "warmup_bytes";
	public static final String JSON_NUMBER_OF_THREADS = "number_of_threads";
	
	/* Latency data */
	public static final String JSON_RTT_AVG = "rtt_avg";
	public static final String JSON_RTT_MIN = "rtt_min";
	public static final String JSON_RTT_MAX = "rtt_max";
	public static final String JSON_RTT_STDDEV = "rtt_stddev";
	public static final String JSON_RECEIVED_PACKETS = "received_packets";
	public static final String JSON_LOST_PACKETS = "lost_packets";
}
