package com.example.longatoj.fraglist;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longatoj on 10/7/2016.
 */
public class ToDoListItemDAO {
    private List<ToDoListItem> items;
    private final String TAG = "DAO";
    private static ToDoListItemDAO instance;

    public static ToDoListItemDAO getInstance(){
        if(instance == null)
            instance = new ToDoListItemDAO();
        return instance;
    }
    private  ToDoListItemDAO() {
        items = new ArrayList<ToDoListItem>();
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
}
