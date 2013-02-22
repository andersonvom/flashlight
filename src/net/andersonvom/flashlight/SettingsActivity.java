package net.andersonvom.flashlight;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
	public static final String PREF_RUN_BACKGROUND = "pref_run_background";
	public static final String PREF_USAGE_COUNT = "pref_usage_count";
	public static final String PREF_RATED_APP = "pref_rated_app";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
	}
}
