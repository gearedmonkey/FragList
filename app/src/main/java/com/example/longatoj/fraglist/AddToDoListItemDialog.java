package com.example.longatoj.fraglist;


import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddToDoListItemDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    Button btnDone;
    TextView txtDateView;
    ToDoListItem item;
    AutoCompleteTextView txtDescription;
    Spinner spinPriority;
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
        if(item.getTimeToComplete() == null)
            item.setTimeToComplete(Calendar.getInstance().getTime());
        if(!item.getDescription().isEmpty()){
            ToDoListItemDAO.getInstance().addItem(item);
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
        txtDateView.setText("Time selected: " + hourOfDay%12 + ":" + minute + " " +
                (hourOfDay/12 == 0 ? "AM" : "PM"));

    }

    private void logEvent(String desc){
        Log.d("AddItemDialog", desc);
    }

}
