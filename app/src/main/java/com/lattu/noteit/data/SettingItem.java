package com.lattu.noteit.data;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingItem {

	private String key;
	private String text;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	/*@SuppressLint("SimpleDateFormat")
	public static SettingItem getNew() {
		
		Locale locale = new Locale("en_US");
		Locale.setDefault(locale);

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String key = formatter.format(new Date());
		
		SettingItem note = new SettingItem();
		note.setKey(key);
		note.setText("");
		return note;
		
	}*/
	
	@Override
	public String toString() {
		return this.getText();
	}
	
}
