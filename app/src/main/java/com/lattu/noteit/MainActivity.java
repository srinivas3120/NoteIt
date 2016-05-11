package com.lattu.noteit;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lattu.noteit.data.SharedPref;
import com.lattu.noteit.data.SettingItem;
import com.lattu.noteit.db.DBNoteItem;
import com.lattu.noteit.db.NoteItDBOpenHelper;
import com.lattu.noteit.db.NoteItDataSource;
import com.lattu.noteit.receivers.AlarmReceiver;

import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final int EDITOR_ACTIVITY_REQUEST = 1001;
	private static final int MENU_DELETE_ID=1002;
	public static final int NOTE_DELETE_ID=1003;
	public static final int NOTE_UPDATE_ID=1004;
	public static final int NOTE_INSERT_ID=1005;
	private int currentNoteId;
	private ListView mainList;
	public ListView drawerList;
	public LinearLayout drawerLinear;
	public LinearLayout drawerProfile;
	private MainAdapter mainAdapter;
	private DrawerAdapter drawerAdapter;
	private ActionBarDrawerToggle drawerListener;
	private List<String> noteTags;
	
	private ImageView drawerProfileHome;
	private ImageView drawerSettings;
	private TextView drawerProfileName;
	private ImageView notification_image;
	private LinearLayout notifLinLayout;
	
	private TextView notificationText;
	private TextView addTagText;
	
	private static final String PROFILE_NAME="PROFILE_NAME";
	private static final String ADD_TAG="ADD_TAG";
	public SharedPreferences settings;
	
	public List<DBNoteItem> dbNoteItems;
	private DrawerLayout drawerLayout;
	
	NoteItDataSource dbDatasource;
	AlarmManager am;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_activity);
		
		mainList=(ListView) findViewById(R.id.main_list);
		drawerList=(ListView) findViewById(R.id.drawerList);
		drawerLinear=(LinearLayout) findViewById(R.id.drawerLinear);
		drawerProfile=(LinearLayout) findViewById(R.id.drawerProfile);
		drawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
		
		drawerProfileHome=(ImageView) findViewById(R.id.drawer_profile_home);
		//drawerSettings=(ImageView) findViewById(R.id.drawerSettings);
		
		drawerProfileName=(TextView) findViewById(R.id.drawerProfileName);
		notification_image=(ImageView) findViewById(R.id.notification_image);
		notifLinLayout=(LinearLayout) findViewById(R.id.notifLinLayout);
		notificationText=(TextView) findViewById(R.id.notificationText);
		addTagText=(TextView) findViewById(R.id.addTagText);
		
		dbDatasource=new NoteItDataSource(this);
		dbDatasource.open();
		displayNotes();
		
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		setDrawerAdapter();
		
		registerForContextMenu(mainList);
		
		 Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
		
		/*FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
		//dataSource=new DataSource(this);
		
		registerForContextMenu(mainList);*/	
		
		/*drawerSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View va) {
				drawerLayout.closeDrawers();
				Intent intent=new Intent(this,SettingsActivity.class);
				startActivityForResult(intent, "1007");
			}
		});*/
		
		settings=PreferenceManager.getDefaultSharedPreferences(this);
		drawerProfileName.setText(settings.getString("username", "Mudavath Srinivas"));
		
		
		
		addTagText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,AddTagActivity.class);
				startActivity(intent);
			}
		});
		drawerProfileHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				List<DBNoteItem> dbNoteItems=dbDatasource.findAllNotes();
				MainActivity.this.getActionBar().setTitle("NoteIt");
				
				displayFilteredNotes(dbNoteItems);
			}
		});
		
		notificationText.setOnClickListener(new OnClickListener() {
			List<DBNoteItem> dbNoteItems;

			@Override
			public void onClick(View v) {
				drawerLayout.closeDrawers();
				List<DBNoteItem> dbNoteItems=dbDatasource.findAllNotifNotes();
				MainActivity.this.getActionBar().setTitle("Notifications");
				
				displayFilteredNotes(dbNoteItems);
				}
		});
	}
	
	
	private void setDrawerAdapter(){
		
		noteTags=dbDatasource.findTags();
		drawerAdapter=new DrawerAdapter(this,noteTags);
		drawerList.setAdapter(drawerAdapter);
		drawerList.setOnItemClickListener(drawerAdapter);

		drawerProfile.setOnClickListener(null);
		
		drawerListener=new ActionBarDrawerToggle(this,drawerLayout,
				R.drawable.ic_navigation_menu_white,R.string.drawer_open,R.string.drawer_close){
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				//drawerLayout.closeDrawer(drawerLinear);
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				//drawerLayout.openDrawer(drawerLinear);
				drawerProfileName.setText(settings.getString("username", "Mudavath Srinivas"));
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
			
			
		};
		drawerLayout.setDrawerListener(drawerListener);
		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}
	
	
	private void displayNotes() {

		dbNoteItems=dbDatasource.findAllNotes();
		mainAdapter=new MainAdapter(this, dbNoteItems);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(mainAdapter);
		
		setDrawerAdapter();
		
	}
	
	private void displayFilteredNotes(List<DBNoteItem> dbNoteItems) {
		
		mainAdapter=new MainAdapter(this, dbNoteItems);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(mainAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_create) {
			createNote();
		}
		if(id==R.id.action_settings){
			editSettings();
		}
		if(drawerListener.onOptionsItemSelected(item)){
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void editSettings() {
		Intent intent=new Intent(this,SettingsActivity.class);
		startActivity(intent);
		
	}


	private void createNote() {
		DBNoteItem note= DBNoteItem.getNew();

		Intent intent = new Intent(this,EditNoteItemActivity.class);

		intent.putExtra(".db.DBNoteItem",note);
		intent.putExtra("EDIT_MODE", true);
		startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
	}
	
		
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		  
		if(requestCode==EDITOR_ACTIVITY_REQUEST && resultCode==NOTE_INSERT_ID){
			DBNoteItem note=new DBNoteItem();
			Bundle b= data.getExtras();
			note=b.getParcelable(".db.DBNoteItem");
			Log.i("on delete","on delete: "+note.getNoteId()+","+note.getNoteTitle()+","+
					note.getNoteData()+","+note.getNoteCreatedDate()+","+
					note.getNoteModifiedDate()+","+note.getNoteAttachmentLink()+","+
					note.getNoteRelationId()+","+note.getNoteRelationNoteId()+","+
					note.getNoteRelationNoteTagId()+","+note.getNoteRelationIsNotify()+","+
					note.getNoteRelationNotifyDate());
			note=dbDatasource.insertNoteItem(note);

			if(note.getNoteRelationIsNotify()==1){
				setOneTimeAlarm(note);
			}
			displayNotes();
		}
		if(requestCode==EDITOR_ACTIVITY_REQUEST && resultCode==NOTE_DELETE_ID){
			DBNoteItem note=new DBNoteItem();
			Bundle b= data.getExtras();
			note=b.getParcelable(".db.DBNoteItem");
			dbDatasource.removeNoteItem(note);
			displayNotes();

		}
		if(requestCode==EDITOR_ACTIVITY_REQUEST && resultCode==NOTE_UPDATE_ID){
			DBNoteItem note=new DBNoteItem();
			Bundle b= data.getExtras();
			note=b.getParcelable(".db.DBNoteItem");
			Log.i("on update","on update: "+note.getNoteId()+","+note.getNoteTitle()+","+
					note.getNoteData()+","+note.getNoteCreatedDate()+","+
					note.getNoteModifiedDate()+","+note.getNoteAttachmentLink()+","+
					note.getNoteRelationId()+","+note.getNoteRelationNoteId()+","+
					note.getNoteRelationNoteTagId()+","+note.getNoteRelationIsNotify()+","+
					note.getNoteRelationNotifyDate());
			dbDatasource.updateNoteItem(note);
			if(note.getNoteRelationIsNotify()==1){
				setOneTimeAlarm(note);
			}
			displayNotes();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info=(AdapterContextMenuInfo) menuInfo;
		currentNoteId=(int) info.id;
		menu.add(0, MENU_DELETE_ID, 0, "Delete");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId()==MENU_DELETE_ID){
			DBNoteItem note=dbNoteItems.get(currentNoteId);
			dbDatasource.removeNoteItem(note);
			displayNotes();
		}
		
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		dbDatasource.open(); //to prevent db leak
	}
	@Override
	protected void onResume() {
		drawerProfileName.setText(settings.getString("username", "Mudavath Srinivas"));
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		dbDatasource.close();
	}
	
	public void setOneTimeAlarm(DBNoteItem note) {
		  Intent intent = new Intent(this, AlarmReceiver.class);
		  intent.putExtra(".view.DBNoteItem",note);
		  
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, note.getNoteId(),
		    intent, PendingIntent.FLAG_UPDATE_CURRENT);
		  
		  SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
	        Date date = null;
			try {
				date = fromFormat.parse(note.getNoteRelationNotifyDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("time",date+" and "+date.getTime()+"current "+System.currentTimeMillis());
			
			if(note.getNoteRelationIsNotify()==1){
				am.set(AlarmManager.RTC_WAKEUP,date.getTime(), pendingIntent);
			}
		  
		 }

		 

	public void setRepeatingAlarm() {
		  Intent intent = new Intent(this, AlarmReceiver.class);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
		    intent, PendingIntent.FLAG_CANCEL_CURRENT);
		  am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
		    (5 * 1000), pendingIntent);
		 }
	
	
	class MainAdapter extends BaseAdapter implements OnItemClickListener{
		List<DBNoteItem> noteList;
		private Context context;

		public MainAdapter(Context context,List<DBNoteItem> dbNoteItems){
			this.context=context;
			this.noteList=dbNoteItems;

		}
		@Override
		public int getCount() {
			return noteList.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return noteList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.list_item_main_layout, parent, false);
			}
			else{
				row=convertView;
			}
			TextView main_list_date=(TextView) row.findViewById(R.id.main_list_date);
			TextView main_list_item=(TextView) row.findViewById(R.id.main_list_item);
			TextView main_list_tag=(TextView) row.findViewById(R.id.main_list_tag);
			
			SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat toFormat = new SimpleDateFormat("dd MMMM HH:mm");
	        String date = null;
			try {
				date = toFormat.format(fromFormat.parse(noteList.get(position).getNoteModifiedDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			main_list_date.setText(date);
			main_list_item.setText(noteList.get(position).getNoteTitle());
			String noteTag=dbDatasource.getTagType(noteList.get(position).getNoteRelationNoteTagId());
			main_list_tag.setText(noteTag);

			return row;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			DBNoteItem note=noteList.get(position);
			Intent intent = new Intent(MainActivity.this,EditNoteItemActivity.class);
			intent.putExtra(".db.DBNoteItem",note);
			startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
			
		}
		
	}
	class DrawerAdapter extends BaseAdapter implements OnItemClickListener{
		Context context;
		List<String> noteTags;
		public DrawerAdapter(Context context,List<String> noteTags){
			this.context=context;
			this.noteTags=noteTags;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return noteTags.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return noteTags.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.list_item_drawer_layout, parent, false);
			}
			else{
				row=convertView;
			}
			TextView drawer_list_text=(TextView) row.findViewById(R.id.drawer_list_text);
			ImageView drawer_list_image=(ImageView) row.findViewById(R.id.drawer_list_image);
			drawer_list_text.setText(noteTags.get(position));
			//drawer_list_image.setImageResource(R.drawable.ic_action_create);
			final int pos = position;
			drawer_list_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					drawerLayout.closeDrawers();
					List<DBNoteItem> dbNoteItems=dbDatasource.findAllNotes(dbDatasource.getNoteTagId(noteTags.get(pos).toString()));
					MainActivity.this.getActionBar().setTitle(noteTags.get(pos).toString());
					drawerList.setItemChecked(pos,true);
					displayFilteredNotes(dbNoteItems);
				}
			});
			
			return row;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//selectItem(position);
			//setTitle("lattu");
			
			/*List<DBNoteItem> dbNoteItems=dbDatasource.findAllNotes(noteTags.get(position));
			displayFilteredNotes(dbNoteItems);
			Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();*/
		}

		private void setTitle(String title) {
			MainActivity.this.getActionBar().setTitle(title);
			}
		public void selectItem(int position) {
			MainActivity.this.drawerList.setItemChecked(position, true);
		}
	
}

			
}