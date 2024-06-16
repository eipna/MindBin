package com.serbi.mindbin.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.serbi.mindbin.R;
import com.serbi.mindbin.adapters.NoteAdapter;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;
import com.serbi.mindbin.models.Note;

import java.util.ArrayList;

public class Trash extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private ArrayList<Note> notesArrayList;
    private ImageView iv_no_notes_trash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash, container, false);
        initializeComponents();
        storeNoteData();

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private void initializeComponents() {
        recyclerView = view.findViewById(R.id.recycler_view_note_trash);
        databaseHelper = new DatabaseHelper(getContext());
        iv_no_notes_trash = view.findViewById(R.id.iv_no_notes_trash);
        notesArrayList = new ArrayList<>();
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getDeletedNotes();
        if (cursor.getCount() == 0) {
            iv_no_notes_trash.setVisibility(View.VISIBLE);
        } else {
            iv_no_notes_trash.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                notesArrayList.add(new Note(
                        cursor.getInt(0),
                        cursor.getString(1), DateHelper.convertSimpleToDetailedDate(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4))
                );
            }
        }
    }
}