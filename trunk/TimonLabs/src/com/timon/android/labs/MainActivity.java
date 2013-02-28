package com.timon.android.labs;

import java.io.IOException;
import java.util.Locale;

import com.timon.android.labs.gpservices.GPDownloaderService;
import com.timon.android.labs.utils.GKIMLog;

import com.fedorvlasov.lazylist.LazyAdapter;
import com.fedorvlasov.lazylist.LazyListMainActivity;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(R.string.title_activity_main);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		protected static final String TAG = "TimonLabs";

		PullToRefreshScrollView mPullRefreshScrollView;
		ScrollView mScrollView;
		public ProgressDialog mProgressDialog; 
		private IStub mDownloaderClientStup;
		private IDownloaderService mRemoteService = null;
		private IDownloaderClient mDownloaderClient = new IDownloaderClient() {
			
			@Override
			public void onServiceConnected(Messenger m) {
				GKIMLog.lf(null, 1, TAG+"=>onServiceConnected: " + m.toString());
				mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
			    mRemoteService.onClientUpdated(mDownloaderClientStup.getMessenger());
			}
			
			@Override
			public void onDownloadStateChanged(int newState) {
				GKIMLog.lf(null, 1, TAG+"=>onDownloadStateChanged: "+ newState);
			}
			
			@Override
			public void onDownloadProgress(DownloadProgressInfo progress) {
				GKIMLog.lf(null, 1, TAG+"=>onDownloadProgress: "+ progress.toString());
				
			}
		};
		
		public OnClickListener mOnClickToDownloadAsset = new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				// TODO Implementing Downloading service
				// Build an Intent to start this activity from the Notification
				Intent notifierIntent = new Intent(getActivity(),
						MainActivity.class);
				notifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						getActivity(), 0, notifierIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// Start the download service (if required)
				try {
					int startResult = DownloaderClientMarshaller
							.startDownloadServiceIfRequired(getActivity(),
									pendingIntent, GPDownloaderService.class);
					GKIMLog.lf(null, 0, TAG+"=>startDownloadServiceIfRequired.");
					// If download has started, initialize this activity to show
					// download progress
					if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
						mDownloaderClientStup = DownloaderClientMarshaller.CreateStub(mDownloaderClient,
				                GPDownloaderService.class);
						// This is where you do set up to display the download
						// progress (next step)
						if (mProgressDialog != null && !mProgressDialog.isShowing()) {
							mProgressDialog.show();
						}
						return;
					} // If the download wasn't necessary, fall through to start the
						// app
				} catch (NameNotFoundException e) {
					 GKIMLog.lf(null, 5, TAG+"=>onReceive NameNotFoundException:" + e.getMessage());
				}

			}
		};

		
		private TextView mTextView;

		private AssetFileDescriptor afd;

		@Override
		public void onResume() {
			GKIMLog.lf(null, 1, TAG+"=>onResume");
			if (mDownloaderClientStup != null) {
				mDownloaderClientStup.connect(getActivity());
			}
			super.onResume();
		}

		@Override
		public void onStop() {
			GKIMLog.lf(null, 1, TAG+"=>onStop");
			if (mDownloaderClientStup != null) {
				mDownloaderClientStup.disconnect(getActivity());
			}
			super.onStop();
		}

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			GKIMLog.lf(null, 0, TAG + "=>onCreateView.");
			mProgressDialog = new ProgressDialog(getActivity());
			int number = 0;
			if (savedInstanceState != null) {
				if (savedInstanceState
						.containsKey(DummySectionFragment.ARG_SECTION_NUMBER)) {
					number = savedInstanceState
							.getInt(DummySectionFragment.ARG_SECTION_NUMBER);
					GKIMLog.lf(null, 0, TAG + "=>onCreateView with number: "
							+ number);
				}
			}
			Bundle agrs = this.getArguments();
			if (agrs != null) {
				if (agrs.containsKey(DummySectionFragment.ARG_SECTION_NUMBER)) {
					number = agrs
							.getInt(DummySectionFragment.ARG_SECTION_NUMBER);
					GKIMLog.lf(null, 0, TAG
							+ "=>onCreateView with agrs number: " + number);
				}
			}
			View root = null;
			switch (number - 1) {
			case 1:
				root = initSecondFragVideoView(inflater);
				break;
			case 2:
				root = initThirdFragList(inflater);
				break;
			case 0:
			default:
				root = inflater.inflate(R.layout.activity_ptr_scrollview, null,
						false);
				mPullRefreshScrollView = (PullToRefreshScrollView) root
						.findViewById(R.id.pull_refresh_scrollview);
				mTextView = (TextView) root
						.findViewById(R.id.tv_pull_to_refresh);
				mPullRefreshScrollView
						.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

							@Override
							public void onRefresh(
									PullToRefreshBase<ScrollView> refreshView) {
								GKIMLog.lf(null, 0, TAG + "=>onRefresh: "
										+ refreshView);
								new GetDataTask().execute();
							}
						});

				mScrollView = mPullRefreshScrollView.getRefreshableView();
				break;
			}
			return root;
		}

		private View initSecondFragVideoView(LayoutInflater inflater) {
			View result = inflater.inflate(R.layout.video, null);
			final VideoView videoV = (VideoView) result
					.findViewById(R.id.video_second_frag);

			// String path = "android.resource://"
			// + this.getActivity().getApplicationInfo().packageName + "/"
			// + R.raw.tckl_logo_ipad;
			String relativePath = "video/tckl_logo_ipad.mp4";
			String path = "file:///android_assets/" + relativePath;

			TextView pathV = (TextView) result
					.findViewById(R.id.tv_second_frag);
			pathV.setText(path);

			Button btnDownload = (Button) result
					.findViewById(R.id.btn_second_frag);

			AssetManager am = getActivity().getAssets();
			if (am != null) {
				try {
					afd = am.openFd(relativePath);
				} catch (IOException e) {
					GKIMLog.lf(
							null,
							5,
							TAG + "=>initSecondFragVideoView exception: "
									+ e.getMessage());
				}
			}
			SurfaceHolder holder = videoV.getHolder();
			final MediaPlayer mp = new MediaPlayer();
			if (afd == null) {
				GKIMLog.lf(null, 0, TAG + "=> Asset file is null");
//				if (btnDownload != null) {
//					btnDownload.setVisibility(View.VISIBLE);
//					btnDownload.setOnClickListener(mOnClickToDownloadAsset);
//				}

			} else {
				GKIMLog.lf(null, 0, TAG + "=> Asset file is existed");
				if (btnDownload != null) {
					btnDownload.setText("Already got Asset files");
					btnDownload.setOnClickListener(null);
				}
				holder.addCallback(new SurfaceHolder.Callback() {
					@Override
					public void surfaceCreated(SurfaceHolder holder) {
						GKIMLog.lf(null, 0, TAG + "=>surfaceCreated.");
						try {
							mp.setDisplay(holder);
							mp.setDataSource(afd.getFileDescriptor(),
									afd.getStartOffset(), afd.getLength());
							mp.setLooping(true);

							mp.prepareAsync();
						} catch (IllegalArgumentException e) {
							GKIMLog.lf(
									null,
									5,
									TAG
											+ "=>initSecondFragVideoView IllegalArgumentException: "
											+ e.getMessage());
						} catch (IllegalStateException e) {
							GKIMLog.lf(
									null,
									5,
									TAG
											+ "=>initSecondFragVideoView IllegalStateException: "
											+ e.getMessage());
						} catch (IOException e) {
							GKIMLog.lf(null, 5, TAG
									+ "=>initSecondFragVideoView IOException: "
									+ e.getMessage());
						}
					}

					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						GKIMLog.lf(null, 0, TAG + "=>surfaceDestroyed.");
					}

					@Override
					public void surfaceChanged(SurfaceHolder holder,
							int format, int width, int height) {
						GKIMLog.lf(null, 0, TAG + "=>surfaceChanged.");
					}
				});
			}
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					GKIMLog.lf(null, 0,
							TAG + "=>onCompletion: " + mp.toString());
				}
			});
			mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					GKIMLog.lf(null, 0, TAG + "=>onPrepared");
					videoV.setVisibility(View.VISIBLE);
					mp.start();
				}
			});

			GKIMLog.lf(null, 0, TAG + "=>initSecondFragVideoView done.");
			return result;
		}

		private View initThirdFragList(LayoutInflater inflater) {
			LinearLayout result = new LinearLayout(getActivity());
			result.setOrientation(LinearLayout.VERTICAL);
			result.setGravity(Gravity.CENTER);
			OnClickListener l = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case 0: // Open LazyList
						startActivity(new Intent(getActivity(),
								LazyListMainActivity.class));
						break;
					default:
						break;
					}
				}
			};
			Button btn1 = new Button(getActivity());
			btn1.setId(0);
			btn1.setText("Open LazyList");

			result.addView(btn1);
			return result;
		}

		private class GetDataTask extends AsyncTask<Void, Void, String[]> {

			@Override
			protected String[] doInBackground(Void... params) {
				// Simulates a background job.
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				return new String[] { "refresh returned." };
			}

			@Override
			protected void onPostExecute(String[] result) {
				// Do some stuff here
				GKIMLog.lf(null, 0, TAG + "=>onPostExecute: " + result[0]);
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshScrollView.onRefreshComplete();
				if (mTextView != null) {
					Activity act = getActivity();
					act.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String text = mTextView.getText().toString();
							text += " returned.";
							mTextView.setText(text);
						}
					});
				}
				super.onPostExecute(result);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
