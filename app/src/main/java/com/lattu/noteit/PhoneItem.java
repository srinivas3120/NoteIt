package com.lattu.noteit;

public class PhoneItem {
	String displayName;
	String number;
	String label;
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		
		return "\n"+number+" :<b>"+displayName+": "+label+"<\b>";
	}
	public String toEmail() {	
		return number+" :<b>"+label+"<\b>";
	}
}
