package com.example.longatoj.fraglist;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToDoListItemDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    Button btnDone;
    TextView txtDateView;
    ToDoListItem item;
    AutoCompleteTextView txtDescription;
    Spinner spinPriority;
    int hour, minute;
    public AddToDoListItemDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_todolistitem_dialog, container, false);
        return view;
    }

    private void btnDoneClicked(View e) {
        item.setDescription(txtDescription.getText().toString());
        item.setPriority(spinPriority.getSelectedItem().toString());
        item.setStatus(ToDoListItem.INCOMPLETE);
        if(item.getTimeToComplete() == null)
            item.setTimeToComplete(Calendar.getInstance().getTime());
        if(!item.getDescription().isEmpty()){
            ToDoListItemDAO.getInstance().addItem(item);
            ToDoListItemDAO.getInstance().setNotification(getActivity(), getContext(), item);
            ToDoListItemDAO.getInstance().emitChange();
            this.dismiss();
        }else if(item.getDescription().isEmpty())
            Toast.makeText(getContext(),"Please enter a description for the item", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnDone = (Button) view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this::btnDoneClicked);

        ArrayAdapter<String> previousEntries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
                ToDoListItemDAO.getInstance().getEntries());
        txtDescription = (AutoCompleteTextView) view.findViewById(R.id.txtDescription);
        txtDescription.setAdapter(previousEntries);

        spinPriority = (Spinner) view.findViewById(R.id.priorities);
        ArrayAdapter<String> listPriorities = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, ToDoListItem.getPriorities());
        spinPriority.setAdapter(listPriorities);

        Button btnDate = (Button) view.findViewById(R.id.txtTime);
        btnDate.setOnClickListener(this::popupDate);

        txtDateView = (TextView) view.findViewById(R.id.textView2);

        item = new ToDoListItem();
        super.onViewCreated(view, savedInstanceState);
    }

    private void popupDate(View view) {
        logEvent("Starting pop-up for date.");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog d = new TimePickerDialog(getActivity(), this, hour, minute, false);
        d.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        logEvent("Setting date");
        item.setTimeToComplete(new GregorianCalendar(year, month, dayOfMonth, hour, minute).getTime());
        txtDateView.setText("Time selected: " + hour%12 + ":" + minute + " " +
                (hour/12 == 0 ? "AM" : "PM") + " " + (month +1) +"/" + dayOfMonth + "/" + year);

    }
    private void logEvent(String desc){
        Log.d("AddItemDialog", desc);
    }

}
