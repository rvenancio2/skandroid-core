package com.samknows.measurement;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import com.samknows.libcore.IObservable;
import com.samknows.libcore.R;
import com.samknows.libcore.SKConstants;
import com.samknows.libcore.SKLogger;
import com.samknows.libcore.SKOperators;
import com.samknows.measurement.environment.CellTowersDataCollector;
import com.samknows.measurement.storage.ExportFile;
import com.samknows.measurement.test.TestResultsManager;
import com.samknows.ska.activity.SKATermsOfUseActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SKApplication extends Application{

	static private SKApplication sAppInstance = null;
	
	public SKApplication() {
		super();

		sAppInstance = this;

		SKConstants.RStringQuit = R.string.quit;
		SKConstants.RStringReallyQuit = R.string.really_quit;
		SKConstants.RStringYes = R.string.yes;
		SKConstants.RStringNoDialog = R.string.no_dialog;

		SKConstants.PREF_KEY_USED_BYTES = "used_bytes";
		SKConstants.PREF_DATA_CAP = "data_cap_pref";
		SKConstants.PROP_TEST_START_WINDOW_RTC = "test_start_window_in_millis_rtc";
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		// Initialize the SKOperators data!
		SKOperators.getInstance(getApplicationContext());

		File theCacheDir = getExternalCacheDir();
		if (theCacheDir == null) {
			theCacheDir = getCacheDir();
		}
		
	
		// Clear the cache dir of old export files... as they might be quite big!
		{
			File[] files = theCacheDir.listFiles();
			
			String baseExportName = getString(R.string.menu_export_default_file_name_no_extension);

			if (files != null) {
				for (File file : files) {
					if (file.getName().indexOf(baseExportName) == 0) {
						file.delete();
					}
				}
			}
		}

		SKLogger.setStorageFolder(theCacheDir);
		TestResultsManager.setStorage(theCacheDir);
		
		// Do NOT use the storage area for ExportFiles... these should be retained for future export.
		ExportFile.setStorage(getFilesDir());
		
		SK2AppSettings.create(this);
		CachingStorage.create(this);
		
		// Start monitoring for cell tower signal strength etc....
		// We need to do this, as Android does not allow us to query this information synchronously.
		CellTowersDataCollector.sStartToCaptureCellTowersData(this);
	}


	public static SKApplication getAppInstance() {
		return sAppInstance;
	}

	public void showTermsAndConditions(Activity activity) {
		Intent intent = new Intent(activity, SKATermsOfUseActivity.class);
		activity.startActivity(intent);
	}

	// Network type results querying...
	public enum eNetworkTypeResults {
		eNetworkTypeResults_Any,
		eNetworkTypeResults_Mobile,
		eNetworkTypeResults_WiFi
	};

	private static eNetworkTypeResults sNetworkTypeResults = eNetworkTypeResults.eNetworkTypeResults_Mobile;

	public static eNetworkTypeResults getNetworkTypeResults() {
		return sNetworkTypeResults;
	}

	public static void setNetworkTypeResults(eNetworkTypeResults networkTypeResults) {
		sNetworkTypeResults = networkTypeResults;
	}

	// Override this, if you want your application to support a 1-day result view.
	public boolean supportOneDayResultView() {
		return false;
	}
	
	// Override this, if you want your application to support continuous testing.
	public boolean supportContinuousTesting() {
		return false;
	}

	// Get the class of the main activity!
	public Class getTheMainActivityClass() {
		return null;
	}

	// Return the About screen title.
	public String getAboutScreenTitle() {
		return getApplicationContext().getString(R.string.about);
	}

	public boolean hideJitter() {
		return false;
	}
	
	public boolean hideLatency() {
		return false;
	}

	public boolean hideLoss() {
		return false;
	}
	
	public boolean hideJitterLatencyAndPacketLoss() {
		return hideJitter() & hideLatency() & hideLoss();
	}

	public boolean allowUserToSelectTestToRun() {
		// Run all tests!
		return false;
	}

	public boolean isExportMenuItemSupported() {
		return false;
	}
	
	// Datacap enabling/disabling
	public boolean canViewDataCapInSettings () {
	   return true;
	}
	
	public boolean canDisableDataCap () {
		return false;
	}
	
	// Datacap - enable/disable (managed via the SKAPreferenceActivity)
	public boolean getIsDataCapEnabled() {
		if (canDisableDataCap () == false) {
			// Can't disable the datacap in this version of the app - so in this
			// case, the data cap is always enabled.
			return true;
		}

		// The value is saved/restored automatically through PreferenceManager.
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		return p.getBoolean(SKConstants.PREF_DATA_CAP_ENABLED, true);
	}
	
	public boolean isSocialMediaExportSupported() {
		return false;
	}
	
	public boolean isSocialMediaImageExportSupported() {
		return false;
	}

	// Support throttle query supported?
	public boolean isThrottleQuerySupported() {
		return false;
	}
	
	public boolean getDoesAppDisplayClosestTargetInfo() {
		return true;
	}
	
	// Some versions of the app can enable background menu forcing via the menu...
	public boolean isForceBackgroundMenuItemSupported () {
		return false;
	}
	
	public String getExportFileProviderAuthority() {
        // e.g.	return "com.samknows.myapppackage.ExportFileProvider.provider";
		SKLogger.sAssert(getClass(),  false);
		return null;
	}
	
	public String getAppName() {
		SKLogger.sAssert(getClass(), false); // Must be overridden!
		return "Unknown";
	}
	
	// For now, we don't generally support server-based upload speed measurement testing.
	public boolean getDoesAppSupportServerBasedUploadSpeedTesting() {
		return false;
	}
	
	// Must be overridden!
	public String getDCSInitUrl() {
		SKLogger.sAssert(getClass(), false);
		return "DCSInitUrl_UNKNOWN";
	}

	// Must be overridden!
	public String getEnterpriseId() {
		SKLogger.sAssert(getClass(), false);
		return "EnterpriseId_UNKNOWN";
	}

	public String getCrashManagerId() {
		SKLogger.sAssert(getClass(), false);
		return "CrashManagerId_UNKNOWN";
	}
	
	private static boolean sbUpdateAllDataOnScreen = false;
	
	public static void sSetUpdateAllDataOnScreen(boolean value) {
		sbUpdateAllDataOnScreen = value;
	}
	
	public static boolean sGetUpdateAllDataOnScreen() {
		return sbUpdateAllDataOnScreen;
	}

	public Boolean getIsBackgroundProcessingEnabledInTheSchedule() {
		Boolean backgroundTest = SK2AppSettings.getSK2AppSettingsInstance().getIsBackgroundProcessingEnabledInTheSchedule();
		return backgroundTest;
	}
	
	public Boolean getIsBackgroundTestingEnabledInUserPreferences() {
    	if (getIsBackgroundProcessingEnabledInTheSchedule() == false) {
    		return false;
    	}
    	
    	Boolean backgroundTest = SK2AppSettings.getSK2AppSettingsInstance().getIsBackgroundTestingEnabledInUserPreferences();
    	return backgroundTest;
	}
	
	public Boolean getPassiveMetricsJustDisplayPublicIpAndSubmissionId() {
		return false;
	}
	
	public boolean getForceUploadDownloadSpeedToReportInMbps() {
		return false;
	}

  // Used by the new app...
  public String mLastSubmissionId = "";
  public String mLastPublicIp = "";
  
  public double[] getDownloadSixSegmentMaxValues() {
	double arrSegmentMinValues_Download[] = {1.0, 2.0, 5.0, 10.0, 30.0, 100.0};
	return arrSegmentMinValues_Download;
  }
  
  public double[] getUploadSixSegmentMaxValues() {
	double arrSegmentMaxValues_Upload[] = {0.5, 1.0, 1.5, 2.0, 10.0, 50.0};
	return arrSegmentMaxValues_Upload;
  }

}
