package com.serbi.mindbin.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.activities.Main;
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
    private FloatingActionButton btn_clear_notes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash, container, false);
        initializeComponents();
        storeNoteData();

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_clear_notes.setOnClickListener(v -> clearTrashNotes());
        return view;
    }

    private void clearTrashNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Empty Trash");
        builder.setMessage("This will clear all notes inside the trash");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            databaseHelper.clearTrashNotes();
            notesArrayList.clear();
            adapter.notifyDataSetChanged();

            btn_clear_notes.setVisibility(View.GONE);
            iv_no_notes_trash.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton("No", (dialog, which) -> { /* Do nothing */ });

        builder.create().show();
    }

    private void initializeComponents() {
        recyclerView = view.findViewById(R.id.recycler_view_note_trash);
        databaseHelper = new DatabaseHelper(getContext());
        iv_no_notes_trash = view.findViewById(R.id.iv_no_notes_trash);
        btn_clear_notes = view.findViewById(R.id.btn_clear_notes);
        notesArrayList = new ArrayList<>();
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getDeletedNotes();
        if (cursor.getCount() == 0) {
            iv_no_notes_trash.setVisibility(View.VISIBLE);
            btn_clear_notes.setVisibility(View.GONE);
        } else {
            btn_clear_notes.setVisibility(View.VISIBLE);
            iv_no_notes_trash.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                notesArrayList.add(new Note(
                        cursor.getInt(0),
                        cursor.getString(1), DateHelper.convertSimpleToDetailedDate(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5))
                );
            }
        }
    }
}