package com.example.longatoj.fraglist;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
public class MainFragment extends ListFragment {

    ToDoListItemDAO toDoListItemDAO;
    private final String TAG = "FRAG";
    ArrayAdapter<ToDoListItem> arrayAdapter;
    public MainFragment() {
        // Required empty public constructor
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

        arrayAdapter = new ArrayAdapter<ToDoListItem>(
                getActivity(), android.R.layout.simple_list_item_1, toDoListItemDAO.getItems());
        setListAdapter(arrayAdapter);
        getListView().setAdapter(arrayAdapter);
        toDoListItemDAO.setAdapter(arrayAdapter);
        Button b = (Button) view.findViewById(R.id.btnAddItem);
        b.setOnClickListener((v)->btnAddItemClicked(v,arrayAdapter));
        
        getListView().setOnItemLongClickListener(this::deleteItem);
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean deleteItem(AdapterView<?> adapterView, View view, int position, long l) {
        ToDoListItem selectedItem = (ToDoListItem) adapterView.getItemAtPosition(position);
        logEvent("List Item was long clicked: " + selectedItem);
        toDoListItemDAO.deleteItem(selectedItem, getContext(), arrayAdapter);
        return true;
    }


    private void btnAddItemClicked(View v, ArrayAdapter<ToDoListItem> arrayAdapter) {
        DialogFragment newFragment = new AddToDoListItemDialog();
        newFragment.show(getFragmentManager(), "dialog");
        logEvent("A new item is being created");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ToDoListItem selectedItem = (ToDoListItem) l.getItemAtPosition(position);

        DialogFragment newFragment = new ViewToDoListItemDialog();
        Bundle b = new Bundle();
        b.putParcelable("item", selectedItem);
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "viewer");

        logEvent("List Item was clicked: " + selectedItem);
    }

    private void logEvent(String desc) {
        Log.d(TAG, desc);
    }
}
