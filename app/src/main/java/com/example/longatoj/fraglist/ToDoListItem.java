package com.example.longatoj.fraglist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * POJO
 */
public class ToDoListItem implements Parcelable {
    private String description;
    private Date timeToComplete;
    private String priority;
    private int status;
    private long id;
    public static String PRIORITY_HIGH = "High";
    public static String PRIORITY_MEDIUM = "Medium";
    public static String PRIORITY_LOW = "Low";
    public static int COMPLETED = 1;
    public static int INCOMPLETE = 0;

    public ToDoListItem(){
        //empty constructor
    }
    public ToDoListItem(String description, Date timeToComplete, String priority, int status) {
        this.description = description;
        this.timeToComplete = timeToComplete;
        this.priority = priority;
        this.status = status;
    }

    protected ToDoListItem(Parcel in) {
        description = in.readString();
        priority = in.readString();
        id = in.readLong();
    }

    public static final Creator<ToDoListItem> CREATOR = new Creator<ToDoListItem>() {
        @Override
        public ToDoListItem createFromParcel(Parcel in) {
            return new ToDoListItem(in);
        }

        @Override
        public ToDoListItem[] newArray(int size) {
            return new ToDoListItem[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public  String getPriority() {
        return this.priority;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return description + " Priority: " + priority;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ToDoListItem)
            return this.id == ((ToDoListItem) obj).getId();
        return super.equals(obj);
    }
    public int comparePriority(ToDoListItem t2) {
        return Integer.compare(getPriorityNum(), t2.getPriorityNum());
    }

    public int getPriorityNum() {
        if(this.priority.equalsIgnoreCase(PRIORITY_HIGH))
            return 1;
        else if(this.priority.equalsIgnoreCase(PRIORITY_MEDIUM))
            return 2;
        else if(this.priority.equalsIgnoreCase(PRIORITY_LOW))
            return 3;
        else return -1;
    }
}
