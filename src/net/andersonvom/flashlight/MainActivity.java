package net.andersonvom.flashlight;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView screen = (TextView) findViewById(R.id.toggle_button);
		screen.setOnClickListener(this);
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
	}

	private void toggleScreen()
	{
	}

}
