package com.lattu.noteit.receivers;



import com.lattu.noteit.ViewNoteItemActivity;
import com.lattu.noteit.db.DBNoteItem;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	 NotificationManager nm;
	 DBNoteItem noteItem;
	 @Override
	 public void onReceive(Context context, Intent intent) {
	  nm = (NotificationManager) context
	    .getSystemService(Context.NOTIFICATION_SERVICE);
	  
	  Bundle b= intent.getExtras();
	  noteItem=b.getParcelable(".view.DBNoteItem");
	  
	  Log.i("onReceiver","onReceiver: "+noteItem.getNoteId()+","+noteItem.getNoteTitle()+","+
				noteItem.getNoteData()+","+noteItem.getNoteCreatedDate()+","+
				noteItem.getNoteModifiedDate()+","+noteItem.getNoteAttachmentLink()+","+
				noteItem.getNoteRelationId()+","+noteItem.getNoteRelationNoteId()+","+
				noteItem.getNoteRelationNoteTagId()+","+noteItem.getNoteRelationIsNotify()+","+
				noteItem.getNoteRelationNotifyDate());
	  
	  CharSequence from = noteItem.getNoteTitle();
	  CharSequence message = noteItem.getNoteData();
	  int requestID = noteItem.getNoteId();
	  
	  
	  PendingIntent contentIntent = PendingIntent.getActivity(context, requestID,
	    new Intent(context,ViewNoteItemActivity.class).putExtra(".receiver.DBNoteItem", noteItem), 
	    					android.app.PendingIntent.FLAG_UPDATE_CURRENT);
	  Notification notif = new Notification(com.lattu.noteit.R.drawable.ic_launcher,
			  message, System.currentTimeMillis());
	  notif.setLatestEventInfo(context, from, message, contentIntent);
	  notif.flags |= Notification.FLAG_AUTO_CANCEL;
	  notif.defaults |= Notification.DEFAULT_SOUND;
	  notif.defaults |= Notification.DEFAULT_VIBRATE;
	  nm.notify(requestID, notif);
	  
	 }
}
