package com.example.longatoj.fraglist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Jordan on 10/16/2016.
 */

public class ViewToDoListItemDialog extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    ToDoListItem item;
    TextView description, time, date;
    Spinner priorities;
    int year, month, dayOfMonth, hour, minute;
    public ViewToDoListItemDialog() {
        //Empty constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        item = (ToDoListItem) getArguments().get("item");
        setTimeVars();
        logEvent("Viewing item: " + item);
        return super.onCreateDialog(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_todolistitem_dialog, container, false);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        ToDoListItemDAO.getInstance().saveItem(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout dl = (LinearLayout) view.findViewById(R.id.layoutDescription);
        description = (TextView) view.findViewById(R.id.txteditDescription);
        dl.setOnClickListener(this::editDescription);
        description.setText(item.getDescription());

        LinearLayout pl = (LinearLayout) view.findViewById(R.id.layoutPriority);
        priorities = (Spinner) view.findViewById(R.id.editPriorities);
        ArrayAdapter<String> listPriorities = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, ToDoListItem.getPriorities());
        priorities.setAdapter(listPriorities);
        priorities.setSelection(listPriorities.getPosition(item.getPriority()));
        priorities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                item.setPriority(s);
                ToDoListItemDAO.getInstance().emitChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button btnCancelEdit = (Button) view.findViewById(R.id.btnEditCancel);
        btnCancelEdit.setOnClickListener((e)->{
            ToDoListItemDAO.getInstance().saveItem(item);
            this.dismiss();

        });

        Button btnDeleteEdit = (Button) view.findViewById(R.id.btnEditDelete);
        btnDeleteEdit.setOnClickListener(view1 -> {
            ToDoListItemDAO.getInstance().removeItem(item);
            ToDoListItemDAO.getInstance().emitChange();
            this.dismiss();
        }
        );
        setupDateArea(view);

        CheckBox cbCompleted = (CheckBox) view.findViewById(R.id.cbCompleted);
        cbCompleted.setChecked(item.getStatus() == ToDoListItem.COMPLETED);
        cbCompleted.setOnCheckedChangeListener(this::cbCompletedChanged);
        super.onViewCreated(view, savedInstanceState);
    }

    private void cbCompletedChanged(CompoundButton compoundButton, boolean b) {
        if(b)
            item.setStatus(ToDoListItem.COMPLETED);
        else
            item.setStatus(ToDoListItem.INCOMPLETE);
        Toast.makeText(getContext(),"Complete status changed", Toast.LENGTH_SHORT).show();
    }

    private void setupDateArea(View view) {
        LinearLayout tl = (LinearLayout) view.findViewById(R.id.layoutTime);
        tl.setOnClickListener(this::popupTime);

        LinearLayout dl = (LinearLayout) view.findViewById(R.id.layoutDate);
        dl.setOnClickListener(this::popupdate);

        time = (TextView) view.findViewById(R.id.editTime);
        time.setText(hour%12 + ":" + minute + " " +
                (hour/12 == 0 ? "AM" : "PM"));

        date = (TextView) view.findViewById(R.id.editDate);
        date.setText((month +1) +"/" + dayOfMonth + "/" + year);

    }

    private void popupdate(View view) {
        logEvent("Starting pop-up for date.");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.show();

    }
    private void popupTime(View view) {
        logEvent("Starting pop-up for Time.");
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog d = new TimePickerDialog(getActivity(), this, hour, minute, false);
        d.show();

    }

    private void editDescription(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter new description of item");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            item.setDescription(input.getText().toString());
            description.setText(input.getText().toString());
            ToDoListItemDAO.getInstance().emitChange();
            Toast.makeText(getContext(),"Description changed", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        logEvent("Setting time.");
        item.setTimeToComplete(new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute).getTime());
        setTimeVars();
        time.setText(hourOfDay%12 + ":" + minute + " " +
                (hourOfDay/12 == 0 ? "AM" : "PM"));
        Toast.makeText(getContext(),"Time changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        logEvent("Setting date");
        item.setTimeToComplete(new GregorianCalendar(year, month, dayOfMonth, hour, minute).getTime());
        setTimeVars();
        date.setText((month +1) +"/" + dayOfMonth + "/" + year);
        Toast.makeText(getContext(),"Date changed", Toast.LENGTH_SHORT).show();

    }
    private void setTimeVars(){
        Calendar c = new GregorianCalendar();
        c.setTime(item.getTimeToComplete());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    private void logEvent(String desc){
        Log.d("ViewItem", desc);
    }
}
