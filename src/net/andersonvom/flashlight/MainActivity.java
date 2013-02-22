package net.andersonvom.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
	public static Camera cam;
	public static final String PREF_USAGE_COUNT = "PREF_USAGE_COUNT";
	public static final String PREF_RATED_APP = "PREF_RATED_APP";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		updateUsageStats();

		ImageView toggleButton = (ImageView) findViewById(R.id.toggle_button);
		toggleButton.setOnClickListener(this);
		if (cam == null) toggleFlashlight(toggleButton.getContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v)
	{
		Context context = v.getContext();
		toggleFlashlight(context);
	}

	private void toggleFlashlight(Context context)
	{
		boolean hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		if (hasFlash)
		{
			Toast.makeText(context, R.string.flashlight_toggle_msg, Toast.LENGTH_SHORT).show();
			toggleCameraFlash(context);
		}
		else
		{
			Toast.makeText(context, R.string.flashlight_not_found, Toast.LENGTH_SHORT).show();
			toggleScreen();
		}
	}

	private void toggleCameraFlash(Context context)
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
		SharedPreferences settings = getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		int usageCount = settings.getInt(PREF_USAGE_COUNT, 0) + 1;
		editor.putInt(PREF_USAGE_COUNT, usageCount);
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
