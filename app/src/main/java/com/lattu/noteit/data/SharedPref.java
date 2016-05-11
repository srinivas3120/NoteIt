package com.lattu.noteit.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

	private static final String PREFKEY = "notes";
	private SharedPreferences notePrefs;
	
	public SharedPref(Context context) {
		notePrefs = context.getSharedPreferences(PREFKEY, Context.MODE_PRIVATE);
	}
	
	public boolean update(SettingItem note) {
		
		if (note.getText().length() > 0) {
			SharedPreferences.Editor editor = notePrefs.edit();
			editor.putString(note.getKey(), note.getText());
			editor.commit();
			return true;
		}
		else {
			return remove(note);
		}
		
	}
	
	public boolean remove(SettingItem note) {
		
		if (notePrefs.contains(note.getKey())) {
			SharedPreferences.Editor editor = notePrefs.edit();
			editor.remove(note.getKey());
			editor.commit();
		}
		
		return true;
	}

}
