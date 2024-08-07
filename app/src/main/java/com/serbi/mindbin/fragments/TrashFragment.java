package com.serbi.mindbin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.adapters.NoteAdapter;
import com.serbi.mindbin.helpers.DatabaseHelper;
import com.serbi.mindbin.helpers.DateHelper;
import com.serbi.mindbin.models.NoteModel;

import java.util.ArrayList;

public class TrashFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private NoteAdapter adapter;
    private ArrayList<NoteModel> notesArrayList;
    private ImageView iv_no_notes_trash;
    private FloatingActionButton btn_clear_notes;
    private TextView tv_no_notes_trash;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash, container, false);
        initializeComponents();
        storeNoteData();

        adapter = new NoteAdapter(getContext(), notesArrayList);
        recyclerView.setAdapter(adapter);

        if (preferences.getBoolean("isGridMode", false)) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);

            recyclerView.setLayoutManager(linearLayoutManager);
        }

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
            tv_no_notes_trash.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton("No", (dialog, which) -> { /* Do nothing */ });

        builder.create().show();
    }

    private void initializeComponents() {
        recyclerView = view.findViewById(R.id.recycler_view_note_trash);
        databaseHelper = new DatabaseHelper(getContext());
        iv_no_notes_trash = view.findViewById(R.id.iv_no_notes_trash);
        btn_clear_notes = view.findViewById(R.id.btn_clear_notes);
        tv_no_notes_trash = view.findViewById(R.id.tv_no_notes_trash);
        notesArrayList = new ArrayList<>();
        preferences = getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
    }

    private String getNoteCreationDateType(String date) {
        if (preferences.getBoolean("isSimpleDate", false)) {
            return DateHelper.convertSimpleToNormalDate(date);
        } else {
            return DateHelper.convertSimpleToDetailedDate(date);
        }
    }

    private void storeNoteData() {
        Cursor cursor = databaseHelper.getDeletedNotes();
        if (cursor.getCount() == 0) {
            iv_no_notes_trash.setVisibility(View.VISIBLE);
            tv_no_notes_trash.setVisibility(View.VISIBLE);
            btn_clear_notes.setVisibility(View.GONE);
        } else {
            btn_clear_notes.setVisibility(View.VISIBLE);
            iv_no_notes_trash.setVisibility(View.GONE);
            tv_no_notes_trash.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                notesArrayList.add(new NoteModel(
                        cursor.getInt(0),
                        cursor.getString(1), getNoteCreationDateType(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5))
                );
            }
        }
    }
}