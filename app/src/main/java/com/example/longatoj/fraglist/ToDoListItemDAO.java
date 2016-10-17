package com.example.longatoj.fraglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by longatoj on 10/7/2016.
 */
public class ToDoListItemDAO {
    private ArrayAdapter<ToDoListItem> arrayAdapter = null;
    private List<ToDoListItem> items;
    private final String TAG = "DAO";
    private static ToDoListItemDAO instance;

    public static ToDoListItemDAO getInstance(){
        if(instance == null)
            instance = new ToDoListItemDAO();
        return instance;
    }
    public void setAdapter(ArrayAdapter<ToDoListItem> arrayAdapter){
        this.arrayAdapter = arrayAdapter;
    }
    private ToDoListItemDAO() {
        items = new ArrayList<ToDoListItem>();
        addItem(new ToDoListItem("TEST", Calendar.getInstance().getTime(), ToDoListItem.PRIORITY_HIGH));
    }
    public void addItem(ToDoListItem i){
        logEvent("Adding item");
        items.add(i);
    }
    public void removeItem(ToDoListItem i){
        logEvent("Revmoing Item");
        items.remove(i);
    }
    private void logEvent(String desc){
        Log.d(TAG, desc);
    }

    public List<ToDoListItem> getItems() {
        return items;
    }

    public void deleteItem(ToDoListItem selectedItem, Context context, ArrayAdapter<ToDoListItem> arrayAdapter) {
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
}
