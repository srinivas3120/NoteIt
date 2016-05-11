package com.lattu.noteit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lattu.noteit.db.DBNoteItem;
import com.lattu.noteit.db.NoteItDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.Html;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditNoteItemActivity extends Activity {

	private LinearLayout mainLinLayout;
	private LinearLayout mainTitleLinLayout;
	private RelativeLayout dateRelLayout;
	private TextView createdDate;
	private TextView modified_date;
	private LinearLayout titleLinLayout;
	private TextView noteTitleText;
	private EditText noteTitle;
	private RelativeLayout noteTagLinLayout;
	private TextView noteTagsText;
	private Spinner noteTags;
	private LinearLayout mainRemindLinLayout;
	private Switch remindMe;
	private LinearLayout remindLinLayout;
	private TextView remainderText;
	private Button notifyDate;
	private Button notifyTime;
	private TextView repeatText;
	private Spinner repeat;
	private LinearLayout mainNoteDataLinLayout;
	private TextView noteDataText;
	private EditText noteData;
	// //////////
	private TextView viewCreatedDate;
	private TextView viewModified_date;

	private TextView viewNoteTitleText;
	private TextView viewNoteTitle;
	private TextView viewNoteTagsText;
	private TextView viewRemainderText;

	private TextView viewNoteDataText;
	private TextView viewNoteData;
	private LinearLayout viewMainLinLayout;

	// //////////////
	private Button insertContacts;
	private static final int CONTACT_PICKER_RESULT = 2001;
	private static final int EMAIL_PICKER_RESULT = 2002;
	
	private Button insertEmail;
	// /////////////
	private MenuItem save;
	private MenuItem delete;
	private MenuItem edit;

	private DBNoteItem noteItem;
	private boolean EDIT_MODE;

	int position, month, year, dom, hour, min;
	Calendar cal;
	String[] dateTime = new String[2];

	private int isNotify;
	private String nDate;
	private String nTime;
	private int noteTagId = 1;

	NoteItDataSource dbDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_noteitem);
		initializeViews();
		dbDatasource = new NoteItDataSource(this);
		dbDatasource.open();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		save = (MenuItem) findViewById(R.id.action_save);
		delete = (MenuItem) findViewById(R.id.action_delete);
		edit = (MenuItem) findViewById(R.id.action_edit);

		noteItem = new DBNoteItem();
		Intent intent = this.getIntent();
		Bundle b = intent.getExtras();
		noteItem = b.getParcelable(".db.DBNoteItem");
		EDIT_MODE = intent.getBooleanExtra("EDIT_MODE", false);
		if (!EDIT_MODE) {
			viewNotes();
		} else {
			mainLinLayout.setVisibility(View.VISIBLE);
			viewMainLinLayout.setVisibility(View.GONE);
			setDBNoteItemTolayout();
			editNoteItem();
		}

	}

	private void viewNotes() {
		mainLinLayout.setVisibility(View.GONE);
		viewMainLinLayout.setVisibility(View.VISIBLE);

		viewCreatedDate.setText("Created: " + noteItem.getNoteCreatedDate());
		viewModified_date
				.setText("Modified: " + noteItem.getNoteModifiedDate());

		viewNoteTitle.setText(noteItem.getNoteTitle());
		// noteTitle.setSelection(noteItem.getNoteTitle().length());
		viewNoteTagsText.setText("Tag : "
				+ dbDatasource.getTagType(noteItem.getNoteRelationNoteTagId()));

		if (noteItem.getNoteRelationIsNotify() == 1) {
			viewRemainderText.setText("Remainder : "
					+ noteItem.getNoteRelationNotifyDate());
		} else
			viewRemainderText.setText("No Remainder's");

		viewNoteData.setText(noteItem.getNoteData());
		// noteData.setSelection(noteItem.getNoteData().length());

	}

	private void editNoteItem() {

		insertContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
						Contacts.CONTENT_URI);
				contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(contactPickerIntent,
						CONTACT_PICKER_RESULT);
			}
		});
		
		insertEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
						Contacts.CONTENT_URI);
				contactPickerIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE);
				startActivityForResult(contactPickerIntent,
						EMAIL_PICKER_RESULT);
			}
		});

		setTagsToNoteItem();
		setRemindMeToNoteItem();
		setDateTimeToNoteItem();
	}

	private void saveNoteItem() {
		Intent intent = new Intent();
		noteItem.setNoteRelationNoteTagId(noteTagId);

		noteItem.setNoteTitle(noteTitle.getText().toString());
		noteItem.setNoteData(noteData.getText().toString());

		noteItem.setNoteRelationIsNotify(isNotify);
		if (isNotify == 1) {

			if (!nDate.equals("yyyy-MM-dd") && !nTime.equals("HH:mm")) {
				noteItem.setNoteRelationNotifyDate(nDate + " " + nTime);
			} else
				noteItem.setNoteRelationIsNotify(0);
		}

		if (noteItem.getNoteModifiedDate() != null) {
			noteItem.setNoteModifiedDate(getCurrentTime());
			intent.putExtra(".db.DBNoteItem", noteItem);
			// dbDatasource.updateNoteItem(noteItem);
			setResult(MainActivity.NOTE_UPDATE_ID, intent);
		} else {
			noteItem.setNoteModifiedDate(getCurrentTime());
			intent.putExtra(".db.DBNoteItem", noteItem);
			// dbDatasource.insertNoteItem(noteItem);
			setResult(MainActivity.NOTE_INSERT_ID, intent);
		}

		finish();
	}

	private String getCurrentTime() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(new Date());
	}

	private void deleteNoteItem() {
		Intent intent = new Intent();
		intent.putExtra(".db.DBNoteItem", noteItem);
		setResult(MainActivity.NOTE_DELETE_ID, intent);
		finish();
	}

	private void initializeViews() {
		mainLinLayout = (LinearLayout) findViewById(R.id.mainLinLayout);
		mainTitleLinLayout = (LinearLayout) findViewById(R.id.mainTitleLinLayout);
		dateRelLayout = (RelativeLayout) findViewById(R.id.dateRelLayout);
		createdDate = (TextView) findViewById(R.id.createdDate);
		modified_date = (TextView) findViewById(R.id.modified_date);
		titleLinLayout = (LinearLayout) findViewById(R.id.titleLinLayout);
		noteTitleText = (TextView) findViewById(R.id.noteTitleText);
		noteTitle = (EditText) findViewById(R.id.noteTitle);
		noteTagLinLayout = (RelativeLayout) findViewById(R.id.noteTagLinLayout);
		noteTagsText = (TextView) findViewById(R.id.noteTagsText);
		noteTags = (Spinner) findViewById(R.id.noteTags);
		mainRemindLinLayout = (LinearLayout) findViewById(R.id.mainRemindLinLayout);
		remindMe = (Switch) findViewById(R.id.remindMe);
		remindLinLayout = (LinearLayout) findViewById(R.id.remindLinLayout);
		remainderText = (TextView) findViewById(R.id.remainderText);
		notifyDate = (Button) findViewById(R.id.notifyDate);
		notifyTime = (Button) findViewById(R.id.notifyTime);
		repeatText = (TextView) findViewById(R.id.repeatText);
		repeat = (Spinner) findViewById(R.id.repeat);
		mainNoteDataLinLayout = (LinearLayout) findViewById(R.id.mainNoteDataLinLayout);
		noteDataText = (TextView) findViewById(R.id.noteDataText);
		noteData = (EditText) findViewById(R.id.noteData);

		viewCreatedDate = (TextView) findViewById(R.id.viewCreatedDate);
		viewModified_date = (TextView) findViewById(R.id.viewModified_date);
		viewNoteTitleText = (TextView) findViewById(R.id.viewNoteTitleText);
		viewNoteTitle = (TextView) findViewById(R.id.viewNoteTitle);
		viewNoteTagsText = (TextView) findViewById(R.id.viewNoteTagsText);
		viewRemainderText = (TextView) findViewById(R.id.viewRemainderText);
		viewNoteDataText = (TextView) findViewById(R.id.viewNoteDataText);
		viewNoteData = (TextView) findViewById(R.id.viewNoteData);
		viewMainLinLayout = (LinearLayout) findViewById(R.id.viewMainLinLayout);

		insertContacts = (Button) findViewById(R.id.browseContacts);
		insertEmail=(Button) findViewById(R.id.browseEmail);

	}

	private void setDBNoteItemTolayout() {
		createdDate.setText("Created: " + noteItem.getNoteCreatedDate());
		modified_date.setText("Modified: " + noteItem.getNoteModifiedDate());

		noteTitle.setText(noteItem.getNoteTitle());
		// noteTitle.setSelection(noteItem.getNoteTitle().length());

		List<Long> tagIds = dbDatasource.findTagIds();
		noteTags.setSelection(
				tagIds.indexOf(noteItem.getNoteRelationNoteTagId()), true);
		isNotify = noteItem.getNoteRelationIsNotify();
		remindMe.setChecked(isNotify == 1);

		if (noteItem.getNoteRelationNotifyDate() != null) {
			dateTime = noteItem.getNoteRelationNotifyDate().split(" ", 2);
			nDate = dateTime[0];
			nTime = dateTime[1];
			notifyDate.setText(nDate);
			notifyTime.setText(nTime);

		}

		noteData.setText(noteItem.getNoteData());
		// noteData.setSelection(noteItem.getNoteData().length());

	}

	private void setRemindMeToNoteItem() {

		if (remindMe != null) {
			remindMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						isNotify = 1;

					} else {
						isNotify = 0;

					}
				}
			});
		}
	}

	private void setDateTimeToNoteItem() {

		notifyDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal = Calendar.getInstance();
				dom = cal.get(Calendar.DAY_OF_MONTH);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH);

				DatePickerDialog dpd = new DatePickerDialog(
						EditNoteItemActivity.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int month, int dom) {

								String month1 = null;
								month = month + 1;

								if (month / 10 == 0) {
									month1 = "0" + month;
								} else
									month1 = "" + month;

								String dom1 = null;

								if (dom / 10 == 0) {
									dom1 = "0" + dom;
								} else
									dom1 = "" + dom;

								nDate = year + "-" + month1 + "-" + dom1;
								notifyDate.setText(nDate);
							}
						}, year, month, dom);
				dpd.show();

			}
		});

		notifyTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cal = Calendar.getInstance();
				min = cal.get(Calendar.MINUTE);
				hour = cal.get(Calendar.HOUR);

				TimePickerDialog tpd = new TimePickerDialog(
						EditNoteItemActivity.this, new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view, int hour,
									int min) {

								String min1 = null;
								if (min / 10 == 0) {
									min1 = "0" + min;
								} else
									min1 = "" + min;

								String hour1 = null;
								if (hour / 10 == 0) {
									hour1 = "0" + hour;
								} else
									hour1 = "" + hour;

								nTime = hour1 + ":" + min1;
								notifyTime.setText(nTime);
							}
						}, hour, min, false);
				tpd.show();
			}
		});

	}

	private void setRepeatToNoteItem() {
		List<String> list = new ArrayList<String>();
		list = dbDatasource.findTags();

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list) {

		};

		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noteTags.setAdapter(spinnerAdapter);
		noteTags.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int noteRelationNoteTagId = dbDatasource.getNoteTagId(parent
						.getItemAtPosition(position).toString());

				noteItem.setNoteRelationNoteTagId(noteRelationNoteTagId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setTagsToNoteItem() {
		List<String> list = new ArrayList<String>();
		list = dbDatasource.findTags();

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list) {

		};
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noteTags.setAdapter(spinnerAdapter);
		String tagType = dbDatasource.getTagType(noteItem
				.getNoteRelationNoteTagId());
		if (tagType != null)
			noteTags.setSelection(list.indexOf(tagType));
		noteTags.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				noteTagId = dbDatasource.getNoteTagId(parent.getItemAtPosition(
						position).toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_save:
			saveNoteItem();
			return true;
		case R.id.action_delete:
			deleteNoteItem();
			return true;
		case R.id.action_edit:
			EDIT_MODE = true;
			invalidateOptionsMenu();
			mainLinLayout.setVisibility(View.VISIBLE);
			viewMainLinLayout.setVisibility(View.GONE);
			setDBNoteItemTolayout();
			editNoteItem();
			// editNotes();
			return true;
		default:
			return false;
		}

	}

	/*
	 * private void editNotes() { noteTextView.setVisibility(View.GONE); et=
	 * (EditText) findViewById(R.id.noteText); et.setVisibility(View.VISIBLE);
	 * et.setText(noteItem.getNoteData());
	 * et.setSelection(noteItem.getNoteData().length()); }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_edit, menu);
		if (EDIT_MODE) {
			menu.findItem(R.id.action_save).setVisible(true);
			menu.findItem(R.id.action_delete).setVisible(true);
			menu.findItem(R.id.action_edit).setVisible(false);

		} else {
			menu.findItem(R.id.action_save).setVisible(false);
			menu.findItem(R.id.action_delete).setVisible(true);
			menu.findItem(R.id.action_edit).setVisible(true);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri result;
			String id;
			switch (requestCode) {
			
			case CONTACT_PICKER_RESULT:
				result = data.getData();
				id = result.getLastPathSegment();
				Log.i("CONTACT_PICKER_RESULT", "LastPathSegment:" + id
						+ " Got a result: " + result.toString());
				//getContactDetails(id);
				getContactDetails(result);
				break;
			case EMAIL_PICKER_RESULT:
				result = data.getData();
				id = result.getLastPathSegment();
				Log.i("EMAIL_PICKER_RESULT", "LastPathSegment:" + id
						+ " Got a result: " + result.toString());
				//getEmailDetails(id);
				getEmailDetails(result);
				break;
			}

		} else {
			Log.i("CONTACT_PICKER_RESULT", "Warning: activity result not ok");
		}
	}

	private void getEmailDetails(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, 
				new String[] { Email.DISPLAY_NAME, Email.ADDRESS, Email.LABEL }, null, null, null);
		PhoneItem phoneItem = new PhoneItem();
		if(cursor.moveToFirst()){
				
				phoneItem.setDisplayName(cursor.getString(cursor
						.getColumnIndex(Email.DISPLAY_NAME)));
				phoneItem.setNumber(cursor.getString(cursor
						.getColumnIndex(Email.ADDRESS)));
				phoneItem.setLabel(cursor.getString(cursor
						.getColumnIndex(Email.LABEL)));
			}
		

			//String notes = noteData.getText().toString();
			String notes=Html.toHtml(noteData.getText());
			noteData.setText(Html.fromHtml(notes + "\n" + phoneItem.toEmail()));

	}

	private void getEmailDetails(String id) {
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI,
				new String[] { Email.DISPLAY_NAME,Email.ADDRESS,Email.LABEL },
				Data.CONTACT_ID + "=?", new String[] { String.valueOf(id) },
				null);

		ArrayList<PhoneItem> phonesList = new ArrayList<PhoneItem>();

		cursor.moveToFirst();
		String columns[] = cursor.getColumnNames();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				PhoneItem phoneItem = new PhoneItem();
				phoneItem.setDisplayName(cursor.getString(cursor
						.getColumnIndex(Email.DISPLAY_NAME)));
				phoneItem.setNumber(cursor.getString(cursor
						.getColumnIndex(Email.ADDRESS)));
				phoneItem.setLabel(cursor.getString(cursor
						.getColumnIndex(Email.LABEL)));

				/*
				 * int index = cursor.getColumnIndex(column);
				 * Log.i("getContactDetails", "Column: " + column + " == [" +
				 * cursor.getString(index) + "]");
				 */
				phonesList.add(phoneItem);

			}
		}

		if (phonesList.size() == 0) {
			Toast.makeText(this,
					"This contact does not contacin any emails",
					Toast.LENGTH_LONG).show();
		} else if (phonesList.size() == 1) {
			String notes = noteData.getText().toString();
			noteData.setText(notes + "\n" + phonesList.get(0).toEmail());
			// toET.setText(phonesList.get(0));
		} else {
			/*
			 * String notes=noteData.getText().toString();
			 * noteData.setText(notes+"\n"+phonesList.get(0));
			 */
			final String[] phonesArr = new String[phonesList.size()];
			for (int i = 0; i < phonesList.size(); i++) {
				phonesArr[i] = phonesList.get(i).toEmail();
			}

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Select Email");
			dialog.setItems(phonesArr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String selectedPhoneItem = phonesArr[which];
					String notes = noteData.getText().toString();
					noteData.setText(notes + "\n" + selectedPhoneItem);
				}
			}).create();
			dialog.show();
		}
	}
	
	private void getContactDetails(Uri uri){
		Cursor cursor = getContentResolver().query(uri, 
				new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Phone.LABEL }, null, null, null);
		PhoneItem phoneItem = new PhoneItem();
		if(cursor.moveToFirst()){
				
				phoneItem.setDisplayName(cursor.getString(cursor
						.getColumnIndex(Phone.DISPLAY_NAME)));
				phoneItem.setNumber(cursor.getString(cursor
						.getColumnIndex(Phone.NUMBER)));
				phoneItem.setLabel(cursor.getString(cursor
						.getColumnIndex(Phone.LABEL)));
			}
		

			//String notes = noteData.getText().toString();
			String notes=Html.toHtml(noteData.getText());
			noteData.setText(Html.fromHtml(notes + "\n" + phoneItem));

	}

	private void getContactDetails(String id) {
		
		Cursor cursor = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Phone.LABEL },
				Data.CONTACT_ID + "=?", new String[] { String.valueOf(id) },
				null);

		ArrayList<PhoneItem> phonesList = new ArrayList<PhoneItem>();

		cursor.moveToFirst();
		String columns[] = cursor.getColumnNames();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				PhoneItem phoneItem = new PhoneItem();
				phoneItem.setDisplayName(cursor.getString(cursor
						.getColumnIndex(Phone.DISPLAY_NAME)));
				phoneItem.setNumber(cursor.getString(cursor
						.getColumnIndex(Phone.NUMBER)));
				phoneItem.setLabel(cursor.getString(cursor
						.getColumnIndex(Phone.LABEL)));

				/*
				 * int index = cursor.getColumnIndex(column);
				 * Log.i("getContactDetails", "Column: " + column + " == [" +
				 * cursor.getString(index) + "]");
				 */
				phonesList.add(phoneItem);

			}
		}

		if (phonesList.size() == 0) {
			Toast.makeText(this,
					"This contact does not contacin any phone numbers",
					Toast.LENGTH_LONG).show();
		} else if (phonesList.size() == 1) {
			String notes = noteData.getText().toString();
			noteData.setText(notes + "\n" + phonesList.get(0));
			// toET.setText(phonesList.get(0));
		} else {
			/*
			 * String notes=noteData.getText().toString();
			 * noteData.setText(notes+"\n"+phonesList.get(0));
			 */
			final String[] phonesArr = new String[phonesList.size()];
			for (int i = 0; i < phonesList.size(); i++) {
				phonesArr[i] = phonesList.get(i).toString();
			}

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Select Contact");
			dialog.setItems(phonesArr, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String selectedPhoneItem = phonesArr[which];
					String notes = noteData.getText().toString();
					noteData.setText(notes + "\n" + selectedPhoneItem);
				}
			}).create();
			dialog.show();
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(".db.DBNoteItem", noteItem);
		setResult(RESULT_OK, intent);
		finish();
	}

}
