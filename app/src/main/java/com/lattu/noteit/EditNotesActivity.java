package com.lattu.noteit;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.lattu.noteit.db.DBNoteItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditNotesActivity extends Activity {

	private DBNoteItem note;
	private TextView noteTextView;
	private EditText et;
	
	private MenuItem save;
	private MenuItem delete;
	private MenuItem edit;
	
	public boolean EDIT_MODE=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_notes);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		save = (MenuItem) findViewById(R.id.action_save);
		delete = (MenuItem) findViewById(R.id.action_delete);
		edit = (MenuItem) findViewById(R.id.action_edit);
		
		
		noteTextView=(TextView) findViewById(R.id.noteTextView);
		noteTextView.setVisibility(View.VISIBLE);
		
		note = new DBNoteItem();
		Intent intent = this.getIntent();
		Bundle b= intent.getExtras();
		note=b.getParcelable(".db.DBNoteItem");
		EDIT_MODE=intent.getBooleanExtra("EDIT_MODE", false);
		if(EDIT_MODE){
			editNotes();
		}
		note.setNoteCreatedDate(note.getNoteCreatedDate());
		note.setNoteData(note.getNoteData());
		
		noteTextView.setText(note.getNoteData());
						
	}

	
	private void saveAndFinish(){
		et= (EditText) findViewById(R.id.noteText);
		String noteText=et.getText().toString();
		Intent intent=new Intent();
		note.setNoteData(noteText);
		intent.putExtra(".db.DBNoteItem",note);
		setResult(RESULT_OK,intent);
		finish();
	}
	

	
	private void removeAndFinish() {
		et= (EditText) findViewById(R.id.noteText);
		String noteText=et.getText().toString();
		Intent intent=new Intent();
		note.setNoteData(noteText);
		intent.putExtra(".db.DBNoteItem",note);
		setResult(MainActivity.NOTE_DELETE_ID,intent);
		finish();	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_save:
			saveAndFinish();
			return true;
		case R.id.action_delete:
			removeAndFinish();
			return true;
		case R.id.action_edit:
			EDIT_MODE=true;
			invalidateOptionsMenu();
			editNotes();
			return true;
		default:
			return false;
		}
		
	}
	

	private void editNotes() {
		noteTextView.setVisibility(View.GONE);
		et= (EditText) findViewById(R.id.noteText);
		et.setVisibility(View.VISIBLE);
		et.setText(note.getNoteData());
		et.setSelection(note.getNoteData().length());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_edit, menu);
		if(EDIT_MODE){
			menu.findItem(R.id.action_save).setVisible(true);
			menu.findItem(R.id.action_delete).setVisible(true);
			menu.findItem(R.id.action_edit).setVisible(false);

		}else{
			menu.findItem(R.id.action_save).setVisible(false);			
			menu.findItem(R.id.action_delete).setVisible(true);
			menu.findItem(R.id.action_edit).setVisible(true);
		}
		return true;
	}
	
	
	@Override
	public void onBackPressed() {
		Intent intent=new Intent();
		intent.putExtra(".db.DBNoteItem",note);
		setResult(RESULT_OK,intent);
		finish();	
	}
	
}
