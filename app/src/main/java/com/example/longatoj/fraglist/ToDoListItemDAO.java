package com.example.longatoj.fraglist;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by longatoj on 10/7/2016.
 */
public class ToDoListItemDAO {
    private ArrayAdapter<ToDoListItem> arrayAdapter = null;
    private List<ToDoListItem> items;
    private final String TAG = "DAO";
    private static ToDoListItemDAO instance;
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public static ToDoListItemDAO getInstance(){
        if(instance == null)
            instance = new ToDoListItemDAO();
        return instance;
    }
    private ToDoListItemDAO() {
        items = new ArrayList<ToDoListItem>();


    }
    public void setAdapter(ArrayAdapter<ToDoListItem> arrayAdapter){
        this.arrayAdapter = arrayAdapter;
    }
    public void setDatabaseContext(Context context){
        logEvent("Setting database context");
        dbHelper = new SQLiteHelper(context);
    }
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    private void close() {
        dbHelper.close();
    }
    public void addItem(ToDoListItem i){
        logEvent("Adding item to DAO list");
        items.add(i);
        addItemToDatabase(i);

    }

    private void addItemToDatabase(ToDoListItem i) {
        logEvent("Adding item to database...");
        open();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DATETOCOMPLETE, i.getTimeToComplete().toString());
        values.put(SQLiteHelper.COLUMN_DESCRIPTION, i.getDescription());
        values.put(SQLiteHelper.COLUMN_PRIORITY, i.getPriority());
        values.put(SQLiteHelper.COLUMN_STATUS, i.getStatus());
        long insertId = database.insert(SQLiteHelper.TABLE, null, values);
        i.setId(insertId);
        Cursor cursor = database.query(SQLiteHelper.TABLE, SQLiteHelper.ALL_COLUMNS, SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        logEvent("Retrieved information for item with date: " + cursor.getString(1));
        cursor.moveToNext();
        close();
    }

    public void removeItem(ToDoListItem i){
        logEvent("Revmoing Item");
        items.remove(i);
        removeItemFromDatabase(i);
    }

    private void removeItemFromDatabase(ToDoListItem i) {
        logEvent("Removing item from database...");
        open();
        int rowsDeleted = database.delete(SQLiteHelper.TABLE, SQLiteHelper.COLUMN_ID + " = " + i.getId(), null);
        if(rowsDeleted > 1)
            Log.e(TAG, "More than one row was removed during SQL delete for item:" + i.toString());
        close();

    }


    public List<ToDoListItem> getItems() {
        logEvent("Getting all items in the database...");
        open();
        Cursor cursor = database.query(SQLiteHelper.TABLE, SQLiteHelper.ALL_COLUMNS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ToDoListItem row = cursorToItem(cursor);
            if(!items.contains(row))
                items.add(row);
            cursor.moveToNext();
        }
        close();
        return items;
    }

    private ToDoListItem cursorToItem(Cursor cursor) {
        String string = cursor.getString(1);
        DateFormat format = new SimpleDateFormat("EE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ToDoListItem i = new ToDoListItem(cursor.getString(3), date, cursor.getString(2), cursor.getInt(4));
        i.setId(cursor.getLong(0));
        return i;
    }

    public void deleteItemWithConfirmation(ToDoListItem selectedItem, Context context, ArrayAdapter<ToDoListItem> arrayAdapter) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    removeItem(selectedItem);
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(context,"Item deleted", Toast.LENGTH_SHORT).show();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public String[] getEntries() {
        List<String> list = new ArrayList<String>();
        for(ToDoListItem t : getItems()){
            list.add(t.getDescription());
        }
        return list.toArray(new String[0]);
    }
    public void emitChange(){
        this.arrayAdapter.notifyDataSetChanged();
    }

    public void setNotification(FragmentActivity activity, Context context, ToDoListItem item) {
        logEvent("Creating notification");
        //TODO make notification appear at time in item.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ToDoList")
                        .setContentText("Don't forget: " + item.getDescription());

        Intent i = new Intent(activity, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    private void logEvent(String desc){
        Log.d(TAG, desc);
    }

    public void saveItem(ToDoListItem i) {
        logEvent("Starting save for item: " + i);
        open();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DATETOCOMPLETE, i.getTimeToComplete().toString());
        values.put(SQLiteHelper.COLUMN_DESCRIPTION, i.getDescription());
        values.put(SQLiteHelper.COLUMN_PRIORITY, i.getPriority());
        values.put(SQLiteHelper.COLUMN_STATUS, i.getStatus());
        int rows = database.update(SQLiteHelper.TABLE,  values, SQLiteHelper.COLUMN_ID + "= " + i.getId(), null);
        if(rows > 1)
            Log.e(TAG, "More than 1 row was affected by the save item method");
        close();
    }

    public void sortByPriority(View v) {
        Collections.sort(items,(t1,t2) -> t1.comparePriority(t2));
        logEvent("sorted by priority");
        emitChange();
    }

    public void sortByDate(View v){
        Collections.sort(items, (t1, t2) -> t1.getTimeToComplete().compareTo(t2.getTimeToComplete()));
        logEvent("sorted by date");
        emitChange();
    }
    public void sortByStatus(View v){
        Collections.sort(items, (t1, t2) -> Integer.compare(t1.getStatus(), t2.getStatus()));
        logEvent("sorted by status");
        emitChange();
    }
}
