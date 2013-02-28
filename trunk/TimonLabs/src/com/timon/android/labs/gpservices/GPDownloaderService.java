/**
 * 
 */
package com.timon.android.labs.gpservices;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

/**
 * @author HP
 *
 */
public class GPDownloaderService extends DownloaderService {
	// You must use the public key belonging to your publisher account
    public static final String BASE64_PUBLIC_KEY = "YourLVLKey";
    // You should also modify this salt
//    public static final byte[] SALT = new byte[] { 1, 42, -12, -1, 54, 98,
//            -100, -12, 43, 2, -8, -4, 9, 5, -106, -107, -33, 45, -1, 84
//    };
    public static final byte[] SALT = "GPPUBLIC_SALT_VALUES".getBytes();
	/* (non-Javadoc)
	 * @see com.google.android.vending.expansion.downloader.impl.DownloaderService#getPublicKey()
	 */
	@Override
	public String getPublicKey() {
		return BASE64_PUBLIC_KEY;
	}

	/* (non-Javadoc)
	 * @see com.google.android.vending.expansion.downloader.impl.DownloaderService#getSALT()
	 */
	@Override
	public byte[] getSALT() {
		return SALT;
	}

	/* (non-Javadoc)
	 * @see com.google.android.vending.expansion.downloader.impl.DownloaderService#getAlarmReceiverClassName()
	 */
	@Override
	public String getAlarmReceiverClassName() {
		return GPAlarmReceiver.class.getName();
	}

}
