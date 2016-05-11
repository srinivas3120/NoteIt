package com.lattu.noteit.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteItDataSource {
	private static final String LOGTAG = "NOTEIT";
	private static final String[] noteitColumns={
		NoteItDBOpenHelper.COLUMN_NOTE_ID,
		NoteItDBOpenHelper.COLUMN_NOTE_TITLE,
		NoteItDBOpenHelper.COLUMN_NOTE_DATA,
		NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,
		NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,
		NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK};
	
	private static final String[] noteTagColumns={
		NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID,
		NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE};
	
	private static final String[] noteRelationColumns={
		NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID,
		NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID,
		NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID,
		NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY,
		NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE};
	
	SQLiteOpenHelper dbHelper;
	public static SQLiteDatabase database;
	Context context;
	
	public NoteItDataSource(Context context){
		dbHelper = NoteItDBOpenHelper.getHelper(context);
        this.context=context;
		open();
	}
	
	public void open(){
		if(dbHelper == null)
            dbHelper = NoteItDBOpenHelper.getHelper(context);
		database=dbHelper.getWritableDatabase(); //ref to connection to database. onCreate() method is triggered.
		Log.i(LOGTAG,"Database opened");
	}
	
	public int getNoteTagId(String noteTagType){
		String query="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_TAG+" where "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE+"='"+noteTagType+"'";
		Cursor cursor=database.rawQuery(query, null);
		Long tagId = null;
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				tagId=cursor.getLong(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID));
			}
			return tagId.intValue();
		}
		return 0;

	}
	
	public void close(){
		dbHelper.close();
		Log.i(LOGTAG,"Database closed");
	}
	
	public List<DBNoteItem> findAllNotifNotes(){
		List<DBNoteItem> dbNoteItems=new ArrayList<DBNoteItem>();
		String findNotes="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_TITLE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_DATA+","+NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE+","+NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_IT+" INNER JOIN "+
				NoteItDBOpenHelper.TABLE_NOTE_RELATION+" ON "+
				NoteItDBOpenHelper.COLUMN_NOTE_ID+"="+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" where "+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+"=1 order by 11 DESC,2 ASC";
		
		DBNoteItem dbNoteItem = null;
		Cursor cursor=database.rawQuery(findNotes, null);
		
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				dbNoteItem=new DBNoteItem();
				dbNoteItem.setNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ID)));
				dbNoteItem.setNoteTitle(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TITLE)));
				dbNoteItem.setNoteData(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_DATA)));
				dbNoteItem.setNoteCreatedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE)));
				dbNoteItem.setNoteModifiedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE)));
				dbNoteItem.setNoteAttachmentLink(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK)));
				
				
				dbNoteItem.setNoteRelationId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID)));
				dbNoteItem.setNoteRelationNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID)));
				dbNoteItem.setNoteRelationNoteTagId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID)));
				dbNoteItem.setNoteRelationIsNotify(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY)));
				dbNoteItem.setNoteRelationNotifyDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE)));			
				dbNoteItems.add(dbNoteItem);
			}
				
		}
			return dbNoteItems;
		
	}	
	
	public List<DBNoteItem> findAllNotes(int noteTagId ){
		List<DBNoteItem> dbNoteItems=new ArrayList<DBNoteItem>();
		/*String noteTagId="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+" from "+
		NoteItDBOpenHelper.TABLE_NOTE_TAG+" where "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE+"='"+noteTagType+"'";
		Cursor cursor=database.rawQuery(noteTagId, null);*/
		
		
		
		String findNotes="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_TITLE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_DATA+","+NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE+","+NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_IT+" INNER JOIN "+
				NoteItDBOpenHelper.TABLE_NOTE_RELATION+" ON "+
				NoteItDBOpenHelper.COLUMN_NOTE_ID+"="+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" where "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+"="+noteTagId+" order by 5 DESC,2 ASC";
		
		DBNoteItem dbNoteItem = null;
		Cursor cursor=database.rawQuery(findNotes, null);
		Log.i("findAllNotes","cursor"+cursor.getCount());
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				dbNoteItem=new DBNoteItem();
				dbNoteItem.setNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ID)));
				dbNoteItem.setNoteTitle(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TITLE)));
				dbNoteItem.setNoteData(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_DATA)));
				dbNoteItem.setNoteCreatedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE)));
				dbNoteItem.setNoteModifiedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE)));
				dbNoteItem.setNoteAttachmentLink(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK)));
				
				
				dbNoteItem.setNoteRelationId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID)));
				dbNoteItem.setNoteRelationNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID)));
				dbNoteItem.setNoteRelationNoteTagId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID)));
				dbNoteItem.setNoteRelationIsNotify(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY)));
				dbNoteItem.setNoteRelationNotifyDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE)));			
				dbNoteItems.add(dbNoteItem);
				Log.i("findAllNotes","cursor"+dbNoteItem.getNoteId()+","+dbNoteItem.getNoteTitle()+","+dbNoteItem.getNoteData()+","+dbNoteItem.getNoteCreatedDate()+","+dbNoteItem.getNoteModifiedDate()+","+dbNoteItem.getNoteAttachmentLink()+","+dbNoteItem.getNoteRelationId()+","+dbNoteItem.getNoteRelationNoteId()+","+dbNoteItem.getNoteRelationNoteTagId()+","+dbNoteItem.getNoteRelationIsNotify()+","+dbNoteItem.getNoteRelationNotifyDate());

				}
				
		}
			return dbNoteItems;
		
	}
	
	public List<DBNoteItem> findAllNotes(){
		List<DBNoteItem> dbNoteItems=new ArrayList<DBNoteItem>();
		
		String findNotes="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_TITLE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_DATA+","+NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+","+
				NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE+","+NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+","+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+","+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_IT+" INNER JOIN "+
				NoteItDBOpenHelper.TABLE_NOTE_RELATION+" ON "+
				NoteItDBOpenHelper.COLUMN_NOTE_ID+"="+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" order by 5 DESC,2 ASC";
		
		DBNoteItem dbNoteItem = null;
		Cursor cursor=database.rawQuery(findNotes, null);
		//Log.i("findAllNotes","cursor"+cursor.getCount());
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				dbNoteItem=new DBNoteItem();
				dbNoteItem.setNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ID)));
				dbNoteItem.setNoteTitle(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TITLE)));
				dbNoteItem.setNoteData(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_DATA)));
				dbNoteItem.setNoteCreatedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE)));
				dbNoteItem.setNoteModifiedDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE)));
				dbNoteItem.setNoteAttachmentLink(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK)));
				
				
				dbNoteItem.setNoteRelationId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID)));
				dbNoteItem.setNoteRelationNoteId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID)));
				dbNoteItem.setNoteRelationNoteTagId(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID)));
				dbNoteItem.setNoteRelationIsNotify(cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY)));
				dbNoteItem.setNoteRelationNotifyDate(cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE)));			
				
				dbNoteItems.add(dbNoteItem);
			}
				
							
		}
			return dbNoteItems;
		
	}
	
	public List<String> findTags(){
		List<String> tags = new ArrayList<String>();
		String allTagsQuery="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_TAG;
		Cursor cursor=database.rawQuery(allTagsQuery, null);
		
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				String tag=null;
				tag=cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE));
				tags.add(tag);
			}
		}
		return tags;						
	}
	
	public List<Long> findTagIds(){
		List<Long> tagIds = new ArrayList<Long>();
		String allTagsQuery="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_TAG;
		Cursor cursor=database.rawQuery(allTagsQuery, null);
		
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				Long tagId=null;
				tagId=cursor.getLong(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID));
				tagIds.add(tagId);
				Log.i("tagId","tagId:"+tagId);
			}
		}
		
		return tagIds;						
	}
	
	public DBNoteItem insertNoteItem(DBNoteItem dbNoteItem){	
		ContentValues values=new ContentValues();
				
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE,dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA,dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK,dbNoteItem.getNoteAttachmentLink());
				
		long insert_noteId=database.insert(NoteItDBOpenHelper.TABLE_NOTE_IT, null, values);
			Log.i("insert_noteId","insert_noteId: "+(int)insert_noteId);	
		if(insert_noteId!=-1){
			dbNoteItem.setNoteId((int)insert_noteId);

			values=new ContentValues();
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID,(int)insert_noteId);
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID,dbNoteItem.getNoteRelationNoteTagId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY,dbNoteItem.getNoteRelationIsNotify());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE,dbNoteItem.getNoteRelationNotifyDate());
			long insert_noteRelationId=database.insert(NoteItDBOpenHelper.TABLE_NOTE_RELATION,null,values);
			Log.i("insert_noteRelationId","insert_noteRelationId: "+insert_noteRelationId);	
			if(insert_noteRelationId!=-1){
				dbNoteItem.setNoteRelationNoteId((int)insert_noteId);
				dbNoteItem.setNoteRelationId((int)insert_noteRelationId);
				return dbNoteItem;
			}
		}
		return dbNoteItem;
		
	}
	
	public void removeNoteItem(DBNoteItem dbNoteItem){
		
		String removerFromNoteIt=NoteItDBOpenHelper.COLUMN_NOTE_ID+"="+dbNoteItem.getNoteId();
		database.delete(NoteItDBOpenHelper.TABLE_NOTE_IT, removerFromNoteIt, null);
		
		String removeFromNoteRelation=NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+"="+dbNoteItem.getNoteRelationId();
				
		database.delete(NoteItDBOpenHelper.TABLE_NOTE_RELATION, removeFromNoteRelation, null);
		
	}
	
	public boolean updateNoteItem(DBNoteItem dbNoteItem){
		
		String whereNoteId=NoteItDBOpenHelper.COLUMN_NOTE_ID+"="+dbNoteItem.getNoteId();
		ContentValues values=new ContentValues();		
		//values.put(NoteItDBOpenHelper.COLUMN_NOTE_ID,dbNoteItem.getNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE,dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA,dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK,dbNoteItem.getNoteAttachmentLink());

		
		int update_noteIt=database.update(NoteItDBOpenHelper.TABLE_NOTE_IT, values,whereNoteId,null);
		Log.i("update_noteIt","update_noteIt: "+update_noteIt);
		
			String whereNoteRelationId=NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+"="+dbNoteItem.getNoteRelationId();/*+" and "+
											NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+"="+dbNoteItem.getNoteRelationNoteId();*/
			values=new ContentValues();
			//values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID,dbNoteItem.getNoteRelationId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID,dbNoteItem.getNoteRelationNoteId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID,dbNoteItem.getNoteRelationNoteTagId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY,dbNoteItem.getNoteRelationIsNotify());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE,dbNoteItem.getNoteRelationNotifyDate());
			int update_noteRelation=database.update(NoteItDBOpenHelper.TABLE_NOTE_RELATION, values,whereNoteRelationId,null);
			
			Log.i("update_noteRelation","update_noteRelation"+update_noteRelation);

			return (update_noteRelation==1);

	}
	
	public boolean updateNoteItem(DBNoteItem dbNoteItem,String whereNoteIt,String whereNoteRelation){
		
		//String where=NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+"='"+dbNoteItem.getNoteCreatedDate()+"'";
		ContentValues values=new ContentValues();		
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ID,dbNoteItem.getNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE,dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA,dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK,dbNoteItem.getNoteAttachmentLink());

		
		int update_noteIt=database.update(NoteItDBOpenHelper.TABLE_NOTE_IT, values,whereNoteIt,null);
		
		if(update_noteIt==1){
			values=new ContentValues();
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID,dbNoteItem.getNoteRelationId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID,dbNoteItem.getNoteRelationNoteId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID,dbNoteItem.getNoteRelationNoteTagId());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY,dbNoteItem.getNoteRelationIsNotify());
			values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE,dbNoteItem.getNoteRelationNotifyDate());
			int update_noteRelation=database.update(NoteItDBOpenHelper.TABLE_NOTE_RELATION, values,whereNoteRelation,null);
			
			return (update_noteRelation==1);
		}
		return false;
	}
	
	public boolean updateNoteIt(DBNoteItem dbNoteItem,String whereNoteIt){	
		//String where=NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+"='"+dbNoteItem.getNoteCreatedDate()+"'";
		
		ContentValues values=new ContentValues();
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ID,dbNoteItem.getNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE,dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA,dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK,dbNoteItem.getNoteAttachmentLink());
		
		int update_noteIt=database.update(NoteItDBOpenHelper.TABLE_NOTE_IT, values,whereNoteIt,null);
						
		return (update_noteIt==1);
	}
	
	public void setNotification(DBNoteItem dbNoteItem){
		String updateNoteRelation="UPDATE "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+" SET "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+" = 1,"+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" = '"+dbNoteItem.getNoteRelationNotifyDate()+
				"' where "+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+" = "+dbNoteItem.getNoteRelationId();
		database.rawQuery(updateNoteRelation, null);
		
	}
	
	public void setNotification(Long noteRelationId,String noteRelationNotifyDate){
		String updateNoteRelation="UPDATE "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+" SET "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+" = 1,"+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" = '"+noteRelationNotifyDate+
				"' where "+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID+" = "+noteRelationId;
		database.rawQuery(updateNoteRelation, null);
		
	}
	
	public void removeNotification(Long noteRelationId){
		String updateNoteRelation="UPDATE "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+" SET "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+" = 0,"+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" = NULL where "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" = "+noteRelationId;
		database.rawQuery(updateNoteRelation, null);
		
		
	}
	
	public void removeNotification(DBNoteItem dbNoteItem){
		String updateNoteRelation="UPDATE "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+" SET "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY+" = 0,"+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE+" = NULL where "+
				NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" = "+dbNoteItem.getNoteRelationNoteId();
		database.rawQuery(updateNoteRelation, null);
		
		
	}
		
/*	public boolean update(DBNoteItem dbNoteItem){
		String where=NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+"='"+dbNoteItem.getNoteCreatedDate()+"'";
		ContentValues values=new ContentValues();
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ID, dbNoteItem.getNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE, dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA, dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE, dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE, dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK, dbNoteItem.getNoteAttachmentLink());
		
		int result= database.update(NoteItDBOpenHelper.TABLE_NOTE_IT, values, where, null);
		return true;
	}*/
		
	/*public DBNoteItem insertNoteIt(DBNoteItem dbNoteItem){	
		ContentValues values=new ContentValues();
				
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ID,dbNoteItem.getNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TITLE,dbNoteItem.getNoteTitle());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_DATA,dbNoteItem.getNoteData());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE,dbNoteItem.getNoteCreatedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_MODIFIED_DATE,dbNoteItem.getNoteModifiedDate());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_ATTACHMENT_LINK,dbNoteItem.getNoteAttachmentLink());
		
		long insert_noteId=database.insert(NoteItDBOpenHelper.TABLE_NOTE_IT, null, values);
		dbNoteItem.setNoteId(insert_noteId);
		
		return dbNoteItem;
	}*/
	
	public boolean insertNoteTag(String noteTagType){	
		ContentValues values=new ContentValues();
	
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE,noteTagType);
		
		long insert_noteTagId=database.insert(NoteItDBOpenHelper.TABLE_NOTE_TAG, null, values);
		
		return (insert_noteTagId!=-1);
	}

	public boolean updateNoteTag(int noteTagId,String noteTagType){	
		String whereNoteTag=NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+"="+noteTagId;
		
		ContentValues values=new ContentValues();
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE,noteTagType);
		
		int update_noteTag=database.update(NoteItDBOpenHelper.TABLE_NOTE_TAG, values,whereNoteTag,null);
				
		return (update_noteTag==1);
	}
	
	public boolean removeNoteTag(int noteTagId,boolean removeNotesWithIt){
		/*String noteNoteTagIdQuery="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+" from "+
				NoteItDBOpenHelper.TABLE_NOTE_TAG+" where "+
				NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE+"='"+noteTagType+"'";
		Cursor cursor=database.rawQuery(noteNoteTagIdQuery, null);
		int noteNoteTagId = 0;
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				noteNoteTagId=cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_ID));

			}
		}*/
		String where=NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+"="+noteTagId+"";
		int result= database.delete(NoteItDBOpenHelper.TABLE_NOTE_TAG,where, null);
		
		updateNoteRelationOnRemoveTag(noteTagId, removeNotesWithIt);
		
		return (result==1);
		
	}

	public void updateNoteRelationOnRemoveTag(int noteRelationNoteTagId,boolean removeNotesWithIt){	
		
		if(removeNotesWithIt){
			
			String inClause="SELECT "+ 
					NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID+" from "+
					NoteItDBOpenHelper.TABLE_NOTE_RELATION+" where "+
					NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+"="+noteRelationNoteTagId;
			
			String in = "";
			Cursor cursor=database.rawQuery(inClause, null);
			if(cursor.getCount()>0){ // getCount() gives no. of rows
				int a = 0;
				while(cursor.moveToNext()){
					a=cursor.getInt(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID));
					in=in+a+",";
				}
				in=in+a+"";
			}
			
			/*Log.i("removeNotesWithIt",""+removeNotesWithIt);*/
			String deleteFromNoteIt="DELETE FROM "+NoteItDBOpenHelper.TABLE_NOTE_IT+" where "+
					NoteItDBOpenHelper.COLUMN_NOTE_ID+" in ("+in+")";
			Log.i("deleteFromNoteIt",deleteFromNoteIt);
			database.rawQuery(deleteFromNoteIt, null);
						
			String deleteFromNoteRelation="DELETE from "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+
					" where "+NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+"="+
					noteRelationNoteTagId;
			database.rawQuery(deleteFromNoteRelation, null);
		}else{
			String updateNoteRelation="UPDATE "+NoteItDBOpenHelper.TABLE_NOTE_RELATION+" SET "+
					NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+" = NULL where "+
					NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID+" = "+noteRelationNoteTagId;
			database.rawQuery(updateNoteRelation, null);
		}
		
	}
			
	public boolean updateNoteRelation(DBNoteItem dbNoteItem,String whereNoteRelation){	
		//String where=NoteItDBOpenHelper.COLUMN_NOTE_CREATED_DATE+"='"+dbNoteItem.getNoteCreatedDate()+"'";
		
		ContentValues values=new ContentValues();
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_ID,dbNoteItem.getNoteRelationId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_ID,dbNoteItem.getNoteRelationNoteId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTE_TAG_ID,dbNoteItem.getNoteRelationNoteTagId());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_IS_NOTIFY,dbNoteItem.getNoteRelationIsNotify());
		values.put(NoteItDBOpenHelper.COLUMN_NOTE_RELATION_NOTIFY_DATE,dbNoteItem.getNoteRelationNotifyDate());
		int update_noteRelation=database.update(NoteItDBOpenHelper.TABLE_NOTE_RELATION, values,whereNoteRelation,null);
						
		return (update_noteRelation==1);
	}

	public String getTagType(int noteRelationNoteTagId) {
		String query="SELECT "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE+" from "+NoteItDBOpenHelper.TABLE_NOTE_TAG+
				" where "+NoteItDBOpenHelper.COLUMN_NOTE_TAG_ID+"="+noteRelationNoteTagId;
		Cursor cursor=database.rawQuery(query, null);
		
		String noteNoteTagType = null;
		if(cursor.getCount()>0){ // getCount() gives no. of rows
			while(cursor.moveToNext()){
				noteNoteTagType=cursor.getString(cursor.getColumnIndex(NoteItDBOpenHelper.COLUMN_NOTE_TAG_TYPE));

			} 
		}
		return noteNoteTagType;
	}
	
}
