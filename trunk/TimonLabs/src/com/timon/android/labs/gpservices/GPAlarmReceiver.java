/**
 * 
 */
package com.timon.android.labs.gpservices;

import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.timon.android.labs.utils.GKIMLog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @author HP
 *
 */
public class GPAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "GPAlarmReceiver";

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		GKIMLog.lf(context, 1, TAG+"=>onReceive");
		try {
            DownloaderClientMarshaller.startDownloadServiceIfRequired(context, intent,
                    GPDownloaderService.class);
        } catch (NameNotFoundException e) {
            GKIMLog.lf(context, 5, TAG+"=>onReceive NameNotFoundException:" + e.getMessage());
        }
	}

}
