package net.andersonvom.flashlight;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
	public static final String PREF_RUN_BACKGROUND = "pref_run_background";
	public static final String PREF_USAGE_COUNT = "pref_usage_count";
	public static final String PREF_RATED_APP = "pref_rated_app";
	public static final int PREF_SUGGEST_MARKET_COUNT_FIRST = 10;
	public static final int PREF_SUGGEST_MARKET_COUNT_LAST = 50;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
	}
}
