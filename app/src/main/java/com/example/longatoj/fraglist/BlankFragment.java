package com.example.longatoj.fraglist;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends ListFragment {

    ToDoListItemDAO toDoListItemDAO;
    private final String TAG = "FRAG";

    public BlankFragment() {
        // Required empty public constructor
    }

    public void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        toDoListItemDAO = ToDoListItemDAO.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ArrayAdapter<ToDoListItem> arrayAdapter = new ArrayAdapter<ToDoListItem>(
                getActivity(), android.R.layout.simple_list_item_1, toDoListItemDAO.getItems());
        setListAdapter(arrayAdapter);
        getListView().setAdapter(arrayAdapter);
        Button b = (Button) view.findViewById(R.id.btnAddItem);
        b.setOnClickListener((v)->btnAddItemClicked(v,arrayAdapter));
        super.onViewCreated(view, savedInstanceState);
    }

    private void btnAddItemClicked(View v, ArrayAdapter<ToDoListItem> arrayAdapter) {
        //TODO add dialog for new ToDoListItem
        toDoListItemDAO.addItem(new ToDoListItem(
                "Hard Coded", Calendar.getInstance().getTime(), ToDoListItem.PRIORITY_LOW));
        arrayAdapter.notifyDataSetChanged();
        DialogFragment newFragment = new AddToDoListItemDialog();
        newFragment.show(getFragmentManager(), "dialog");
        logEvent("A new item is being created");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ToDoListItem selectedItem = (ToDoListItem) l.getItemAtPosition(position);

        logEvent("List Item was clicked: " + selectedItem);
        //TODO open dialog to edit item
    }

    private void logEvent(String desc) {
        Log.d(TAG, desc);
    }
}
