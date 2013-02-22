package net.andersonvom.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	public static Camera cam;
	public static Context context;
	public static boolean cameraOn = true;
	public static SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		updateUsageStats();

		ImageView toggleButton = (ImageView) findViewById(R.id.toggle_button);
		toggleButton.setOnClickListener(this);
		context = toggleButton.getContext();
		if (cam == null) toggleFlashlight();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				break;
		}

		return true;
	}

	@Override
	public void onClick(View v)
	{
		cameraOn = !cameraOn;
		toggleFlashlight();
	}

	@Override
	protected void onPause()
	{
		boolean runBackground = settings.getBoolean(SettingsActivity.PREF_RUN_BACKGROUND, false);
		if (cam != null && !runBackground) toggleFlashlight();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		if (cameraOn && cam == null) toggleFlashlight();
		super.onResume();
	}

	private void toggleFlashlight()
	{
		boolean hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (hasFlash)
		{
			Toast.makeText(context, R.string.flashlight_toggle_msg, Toast.LENGTH_SHORT).show();
			toggleCameraFlash();
		}
		else
		{
			Toast.makeText(context, R.string.flashlight_not_found, Toast.LENGTH_SHORT).show();
			toggleScreen();
		}
	}

	private void toggleCameraFlash()
	{
		if (cam == null) cam = Camera.open();

		Parameters p = cam.getParameters();
		if (p.getFlashMode().equals(Parameters.FLASH_MODE_OFF))
		{
			Log.w("[Flashlight]", "Flash TORCH....");
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			cam.setParameters(p);
			cam.startPreview();
		}
		else
		{
			Log.w("[Flashlight]", "Flash OFF...");
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(p);
			cam.stopPreview();
			cam.release();
			cam = null;
		}
	}

	private void toggleScreen()
	{
	}

	private void updateUsageStats()
	{
		SharedPreferences.Editor editor = settings.edit();
		int usageCount = settings.getInt(SettingsActivity.PREF_USAGE_COUNT, 0) + 1;
		editor.putInt(SettingsActivity.PREF_USAGE_COUNT, usageCount);
		editor.commit();

		if (usageCount % 50 == 0)
		{
			suggestRateApp();
		}
	}

	private void suggestRateApp()
	{
	}

}
