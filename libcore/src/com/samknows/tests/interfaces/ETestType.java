package com.samknows.tests.interfaces;

/**
 * 
 * @author skeugene
 *
 *	This enum defines the order in which tests are executed in case there is a batch of test to be run
 *
 */
public enum ETestType {
	CLOSEST_TARGET,
	DOWNLOAD,
	C_UPLOAD,
	S_UPLOAD,
	LATENCY,
	PROXY_DETECTOR,
	TRIGGER 
}
