//package com.example.longatoj.fraglist;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.util.Log;
//import android.widget.DatePicker;
//import android.widget.TimePicker;
//
//import java.util.Calendar;
//
///**
// * Created by Jordan on 10/16/2016.
// */
//
//public class DateTimeDialog implements
//        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
//    ToDoListItem item;
//    public DateTimeDialog(ToDoListItem item){
//        this.item = item;
//    }
//
//    public void getDate(Activity a){
//        final Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        logEvent("Starting pop-up for date.");
//        DatePickerDialog datePicker = new DatePickerDialog(a, this, year, month, day);
//        datePicker.show();
//        logEvent("Starting pop-up for time");
//        TimePickerDialog timePicker = new TimePickerDialog(a, this, hour, minute, false);
//        timePicker.show();
//
//    }
//
//    private void logEvent(String s) {
//        Log.d("DateTimePicker", s);
//    }
//
//    @Override
//    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//
//    }
//}
