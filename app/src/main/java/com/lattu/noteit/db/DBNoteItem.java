package com.lattu.noteit.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.lattu.noteit.MainActivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class DBNoteItem implements Parcelable{
	
	private int noteId;
	private String noteTitle;
	private String noteData;
	private String noteCreatedDate;
	private String noteModifiedDate;
	private String noteAttachmentLink;
	
/*	private long noteTagId;
	private String noteTagType;*/
	
	private int noteRelationId;
	private int noteRelationNoteId;
	private int noteRelationNoteTagId;
	private int noteRelationIsNotify;
	private String noteRelationNotifyDate;

	
	
public static DBNoteItem getNew() {
		
		Locale locale = new Locale("en_US");
		Locale.setDefault(locale);

		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String key = formatter.format(new Date());
		
		DBNoteItem note = new DBNoteItem();
		note.setNoteCreatedDate(key);
		note.setNoteRelationNotifyDate("yyyy-MM-dd HH:mm");
		note.setNoteAttachmentLink("/images");
		note.setNoteData("");
		return note;
		
	}
	
	
		
	public DBNoteItem(){
		
	}
	public DBNoteItem(Parcel in){
		noteId=in.readInt();
		noteTitle=in.readString();
		noteData=in.readString();
		noteCreatedDate=in.readString();
		noteModifiedDate=in.readString();
		noteAttachmentLink=in.readString();

/*		noteTagId=in.readLong();
		noteTagType=in.readString();*/

		noteRelationId=in.readInt();
		noteRelationNoteId=in.readInt();
		noteRelationNoteTagId=in.readInt();
		noteRelationIsNotify=in.readInt();
		noteRelationNotifyDate=in.readString(); 
	}
	
	@Override
	public int describeContents() {	
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(noteId);
		dest.writeString(noteTitle);
		dest.writeString(noteData);
		dest.writeString(noteCreatedDate);
		dest.writeString(noteModifiedDate);
		dest.writeString(noteAttachmentLink);

/*		dest.writeLong(noteTagId);
		dest.writeString(noteTagType);*/

		dest.writeInt(noteRelationId);
		dest.writeInt(noteRelationNoteId);
		dest.writeInt(noteRelationNoteTagId);
		dest.writeInt(noteRelationIsNotify);
		dest.writeString(noteRelationNotifyDate);
	}
	
	public static final Parcelable.Creator<DBNoteItem> CREATOR=
			new Parcelable.Creator<DBNoteItem>() {
				@Override
				public DBNoteItem createFromParcel(Parcel source) {
					return new DBNoteItem(source);
				} 

				@Override
				public DBNoteItem[] newArray(int size) {
					return new DBNoteItem[size];
				}
			};



	public int getNoteId() {
		return noteId;
	}



	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}



	public String getNoteTitle() {
		return noteTitle;
	}



	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}



	public String getNoteData() {
		return noteData;
	}



	public void setNoteData(String noteData) {
		this.noteData = noteData;
	}



	public String getNoteCreatedDate() {
		return noteCreatedDate;
	}



	public void setNoteCreatedDate(String noteCreatedDate) {
		this.noteCreatedDate = noteCreatedDate;
	}



	public String getNoteModifiedDate() {
		return noteModifiedDate;
	}



	public void setNoteModifiedDate(String noteModifiedDate) {
		this.noteModifiedDate = noteModifiedDate;
	}



	public String getNoteAttachmentLink() {
		return noteAttachmentLink;
	}



	public void setNoteAttachmentLink(String noteAttachmentLink) {
		this.noteAttachmentLink = noteAttachmentLink;
	}



/*	public long getNoteTagId() {
		return noteTagId;
	}



	public void setNoteTagId(long noteTagId) {
		this.noteTagId = noteTagId;
	}



	public String getNoteTagType() {
		return noteTagType;
	}



	public void setNoteTagType(String noteTagType) {
		this.noteTagType = noteTagType;
	}

*/

	public int getNoteRelationId() {
		return noteRelationId;
	}



	public void setNoteRelationId(int noteRelationId) {
		this.noteRelationId = noteRelationId;
	}



	public int getNoteRelationNoteId() {
		return noteRelationNoteId;
	}



	public void setNoteRelationNoteId(int noteRelationNoteId) {
		this.noteRelationNoteId = noteRelationNoteId;
	}



	public int getNoteRelationNoteTagId() {
		return noteRelationNoteTagId;
	}



	public void setNoteRelationNoteTagId(int noteRelationNoteTagId) {
		this.noteRelationNoteTagId = noteRelationNoteTagId;
	}



	public int getNoteRelationIsNotify() {
		return noteRelationIsNotify;
	}



	public void setNoteRelationIsNotify(int noteRelationIsNotify) {
		this.noteRelationIsNotify = noteRelationIsNotify;
	}



	public String getNoteRelationNotifyDate() {
		return noteRelationNotifyDate;
	}



	public void setNoteRelationNotifyDate(String noteRelationNotifyDate) {
		this.noteRelationNotifyDate = noteRelationNotifyDate;
	}

}
