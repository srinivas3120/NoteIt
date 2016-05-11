package com.lattu.noteit;

import java.util.ArrayList;
import java.util.List;

import com.lattu.noteit.db.DBNoteItem;
import com.lattu.noteit.db.NoteItDataSource;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddTagActivity extends Activity {
		
		private static final int MENU_DELETE_TAG_ID = 2001;
		private static final int MENU_UPDATE_TAG_ID = 2002;
		private ListView allNoteTags;
		private Button addTag;
		private EditText addTagEditText;
		private NoteItDataSource dbDatasource;
		
		private Spinner allNoteTagsSpinner;
		private EditText updateTagEditText;
		private Button updateTag;
		
		private int spinnernoteTagId;
		private int currentNoteId;
		
		
		private List<String> allTags;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			setContentView(R.layout.activity_add_tag);
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
			dbDatasource=new NoteItDataSource(this);
			dbDatasource.open();
			
			allNoteTags=(ListView) findViewById(R.id.allNoteTags);
			addTag=(Button) findViewById(R.id.addTag);
			addTagEditText=(EditText) findViewById(R.id.addTagEditText);
			
			allNoteTagsSpinner=(Spinner) findViewById(R.id.allNoteTagsSpinner);
			updateTagEditText=(EditText) findViewById(R.id.updateTagEditText);
			updateTag=(Button) findViewById(R.id.updateTag);
			
			registerForContextMenu(allNoteTags);
			
			allNoteTagsSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position,
						long id) {
					spinnernoteTagId = dbDatasource.getNoteTagId(parent.getItemAtPosition(position).toString());

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			updateTag.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String updateTag=updateTagEditText.getText().toString();
					Log.i("updateTag",updateTag);
					boolean success=dbDatasource.updateNoteTag(spinnernoteTagId,updateTag);
					if(success){
						//update notes tag
						Toast.makeText(getApplicationContext(), "'"+updateTag+"' Tag was Updated successfully", 
								   Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "Error !!! '"+updateTag+"' Tag can not be Updated", 
								   Toast.LENGTH_SHORT).show();
					}
					refreshTags();
				}
				
			});
			
			addTag.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String addTag=addTagEditText.getText().toString();
					boolean success=dbDatasource.insertNoteTag(addTag);
					if(success){
						Toast.makeText(getApplicationContext(), "'"+addTag+"' Tag was added successfully", 
								   Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(getApplicationContext(), "Error !!! '"+addTag+"' Tag can not be added", 
								   Toast.LENGTH_SHORT).show();
					}
					refreshTags();
				}
			});
			
			refreshTags();
						
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
		
		private void refreshTags() {
			allTags=dbDatasource.findTags();
			
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, allTags);
			spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			allNoteTagsSpinner.setAdapter(spinnerAdapter);
			
			
			allNoteTags.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allTags));
			allNoteTags.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
			AdapterContextMenuInfo info=(AdapterContextMenuInfo) menuInfo;
			currentNoteId=(int) info.id;
			
			menu.setHeaderTitle("Select The Action to delete Tag and Notes related to Tag permanently ");    
			menu.add(0, MENU_DELETE_TAG_ID, 0, "Delete");
		}
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			if(item.getItemId()==MENU_DELETE_TAG_ID){
				
				Toast.makeText(getApplicationContext(),"This Feature will be added soon !", 
						   Toast.LENGTH_SHORT).show();
				/*
				
				boolean success=dbDatasource.removeNoteTag(dbDatasource.getNoteTagId(allTags.get(currentNoteId)), true);
				if(success){
					Toast.makeText(getApplicationContext(), "'"+allTags.get(currentNoteId)+"' Tag was Removed successfully along with its Notes.", 
							   Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "Error !!! '"+allTags.get(currentNoteId)+"' Tag can not be Removed", 
							   Toast.LENGTH_SHORT).show();
				}
			*/}
			refreshTags();
			
			return super.onContextItemSelected(item);
		}
		
		@Override
		public void onBackPressed() {
			Intent intent=new Intent(this,MainActivity.class);
			startActivity(intent);
			finish();	
		}
		
		
}
