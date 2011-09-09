/*
 *    Copyright 2010 Werner Bayer
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.pardus.android.browser;

import java.io.File;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

/**
 * Main activity - application entry point.
 */
public class Pardus extends Activity {

	private static final int MENU_GROUP_UNIS = 1;

	private static final int MENU_ORION = 1;

	private static final int MENU_ARTEMIS = 2;

	private static final int MENU_PEGASUS = 3;

	private PardusWebView browser;

	private ProgressBar progress;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (PardusConstants.DEBUG) {
			Log.v(this.getClass().getSimpleName(), "Creating application");
		}
		PardusPreferences.init(this);
		PardusNotification.init(this);
		CookieSyncManager.createInstance(this);
		// determine available storage directories (prefer external device)
		String storageDir = getExternalPardusDir();
		if (storageDir == null) {
			// no external storage available
			storageDir = getInternalPardusDir();
			if (storageDir == null) {
				Log.e(this.getClass().getSimpleName(),
						"Unable to determine storage directory");
				PardusNotification
						.showLong("No suitable place to store the image pack found");
				finish();
			}
		}
		if (PardusConstants.DEBUG) {
			Log.d(this.getClass().getSimpleName(), "Storage directory set to "
					+ storageDir);
		}
		String cacheDir = getCacheDir().getAbsolutePath();
		// attach layout to screen
		setContentView(R.layout.browser);
		// initialize progress bar
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.setMax(100);
		progress.setIndeterminate(false);
		// initialize browser
		browser = (PardusWebView) findViewById(R.id.browser);
		browser.initClients(progress);
		browser.initDownloadListener(storageDir, cacheDir);
		browser.login(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getGroupId() == MENU_GROUP_UNIS) {
			switch (item.getItemId()) {
			case MENU_ARTEMIS:
				browser.switchUniverse("Artemis");
				return true;
			case MENU_ORION:
				browser.switchUniverse("Orion");
				return true;
			case MENU_PEGASUS:
				browser.switchUniverse("Pegasus");
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		switch (item.getItemId()) {
		case R.id.option_settings:
			browser.showSettings();
			return true;
		case R.id.option_about:
			browser.showAbout();
			return true;
		case R.id.option_refresh:
			browser.reload();
			return true;
		case R.id.option_login:
			browser.login(false);
			return true;
		case R.id.option_logout:
			finish();
			return true;
		case R.id.option_nav:
			browser.loadUniversePage(PardusConstants.navPage);
			return true;
		case R.id.option_msg:
			browser.loadUniversePage(PardusConstants.msgPage);
			return true;
		case R.id.option_sendmsg:
			browser.loadUniversePage(PardusConstants.sendMsgPage);
			return true;
		case R.id.option_forum:
			browser.loadUniversePage(PardusConstants.forumPage);
			return true;
		case R.id.option_chat:
			browser.loadUniversePage(PardusConstants.chatPage);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.removeGroup(MENU_GROUP_UNIS);
		String universe = browser.getUniverse();
		if (universe != null) {
			String switchStr = getString(R.string.option_switch);
			if (!universe.equals("artemis")) {
				menu.add(MENU_GROUP_UNIS, MENU_ARTEMIS, 1, switchStr
						+ " Artemis");
			}
			if (!universe.equals("orion")) {
				menu.add(MENU_GROUP_UNIS, MENU_ORION, 2, switchStr + " Orion");
			}
			if (!universe.equals("pegasus")) {
				menu.add(MENU_GROUP_UNIS, MENU_PEGASUS, 3, switchStr
						+ " Pegasus");
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && browser.canGoBack()) {
			browser.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		if (PardusConstants.DEBUG) {
			Log.v(this.getClass().getSimpleName(),
					"Starting (or restarting) application");
		}
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		if (PardusConstants.DEBUG) {
			Log.v(this.getClass().getSimpleName(),
					"Resuming (or starting) application");
		}
		// wake up the browser
		if (VERSION.SDK_INT >= 7) {
			try {
				Class.forName("android.webkit.WebView")
						.getMethod("onResume", (Class[]) null)
						.invoke(browser, (Object[]) null);
			} catch (Exception e) {
				Log.w(this.getClass().getSimpleName(),
						"Cannot wake up browser threads: "
								+ Log.getStackTraceString(e));
			}
		}
		browser.resumeTimers();
		CookieSyncManager.getInstance().startSync();
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		if (PardusConstants.DEBUG) {
			Log.v(this.getClass().getSimpleName(),
					"Pausing application (to be resumed or stopped or killed)");
		}
		browser.stopLoading();
		if (isFinishing() || PardusPreferences.isLogoutOnHide()) {
			browser.logout();
		}
		progress.setVisibility(View.GONE);
		// keep the browser from working in the background
		CookieSyncManager.getInstance().stopSync();
		browser.pauseTimers();
		if (VERSION.SDK_INT >= 7) {
			try {
				Class.forName("android.webkit.WebView")
						.getMethod("onPause", (Class[]) null)
						.invoke(browser, (Object[]) null);
			} catch (Exception e) {
				Log.w(this.getClass().getSimpleName(),
						"Cannot pause browser threads: "
								+ Log.getStackTraceString(e));
			}
		}
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		if (PardusConstants.DEBUG) {
			Log.v(this.getClass().getSimpleName(),
					"Stopping application (to be destroyed or restarted)");
		}
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.Activity#onConfigurationChanged(android.content.res.Configuration
	 * )
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Gets (or creates if needed) the pardus directory on an external device.
	 * 
	 * @return the location of the pardus directory, or null if no external
	 *         device or if the directory cannot be created
	 */
	private String getExternalPardusDir() {
		String dir = null;
		File externalStorage = Environment.getExternalStorageDirectory();
		if (externalStorage.isDirectory()) {
			// external storage mounted
			if (PardusConstants.DEBUG) {
				Log.v(this.getClass().getSimpleName(),
						"External storage directory at "
								+ externalStorage.getAbsolutePath());
			}
			dir = externalStorage.getAbsolutePath() + "/pardus/img";
			File externalPardusStorage = new File(dir);
			if (!externalPardusStorage.canRead()
					|| !externalPardusStorage.isDirectory()) {
				if (!externalPardusStorage.mkdirs()) {
					if (PardusConstants.DEBUG) {
						Log.v(this.getClass().getSimpleName(),
								"Cannot create external Pardus storage directory");
					}
					return null;
				}
			}
		}
		return dir;
	}

	/**
	 * Gets (or creates if needed) the internal pardus directory.
	 * 
	 * @return the location of the pardus directory, or null if it cannot be
	 *         created
	 */
	private String getInternalPardusDir() {
		String dir = getFilesDir().getAbsolutePath() + "/img";
		File storage = new File(dir);
		if (!storage.canRead() || !storage.isDirectory()) {
			if (!storage.mkdirs()) {
				return null;
			}
		}
		return dir;
	}

}
