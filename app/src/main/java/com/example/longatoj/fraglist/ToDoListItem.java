package com.example.longatoj.fraglist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * POJO
 */
public class ToDoListItem implements Parcelable {
    private String description;
    private Date timeToComplete;
    private String priority;
    public static String PRIORITY_HIGH = "High";
    public static String PRIORITY_MEDIUM = "Medium";
    public static String PRIORITY_LOW = "Low";

    public ToDoListItem(){
        //empty constructor
    }
    public ToDoListItem(String description, Date timeToComplete, String priority) {
        this.description = description;
        this.timeToComplete = timeToComplete;
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
        ToDoListItemDAO.getInstance().emitChange();
    }

    public void setTimeToComplete(Date timeToComplete) {
        this.timeToComplete = timeToComplete;
        ToDoListItemDAO.getInstance().emitChange();
    }

    public void setPriority(String priority) {
        this.priority = priority;
        ToDoListItemDAO.getInstance().emitChange();
    }

    public Date getTimeToComplete() {
        return timeToComplete;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
    public static String[] getPriorities(){
        String[] list = new String[3];
        list[0] = PRIORITY_HIGH;
        list[1] = PRIORITY_MEDIUM;
        list[2] = PRIORITY_LOW;
        return list;
    }
    @Override
    public String toString() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.getDescription(),
                this.getPriority(),
                this.getTimeToComplete().toString()
        });

    }
}
