package com.lattu.noteit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteItDBOpenHelper extends SQLiteOpenHelper {
	
	private static final String LOGTAG = "NOTEIT";
	private static final String DATABASE_NAME = "noteit.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_NOTE_IT = "noteit";
	public static final String COLUMN_NOTE_ID = "noteId";
	public static final String COLUMN_NOTE_TITLE = "noteTitle";
	public static final String COLUMN_NOTE_DATA="noteData";
	public static final String COLUMN_NOTE_CREATED_DATE = "noteCreatedDate";
	public static final String COLUMN_NOTE_MODIFIED_DATE = "noteModifiedDate";
	public static final String COLUMN_NOTE_ATTACHMENT_LINK="noteAttachmentLink";
	
	public static final String TABLE_NOTE_TAG = "noteTag";
	public static final String COLUMN_NOTE_TAG_ID = "noteTagId";
	public static final String COLUMN_NOTE_TAG_TYPE = "noteTagType";
	
	public static final String TABLE_NOTE_RELATION = "noteRelation";
	public static final String COLUMN_NOTE_RELATION_ID = "noteRelationId";
	public static final String COLUMN_NOTE_RELATION_NOTE_ID = "noteRelationNoteId";
	public static final String COLUMN_NOTE_RELATION_NOTE_TAG_ID = "noteRelationNoteTagId";
	public static final String COLUMN_NOTE_RELATION_IS_NOTIFY = "noteRelationIsNotify";
	public static final String COLUMN_NOTE_RELATION_NOTIFY_DATE="noteRelationNotifyDate";
	
	
	
	private static final String TABLE_NOTE_IT_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_IT + " (" +
			COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NOTE_TITLE + " TEXT, " +
			COLUMN_NOTE_DATA + " TEXT, " +
			COLUMN_NOTE_CREATED_DATE + " DATETIME, " +
			COLUMN_NOTE_MODIFIED_DATE + " DATETIME, " +
			COLUMN_NOTE_ATTACHMENT_LINK + " TEXT " +		
			")";
	
	private static final String TABLE_NOTE_TAG_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_TAG + " (" +
			COLUMN_NOTE_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NOTE_TAG_TYPE + " TEXT UNIQUE " +		
			")";
	
	private static final String TABLE_NOTE_RELATION_CREATE = 
			"CREATE TABLE IF NOT EXISTS " + TABLE_NOTE_RELATION + " (" +
			COLUMN_NOTE_RELATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NOTE_RELATION_NOTE_ID + " INTEGER, " +
			COLUMN_NOTE_RELATION_NOTE_TAG_ID + " INTEGER, " +
			COLUMN_NOTE_RELATION_IS_NOTIFY + " INTEGER, " +
			COLUMN_NOTE_RELATION_NOTIFY_DATE + " DATETIME " +
			")";
	
	private static final String TABLE_NOTE_TAG_INSERT_SHOPING=
			"INSERT INTO "+TABLE_NOTE_TAG+" ("+COLUMN_NOTE_TAG_TYPE+") VALUES ('Personal')";

	private static final String TABLE_NOTE_TAG_INSERT_CALLS=
			"INSERT INTO "+TABLE_NOTE_TAG+" ("+COLUMN_NOTE_TAG_TYPE+") VALUES ('Calls')";
	

	private static final String TABLE_NOTE_TAG_INSERT_PERSONAL=
			"INSERT INTO "+TABLE_NOTE_TAG+" ("+COLUMN_NOTE_TAG_TYPE+") VALUES ('Office')";
	private static NoteItDBOpenHelper instance;
	
	public static synchronized NoteItDBOpenHelper getHelper(Context context) {
        if (instance == null)
            instance = new NoteItDBOpenHelper(context);
        return instance;
    }
	
	private NoteItDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_NOTE_IT_CREATE);
		db.execSQL(TABLE_NOTE_TAG_CREATE);
		db.execSQL(TABLE_NOTE_RELATION_CREATE);
		db.execSQL(TABLE_NOTE_TAG_INSERT_SHOPING);
		db.execSQL(TABLE_NOTE_TAG_INSERT_CALLS);
		db.execSQL(TABLE_NOTE_TAG_INSERT_PERSONAL);
		Log.i(LOGTAG,"Tables have been created");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " +TABLE_NOTE_IT);
		db.execSQL("DROP TABLE IF EXISTS " +TABLE_NOTE_TAG_CREATE);
		db.execSQL("DROP TABLE IF EXISTS " +TABLE_NOTE_RELATION_CREATE);
		onCreate(db);
	}

}
