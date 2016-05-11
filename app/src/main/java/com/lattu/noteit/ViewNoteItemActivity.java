package com.lattu.noteit;

import com.lattu.noteit.db.DBNoteItem;
import com.lattu.noteit.db.NoteItDataSource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class ViewNoteItemActivity extends Activity {
	
	private DBNoteItem noteItem;
	
	private TextView viwCreatedDate;
	   private TextView viwModified_date;

	   private TextView viwNoteTitleText;
	   private TextView viwNoteTitle;
	   private TextView viwNoteTagsText;
	   private TextView viwRemainderText;
	   
	   private TextView viwNoteDataText;
	   private TextView viwNoteData;
	   private LinearLayout viwMainLinLayout;
	   
	   NoteItDataSource dbDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_view_noteitem);
		
		dbDatasource=new NoteItDataSource(this);
		dbDatasource.open();
		
		initializeViews();
		noteItem = new DBNoteItem();
		Intent intent = this.getIntent();
		Bundle b= intent.getExtras();
		noteItem=b.getParcelable(".receiver.DBNoteItem");
		viewNotes();
		
	}
	
	
	private void viewNotes() {
		
		
		viwCreatedDate.setText("Created: "+noteItem.getNoteCreatedDate());
		viwModified_date.setText("Modified: "+noteItem.getNoteModifiedDate());
		
		viwNoteTitle.setText(noteItem.getNoteTitle());
		//noteTitle.setSelection(noteItem.getNoteTitle().length());
		viwNoteTagsText.setText("Tag : "+dbDatasource.getTagType(noteItem.getNoteRelationNoteTagId()));
		
		if(noteItem.getNoteRelationIsNotify()==1){
			viwRemainderText.setText("Remainder : "+noteItem.getNoteRelationNotifyDate());	
		}
		else
			viwRemainderText.setText("No Remainder's");	
		
		viwNoteData.setText(noteItem.getNoteData());
		//noteData.setSelection(noteItem.getNoteData().length());
	
	}
	
	
	private void initializeViews(){
			
		
		viwCreatedDate=(TextView)findViewById(R.id.viwCreatedDate);
		viwModified_date=(TextView)findViewById(R.id.viwModified_date);
		viwNoteTitleText=(TextView)findViewById(R.id.viwNoteTitleText);
		viwNoteTitle=(TextView)findViewById(R.id.viwNoteTitle);
		viwNoteTagsText=(TextView)findViewById(R.id.viwNoteTagsText);
		viwRemainderText=(TextView)findViewById(R.id.viwRemainderText);
		viwNoteDataText=(TextView)findViewById(R.id.viwNoteDataText);
		viwNoteData=(TextView)findViewById(R.id.viwNoteData);
		viwMainLinLayout=(LinearLayout) findViewById(R.id.viwMainLinLayout);
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_home_viw:
			onBackPressed();
			return true;
		case R.id.action_delete_viw:
			onDelete();
			return true;
		default:
			return false;
		}
		
	}
	
	private void onDelete() {
		dbDatasource.removeNoteItem(noteItem);
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();	
	}


	@Override
	public void onBackPressed() {
		Log.i("onBackPressed","before onBackPressed: "+noteItem.getNoteId()+","+noteItem.getNoteTitle()+","+
				noteItem.getNoteData()+","+noteItem.getNoteCreatedDate()+","+
				noteItem.getNoteModifiedDate()+","+noteItem.getNoteAttachmentLink()+","+
				noteItem.getNoteRelationId()+","+noteItem.getNoteRelationNoteId()+","+
				noteItem.getNoteRelationNoteTagId()+","+noteItem.getNoteRelationIsNotify()+","+
				noteItem.getNoteRelationNotifyDate());
		noteItem.setNoteRelationIsNotify(0);
		Log.i("onBackPressed","onBackPressed: "+noteItem.getNoteId()+","+noteItem.getNoteTitle()+","+
				noteItem.getNoteData()+","+noteItem.getNoteCreatedDate()+","+
				noteItem.getNoteModifiedDate()+","+noteItem.getNoteAttachmentLink()+","+
				noteItem.getNoteRelationId()+","+noteItem.getNoteRelationNoteId()+","+
				noteItem.getNoteRelationNoteTagId()+","+noteItem.getNoteRelationIsNotify()+","+
				noteItem.getNoteRelationNotifyDate());

		dbDatasource.updateNoteItem(noteItem);
		
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_view, menu);
		return true;
	}

	
}
