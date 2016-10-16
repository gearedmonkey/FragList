package com.example.longatoj.fraglist;

import java.util.Date;

/**
 * POJO
 */
public class ToDoListItem {
    private String description;
    private Date timeToComplete;
    private String priority;
    public static String PRIORITY_HIGH = "High";
    public static String PRIORITY_MEDIUM = "Medium";
    public static String PRIORITY_LOW = "Low";

    public ToDoListItem(String description, Date timeToComplete, String priority) {
        this.description = description;
        this.timeToComplete = timeToComplete;
        this.priority = priority;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeToComplete(Date timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    @Override
    public String toString() {
        return description;
    }
}
