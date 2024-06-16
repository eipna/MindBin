package com.serbi.mindbin.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.activities.CreateNote;
import com.serbi.mindbin.adapters.NoteAdapter;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;
import com.serbi.mindbin.models.Note;

import java.util.ArrayList;

public class Notes extends Fragment {

    private View view;
    private FloatingActionButton btn_add_note;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ArrayList<Note> notesArrayList;
    private NoteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        initializeComponents();
        storeNoteData();

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_add_note.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreateNote.class));
            getActivity().finish();
        });
        return view;
    }

    private void initializeComponents() {
        btn_add_note = view.findViewById(R.id.btn_add_note);
        recyclerView = view.findViewById(R.id.recycler_view_note);
        databaseHelper = new DatabaseHelper(getContext());
        notesArrayList = new ArrayList<>();
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getAllNotes();
        while (cursor.moveToNext()) {
            notesArrayList.add(new Note(
                    cursor.getString(1), DateHelper.convertSimpleToDetailedDate(cursor.getString(2)),
                    cursor.getString(3), cursor.getString(4))
            );
        }
    }
}