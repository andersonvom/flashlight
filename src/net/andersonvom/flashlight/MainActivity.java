package net.andersonvom.flashlight;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	public static Camera cam;
	public static boolean cameraOn = true;
	public static SharedPreferences settings;
	public static int currentBackgroundColor = Color.BLACK;
	public static boolean hasFlash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		hasFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		ImageView toggleButton = (ImageView) findViewById(R.id.toggle_button);
		toggleButton.setOnClickListener(this);
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
		if (!runBackground)
		{
			if (cam != null) toggleFlashlight();
			updateUsageStats();
		}
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
		if (hasFlash)
		{
			toggleCameraFlash();
		}
		else
		{
			toggleScreen();
		}
	}

	private boolean toggleCameraFlash()
	{
		if (cam == null) cam = Camera.open();
		Parameters p = cam.getParameters();

		boolean supportsTorchMode = p.getSupportedFlashModes().contains(Parameters.FLASH_MODE_TORCH);
		if (!supportsTorchMode)
		{
			Toast.makeText(this, R.string.torch_not_supported, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (p.getFlashMode().equals(Parameters.FLASH_MODE_OFF))
		{
			Log.w("[Flashlight]", "Flashlight ON");
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			cam.setParameters(p);
			cam.startPreview();
			Toast.makeText(this, R.string.flashlight_on, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Log.w("[Flashlight]", "Flashlight OFF");
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			cam.setParameters(p);
			cam.stopPreview();
			cam.release();
			cam = null;
			Toast.makeText(this, R.string.flashlight_off, Toast.LENGTH_SHORT).show();
		}

		return true;
	}

	private void toggleScreen()
	{
		currentBackgroundColor = (currentBackgroundColor == Color.BLACK) ? Color.WHITE : Color.BLACK;
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_activity_layout);
		layout.setBackgroundColor(currentBackgroundColor);
	}

	private void updateUsageStats()
	{
		SharedPreferences.Editor editor = settings.edit();
		int usageCount = settings.getInt(SettingsActivity.PREF_USAGE_COUNT, 0) + 1;
		editor.putInt(SettingsActivity.PREF_USAGE_COUNT, usageCount);
		editor.commit();

		if (usageCount % SettingsActivity.PREF_SUGGEST_MARKET_COUNT == 0)
		{
			suggestRateApp();
		}
	}

	private void suggestRateApp()
	{
	}

}
