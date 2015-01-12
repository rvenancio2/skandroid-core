package com.samknows.measurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.json.JSONObject;






import com.samknows.libcore.R;
import com.samknows.libcore.SKConstants;
import com.samknows.libcore.SKLogger;
import com.samknows.measurement.net.SubmitTestResultsAnonymousAction;
import com.samknows.measurement.schedule.ScheduleConfig;
import com.samknows.measurement.schedule.TestDescription;
import com.samknows.measurement.schedule.condition.ConditionGroupResult;
import com.samknows.measurement.schedule.datacollection.BaseDataCollector;
import com.samknows.measurement.storage.DBHelper;
import com.samknows.measurement.storage.PassiveMetric;
import com.samknows.measurement.storage.TestBatch;
import com.samknows.measurement.storage.StorageTestResult;
import com.samknows.measurement.test.TestContext;
import com.samknows.measurement.test.TestExecutor;
import com.samknows.tests.ClosestTarget;
import com.samknows.tests.Test;
import com.samknows.tests.TestFactory;
import com.samknows.tests.interfaces.ITest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/*  
 * This class is used to run the the tests when they are executed manually
 * implements a runnable interface and and uses an Handler in order to publish
 * the tests results to the interface
 */
public class ManualTest1 implements Runnable, Observer {
	private Handler mHandler, localHandler;	
	private TestDescription mTestDescription;
	private Context ctx;
	private AtomicBoolean run = new AtomicBoolean(true);
	public static boolean isExecuting = false;

	private void init(){
		localHandler = new Handler() {
		      @Override
		      public void handleMessage(Message msg) {
		        //TODO here request current ITest stats from

		    	  if(msg != null ){
		    		  if (msg.obj instanceof ITest ){
		    			  ITest test = (ITest) msg.obj;
		    			  test.getProgress();
		    			  test.getValue();
		    			  test.getType();
		    			  test.getDimension();
		    		  }
		    	  }
		      }
		};
	}
	
	ManualTest1(Context ctx, TestDescription td) {
		mTestDescription = td;
		this.ctx = ctx;
	}

		
	// returns the maximum amount of bytes used by the manual test
	// This value is generally *MUCH* higher than the *actually* used value.
	// e.g. 40+MB, compared to 4MB. The reason is that the value is from SCHEDULE.xml, and specifies the absolute
	// maximum that a test is allowed to use; in practise, the test runs for a capped amount of time (also in the schedule data),
	// and processes far less data that the defined maximum number of bytes to use.
/*	public long getNetUsage() {
		long ret = 0;
		for (TestDescription td : mTestDescription) {
			ret += td.maxUsageBytes;
		}
		return ret;
	}*/

	/*
	 * Runs all the test in manual tests
	 */
	private boolean mbUdpClosestTargetTestSucceeded = false;
	public static final String kManualTest_UDPFailedSkipTests = "kManualTest_UDPFailedSkipTests";

	@Override
	public void run() {
		DBHelper db = new DBHelper(ctx);
	
		mbUdpClosestTargetTestSucceeded  = false;
		
		sSetIsExecuting(true);
		
		// Start collectors for the passive metrics
		// Start tests
		long startTime = System.currentTimeMillis();
		JSONObject batch = new JSONObject();
		TestContext tc = TestContext.createManualTestContext(ctx);
		TestExecutor manualTestExecutor = new TestExecutor(tc);
		List<JSONObject> testsResults = new ArrayList<JSONObject>();
		List<JSONObject> passiveMetrics = new ArrayList<JSONObject>();
		manualTestExecutor.startInBackGround();
		Message msg;
		long testsBytes = 0;
		//for (TestDescription td : mTestDescription)
		do
		{
			TestDescription td = mTestDescription;
			// GIVEN:
			// - running manual test
			// - if latency/loss/jitter
			// WHEN:
			// - if the closest target UDP test had failed (NB: this is ALWAYS run first in manual testing)
			// THEN:
			// - skip this test
			
			if (td.type.equals(SKConstants.TEST_TYPE_LATENCY)) {
				if (mbUdpClosestTargetTestSucceeded == false) {
					continue;
				}
			}

			manualTestExecutor.addRequestedTest(td);
			// check if a stop command has been received
			if (!run.get()) {
				manualTestExecutor.cancelNotification();
				SKLogger.d(this, "Manual test interrupted by the user.");
				break;
			}
			ConditionGroupResult tr = new ConditionGroupResult();
			ObservableExecutor oe = new ObservableExecutor(manualTestExecutor, td, tr);
			Thread t = new Thread(oe);
			t.start();
			while (true) {
				try {
					t.join(100);
					if (!t.isAlive())
						break;
				} catch (InterruptedException ie) {
					SKLogger.e(this, ie.getMessage());
				}
//				for (JSONObject pm : progressMessage(td, manualTestExecutor)) {
//					msg = new Message();
//					msg.obj = pm;
//					mHandler.sendMessage(msg);
//					SKLogger.e(this.toString(), "Message sent:at " + (new java.text.SimpleDateFormat("HH:mm:ss.SSS")).format(new java.util.Date()) + pm.toString());//haha
//				}

			}
			
			if (td.type.equals(SKConstants.TEST_TYPE_CLOSEST_TARGET)) {
				SKLogger.sAssert(getClass(), manualTestExecutor.getExecutingTest() != null);
				if (manualTestExecutor.getExecutingTest() != null) {
					SKLogger.sAssert(getClass(), manualTestExecutor.getExecutingTest().getClass() == ClosestTarget.class);

					ClosestTarget closestTargetTest = (ClosestTarget)manualTestExecutor.getExecutingTest();
					mbUdpClosestTargetTestSucceeded = closestTargetTest.getUdpClosestTargetTestSucceeded();
				}
			}
			
			testsBytes += manualTestExecutor.getLastTestByte();
			
//			Test theTestThatWasRun = oe.getTheExecutedTestPostRun();
//			if (theTestThatWasRun != null) {
//				if (theTestThatWasRun instanceof ClosestTarget) {
//
//				}
//			}

			List<JSONObject> currResults = new ArrayList<JSONObject>();
			for (String out : tr.results) {
				List<JSONObject> theResult = StorageTestResult.testOutput(out, manualTestExecutor);
				if (theResult != null) {
					currResults.addAll(theResult);
				}
			}
			for (JSONObject cr : currResults) {
				// publish results
				msg = new Message();
				msg.obj = cr;
				mHandler.sendMessage(msg);
				SKLogger.e(this.toString(), "Message sent: at " + (new java.text.SimpleDateFormat("HH:mm:ss.SSS")).format(new java.util.Date()) + cr.toString());//haha
			}
			testsResults.addAll(currResults);
		}while(false);
		
		SKLogger.d(this, "bytes used by the tests: " + testsBytes);
		SK2AppSettings.getInstance().appendUsedBytes(testsBytes);
		// stops collectors
		manualTestExecutor.stop();

		// Gather data from collectors
		for (BaseDataCollector collector : tc.config.dataCollectors) {
			if (collector.isEnabled) {
				for (JSONObject o : collector.getPassiveMetric()) {
					// update interface
					msg = new Message();
					msg.obj = PassiveMetric.passiveMetricToCurrentTest(o);
					mHandler.sendMessage(msg);
					SKLogger.e(this.toString(), "Message sent: at " + (new java.text.SimpleDateFormat("HH:mm:ss.SSS")).format(new java.util.Date())+ PassiveMetric.passiveMetricToCurrentTest(o).toString());//haha
					// save metric
					passiveMetrics.add(o);
				}
			}
		}
		// insert batch in the database
		try {
			batch.put(TestBatch.JSON_DTIME, startTime);
			batch.put(TestBatch.JSON_RUNMANUALLY, "1");
		} catch (JSONException je) {
			SKLogger.e(this,
					"Error in creating test batch object: " + je.getMessage());
		}

		// insert the results in the database only if we didn't receive a stop
		// command
		long batchId = -1;
		if (run.get()) {
			batchId = db.insertTestBatch(batch, testsResults, passiveMetrics);
		}
		
		// And now upload the test (this will get a submission id etc., so *must* have a batch id to save to the database...)
		manualTestExecutor.save("manual_test", batchId);

		// Send completed message to the interface
		msg = new Message();
		JSONObject jtc = new JSONObject();
		try {
			Thread.sleep(1000);
			jtc.put(StorageTestResult.JSON_TYPE_ID, "completed");
			msg.obj = jtc;

		} catch (JSONException je) {
			SKLogger.e(this, "Error in creating json object: " + je.getMessage());
		} catch (InterruptedException e) {
			SKLogger.e(
					this,
					"Sleep interrupted in the manual test view: "
							+ e.getMessage());
		}
		mHandler.sendMessage(msg);
		SKLogger.e(this.toString(), "Message sent: at " + (new java.text.SimpleDateFormat("HH:mm:ss.SSS")).format(new java.util.Date()) + jtc.toString());//haha

		try {
			// Submitting test results
			new SubmitTestResultsAnonymousAction(ctx).execute();
		} catch (Throwable t) {
			SKLogger.e(this, "Submit result. ", t);
		}

		// MPC 21/11/2014 - Ipp w can see NO REASON for this... except, to start the traffic stat collector - which then stops immediately!
//		if(!SKApplication.getAppInstance().getIsBackgroundTestingEnabledInUserPreferences()){
//			MainService.force_poke(ctx);
//		}
		SKLogger.d(this, "Exiting manual test");
		
		sSetIsExecuting(false);
	}
	
	private static void sSetIsExecuting(boolean bValue) {
		isExecuting = bValue;
	}

	private class ObservableExecutor implements Runnable {
		public TestExecutor te;
		private TestDescription td;
		private ConditionGroupResult tr;
		private Test theTestThatWasRun = null;

		public ObservableExecutor(TestExecutor te, TestDescription td,
				ConditionGroupResult tr) {
			this.te = te;
			this.td = td;
			this.tr = tr;
		}

		@Override
		public void run() {
			theTestThatWasRun = te.executeTest(td, tr);
		}

     	public Test getTheExecutedTestPostRun() {
     		return theTestThatWasRun;
	     }
	
	}
	

	// It stops the test from being executed
	public void stop() {
		
		run.set(false);
	}

	@Override
	public void update(Observable observable, Object data) {
		Message msg = Message.obtain(localHandler, 0, data);
		localHandler.sendMessage(msg);
		
	}
}
