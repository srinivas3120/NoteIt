package com.lattu.noteit;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.settings);
		ListPreference lp = (ListPreference)findPreference("pref_viewType");
		CharSequence[] entries = { "List View", "Grid View" };
		CharSequence[] entryValues = { "1", "2"};
		lp.setEntries(entries);
		lp.setEntryValues(entryValues);
	}
	@Override
	public void onBackPressed() {
		finish();	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return false;	
		}
	}
}
